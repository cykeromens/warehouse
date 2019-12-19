package com.cluster.warehouse.service.dealpool;

import com.cluster.warehouse.domain.Summary;
import com.cluster.warehouse.service.SummaryService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ForkJoinService {

    private final ApplicationContext applicationContext;
    private final SummaryService summaryService;

    public ForkJoinService(ApplicationContext applicationContext, SummaryService summaryService) {
        this.applicationContext = applicationContext;
        this.summaryService = summaryService;
    }

    // ForkJoinPool will start 1 thread for each available core
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    public static final ForkJoinPool forkJoinPool = new ForkJoinPool(NUMBER_OF_CORES);

    private static final Logger log = Logger.getLogger(ForkJoinService.class.getName());

    public void fileToDatabase(Path path) throws Exception {

        log.info("Reading file lines into a List<String> ...");

        List<String> allLines = Files.readAllLines(path);
        int size = allLines.size();
        List<String> lines = allLines.subList(1, size);

        log.info(() -> "Read a total of " + size
                + " lines and inserting lines");

        log.info("Start inserting ...");

        StopWatch watch = new StopWatch();
        watch.start();
        Map<String, Integer> resultMap = forkjoin(lines, path);
        watch.stop();
        log.log(Level.INFO, "Stop inserting. \n Total time: {0} ms ({1} s)",
                new Object[]{watch.getTotalTimeMillis(), watch.getTotalTimeSeconds()});
        Integer valid = resultMap.get("valid");
        Integer invalid = resultMap.get("invalid");
        if (size - invalid != 0) {
            Summary summary = new Summary();
            summary.setFileName(path.toString());
            summary.setProcessDuration(watch.getTotalTimeSeconds());
            summary.setTotalImported(valid + invalid);
            summary.setTotalValid(valid);
            summary.setTotalNotValid(invalid);
            log.log(Level.INFO, "Saving summary");
            summaryService.save(summary);
        }
    }

    private Map<String, Integer> forkjoin(List<String> lines, Path path) {
        ForkingComponent forkingComponent
                = applicationContext.getBean(ForkingComponent.class, lines, path);
        forkJoinPool.invoke(forkingComponent);
        do {
            System.out.printf("******************************************\n");
            System.out.printf("Main: Parallelism: %d\n", forkJoinPool.getParallelism());
            System.out.printf("Main: Active Threads: %d\n", forkJoinPool.getActiveThreadCount());
            System.out.printf("Main: Task Count: %d\n", forkJoinPool.getQueuedTaskCount());
            System.out.printf("Main: Steal Count: %d\n", forkJoinPool.getStealCount());
            System.out.printf("******************************************\n");
        } while (!forkingComponent.isDone());
        return ForkingComponent.batchResultMap;
    }

}
