package com.cluster.warehouse.service.dealpool;

import com.cluster.warehouse.domain.Summary;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ForkJoinService {

    private final ApplicationContext applicationContext;

    public ForkJoinService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    // ForkJoinPool will start 1 thread for each available core
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    public static final ForkJoinPool forkJoinPool = new ForkJoinPool(NUMBER_OF_CORES);

    private static final Logger log = Logger.getLogger(ForkJoinService.class.getName());

    public void fileToDatabase(Path path) throws IOException {

        log.info("Reading file lines into a List<String> ...");

        // fetch 200000+ lines
        List<String> allLines = Files.readAllLines(path);
        int size = allLines.size();
        // run on a snapshot of NUMBER_OF_LINES_TO_INSERT lines
        List<String> lines = allLines.subList(1, size);

        log.info(() -> "Read a total of " + size
                + " lines and inserting lines");

        log.info("Start inserting ...");

        StopWatch watch = new StopWatch();
        watch.start();
        forkjoin(lines);
        watch.stop();

        Summary summary = new Summary();
        summary.setFileName(path.toString());
        summary.setProcessDuration(watch.getTotalTimeSeconds());
        summary.setTotalImported(size);
        log.log(Level.INFO, "Stop inserting. \n Total time: {0} ms ({1} s)",
                new Object[]{watch.getTotalTimeMillis(), watch.getTotalTimeSeconds()});
    }

    private void forkjoin(List<String> lines) {
        ForkingComponent forkingComponent
                = applicationContext.getBean(ForkingComponent.class, lines);
        forkJoinPool.invoke(forkingComponent);
        do {
            System.out.printf("******************************************\n");
            System.out.printf("Main: Parallelism: %d\n", forkJoinPool.getParallelism());
            System.out.printf("Main: Active Threads: %d\n", forkJoinPool.getActiveThreadCount());
            System.out.printf("Main: Task Count: %d\n", forkJoinPool.getQueuedTaskCount());
            System.out.printf("Main: Steal Count: %d\n", forkJoinPool.getStealCount());
            System.out.printf("******************************************\n");
        } while (!forkingComponent.isDone());
    }
}
