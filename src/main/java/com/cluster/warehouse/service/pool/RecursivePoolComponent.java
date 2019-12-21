package com.cluster.warehouse.service.pool;

import com.cluster.warehouse.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RecursivePoolComponent extends RecursiveAction {

	@Autowired
	private ApplicationProperties properties;

    @Autowired
    private JoiningComponent joiningComponent;

    @Autowired
    private ApplicationContext applicationContext;

    public static Map<String, Integer> batchResultMap = new HashMap<>();
	private final List<Map<?, ?>> readLines;
    private final Path path;

	public RecursivePoolComponent(List<Map<?, ?>> readLines, Path path) {
        this.readLines = readLines;
        this.path = path;
    }

    @Override
    public void compute() {
		int batchSize = properties.getBatch().getUpload().getBatchSize();
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

	private synchronized List<RecursivePoolComponent> createSubtasks() {
		List<RecursivePoolComponent> subtasks = new ArrayList<>();

        int size = readLines.size();

		List<Map<?, ?>> listOne = readLines.subList(0, (size + 1) / 2);
		List<Map<?, ?>> listTwo = readLines.subList((size + 1) / 2, size);

		subtasks.add(applicationContext.getBean(RecursivePoolComponent.class, new ArrayList<>(listOne), path));
		subtasks.add(applicationContext.getBean(RecursivePoolComponent.class, new ArrayList<>(listTwo), path));

        return subtasks;
    }
}
