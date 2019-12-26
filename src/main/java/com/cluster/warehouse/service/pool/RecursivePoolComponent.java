package com.cluster.warehouse.service.pool;

import com.cluster.warehouse.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * A pool component that is called recursively
 */
@Component
@Scope("prototype")
public class RecursivePoolComponent extends RecursiveAction {
	private static final Logger log = LoggerFactory.getLogger(RecursivePoolComponent.class);

	@Autowired
	private ApplicationProperties properties;

	@Autowired
	private JoiningComponent joiningComponent;

	@Autowired
	private ApplicationContext applicationContext;

	static Map<String, Integer> batchResultMap = new HashMap<>();
	private final List<Map<String, String>> readLines;

	public RecursivePoolComponent(List<Map<String, String>> readLines) {
		this.readLines = readLines;
	}

	@Override
	public void compute() {
		int batchSize = properties.getBatch().getUpload().getBatchSize();
		if (readLines.size() > batchSize) {
			log.debug("Creating more sub task... ");
			ForkJoinTask.invokeAll(createSubtasks());
		} else {
			Map<String, Integer> batchMap = joiningComponent.executeBatch(readLines);
			mergeMap(batchMap);
		}
	}

	private void mergeMap(Map<String, Integer> newMap) {
		newMap.forEach((k, v) -> batchResultMap.merge(k, v, Integer::sum));
	}

	private synchronized List<RecursivePoolComponent> createSubtasks() {
		List<RecursivePoolComponent> subtasks = new ArrayList<>();
		int size = readLines.size();

		List<Map<String, String>> listOne = readLines.subList(0, (size + 1) / 2);
		List<Map<String, String>> listTwo = readLines.subList((size + 1) / 2, size);

		subtasks.add(applicationContext.getBean(RecursivePoolComponent.class, new ArrayList<>(listOne)));
		subtasks.add(applicationContext.getBean(RecursivePoolComponent.class, new ArrayList<>(listTwo)));

		return subtasks;
	}
}
