package com.cluster.warehouse.service.pool;

import com.cluster.warehouse.config.ApplicationProperties;
import com.cluster.warehouse.domain.Summary;
import com.cluster.warehouse.service.ReportService;
import com.cluster.warehouse.service.SummaryService;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

@Service
public class ForkJoinService {

    // ForkJoinPool will start 1 thread for each available core
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    public static final ForkJoinPool forkJoinPool = new ForkJoinPool(NUMBER_OF_CORES);

    private static final Logger log = LoggerFactory.getLogger(ForkJoinService.class.getName());

    private final ApplicationContext applicationContext;
    private final SummaryService summaryService;
    private final ReportService reportService;
    private ApplicationProperties properties;

    public ForkJoinService(ApplicationContext applicationContext, ApplicationProperties properties,
                           SummaryService summaryService, ReportService reportService) {
        this.applicationContext = applicationContext;
        this.properties = properties;
        this.summaryService = summaryService;
        this.reportService = reportService;
    }

    public Map<String, Integer> beginProcess(MultipartFile file, Path path) {
        Map<String, Integer> resultMap;
        try {

            log.debug("Coping file to directory");
            CsvSchema csv = CsvSchema.emptySchema()
                    .withHeader()
                    .withColumnSeparator(properties.getBatch().getUpload().getDelimiter());
            CsvMapper csvMapper = new CsvMapper();
            MappingIterator<Map<String, String>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv).readValues(path.toFile());

            List<Map<String, String>> list = mappingIterator.readAll();
            StopWatch watch = new StopWatch();
            watch.start();

            resultMap = forkjoin(list, path);

            watch.stop();

            log.info("Stop inserting. \n Total time: {0} ms ({1} s)",
                    new Object[]{watch.getTotalTimeMillis(), watch.getTotalTimeSeconds()});

            Integer valid = resultMap.get("valid");
            Integer invalid = resultMap.get("invalid");
            long total = valid + invalid;

            log.info("Saving summary...");

            Summary summary = new Summary()
                    .duration(watch.getTotalTimeSeconds())
                    .total(total)
                    .valid(valid.longValue())
                    .invalid(invalid.longValue())
                    .date(LocalDate.now());
            summary.setFileName(path.toFile().toString());
            summaryService.save(summary);
            log.info("Done Processing!", file.getName());
            reportService.generateReport();
            log.info("Report updated!", file.getName());

        } catch (Exception e) {
            log.error("Batch update failed: ", e.getMessage());
            e.printStackTrace();
            resultMap = new HashMap<>();
            resultMap.put("error", 500);
        }
        return resultMap;
    }

    private Map<String, Integer> forkjoin(List<Map<String, String>> lines, Path path) {
        RecursivePoolComponent recursivePoolComponent
                = applicationContext.getBean(RecursivePoolComponent.class, lines, path);
        forkJoinPool.invoke(recursivePoolComponent);
        do {
            System.out.printf("******************************************\n");
            System.out.printf("Main: Parallelism: %d\n", forkJoinPool.getParallelism());
            System.out.printf("Main: Active Threads: %d\n", forkJoinPool.getActiveThreadCount());
            System.out.printf("Main: Task Count: %d\n", forkJoinPool.getQueuedTaskCount());
            System.out.printf("Main: Steal Count: %d\n", forkJoinPool.getStealCount());
            System.out.printf("******************************************\n");
        } while (!recursivePoolComponent.isDone());
        return RecursivePoolComponent.batchResultMap;
    }

}
