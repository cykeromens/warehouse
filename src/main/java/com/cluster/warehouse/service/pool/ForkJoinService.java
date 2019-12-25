package com.cluster.warehouse.service.pool;

import com.cluster.warehouse.config.ApplicationProperties;
import com.cluster.warehouse.domain.Report;
import com.cluster.warehouse.domain.Summary;
import com.cluster.warehouse.service.ReportService;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

import static com.cluster.warehouse.config.Constants.*;

/**
 * Create a @{@link ForkJoinPool} that manages @{@link java.util.concurrent.RecursiveAction}
 */
@Service
public class ForkJoinService {

    // ForkJoinPool will start 1 thread for each available core
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    public static final ForkJoinPool forkJoinPool = new ForkJoinPool(NUMBER_OF_CORES);
	static String fileName;
	static String extension;

    private static final Logger log = LoggerFactory.getLogger(ForkJoinService.class);

    private final ApplicationContext applicationContext;
    private final ReportService reportService;
    private final MongoTemplate mongoTemplate;
    private final ApplicationProperties properties;

    public ForkJoinService(ApplicationContext applicationContext, ApplicationProperties properties,
                           MongoTemplate mongoTemplate, ReportService reportService) {
        this.applicationContext = applicationContext;
        this.properties = properties;
        this.reportService = reportService;
        this.mongoTemplate = mongoTemplate;
    }

    public Map<String, Integer> beginProcess(MultipartFile file, Path path) {
		log.debug("File about to be processed in a {} core computer ");
		if (NUMBER_OF_CORES < 8) {
			log.debug("Great performs can be achieved on a high scared PC from at least 8 cores.");
		}

        Map<String, Integer> resultMap;
		fileName = file.getOriginalFilename();
		extension = FilenameUtils.getExtension(fileName);
        try {
            StopWatch watch = new StopWatch();
            watch.start();
            log.debug("Counting time started");

            CsvSchema csv = CsvSchema.emptySchema()
                    .withHeader()
                    .withColumnSeparator(properties.getBatch().getUpload().getDelimiter());
            CsvMapper csvMapper = new CsvMapper();
            MappingIterator<Map<String, String>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv).readValues(path.toFile());
            List<Map<String, String>> list = mappingIterator.readAll();

			log.info("About to start forkjoin pool processing.");

            resultMap = forkjoin(list);
            watch.stop();

			log.info("Stop inserting. Total time: {} ms ({} s) on core {}",
					watch.getTotalTimeMillis(),
					watch.getTotalTimeSeconds(), NUMBER_OF_CORES);
            log.info("Done Processing file {}!", path.getFileName());

            saveSummary(resultMap, watch.getTotalTimeSeconds());
        } catch (Exception e) {
            log.error("Batch update failed on : {}", e.getMessage());
            e.printStackTrace();
            resultMap = new HashMap<>();
            resultMap.put("error", 500);
        }
        return resultMap;
    }

    private Map<String, Integer> forkjoin(List<Map<String, String>> deals) {
        log.info("Creating forkjoin pool");
        RecursivePoolComponent recursivePoolComponent
                = applicationContext.getBean(RecursivePoolComponent.class, deals);
        forkJoinPool.invoke(recursivePoolComponent);
        do {
            log.debug("******************************************\n");
            log.debug("Main: Parallelism: {}", forkJoinPool.getParallelism());
            log.debug("Main: Active Threads: {}", forkJoinPool.getActiveThreadCount());
            log.debug("Main: Task Count: {}", forkJoinPool.getQueuedTaskCount());
            log.debug("Main: Steal Count: {}", forkJoinPool.getStealCount());
            log.debug("******************************************\n");
        } while (!recursivePoolComponent.isDone());
        return RecursivePoolComponent.batchResultMap;
    }

    private void saveSummary(Map<String, Integer> result, double duration) {
        log.info("Saving summary...");
        Query query = new Query();
		query.addCriteria(Criteria.where("fileName").is(fileName));
        //summary on time
        int validCount = result.get(VALID_COUNT);
        int invalidCount = result.get(INVALID_COUNT);
        Update update = new Update();
		update.set(TOTAL, validCount + invalidCount);
		update.set(VALID_COUNT, validCount);
		update.set(INVALID_COUNT, invalidCount);
		update.set(DATE, LocalDate.now());
		update.set(DURATION, duration);
        mongoTemplate.upsert(query, update, Summary.class);
        log.info("Summary updated!");

        List<Report> reports = reportService.generateReport();
        saveReport(reports);
        log.info("Reports updated!");
    }

    private void saveReport(List<Report> reports) {
        log.info("Updating report...");
        reports.forEach(report -> {
            report.lastUpdated(LocalDate.now());
            Query query = new Query();
			query.addCriteria(Criteria.where(FROM_ISO_CODE)
                    .is(report.getFromIsoCode())
					.and(TO_ISO_CODE).is(report.getToIsoCode()));
            Update update = new Update();
			update.set(FROM_ISO_CODE, report.getFromIsoCode());
			update.set(TO_ISO_CODE, report.getToIsoCode());
			update.set(TOTAL, report.getTotal());
			update.set(LAST_UPDATED, LocalDate.now());
            mongoTemplate.upsert(query, update, Report.class);
        });
    }
}
