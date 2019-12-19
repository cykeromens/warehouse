package com.cluster.warehouse.service.dealpool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

@Component
@Scope("prototype")
public class ForkingComponent extends RecursiveAction {

    @Value("${jdbc.batch.size}")
    private int batchSize;

    @Autowired
    private JoiningComponent joiningComponent;

    @Autowired
    private ApplicationContext applicationContext;

    public static Map<String, Integer> batchResultMap = new HashMap<>();
    private final List<String> readLines;
    private final Path path;

    public ForkingComponent(List<String> readLines, Path path) {
        this.readLines = readLines;
        this.path = path;
    }

    @Override
    public void compute() {
        if (readLines.size() > batchSize) {
            ForkJoinTask.invokeAll(createSubtasks());
        } else {
            Map<String, Integer> batchMap = joiningComponent.executeBatch(readLines, path);
            mergeMap(batchMap);
        }
    }

    public void mergeMap(Map<String, Integer> newMap) {
        newMap.forEach((k, v) -> batchResultMap.merge(k, v, Integer::sum));
    }

    private synchronized List<ForkingComponent> createSubtasks() {
        List<ForkingComponent> subtasks = new ArrayList<>();

        int size = readLines.size();

        List<String> listOne = readLines.subList(0, (size + 1) / 2);
        List<String> listTwo = readLines.subList((size + 1) / 2, size);

        subtasks.add(applicationContext.getBean(ForkingComponent.class, new ArrayList<>(listOne), path));
        subtasks.add(applicationContext.getBean(ForkingComponent.class, new ArrayList<>(listTwo), path));

        return subtasks;
    }
}
