package com.cluster.warehouse.service.dealpool;

import com.cluster.warehouse.domain.Deal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

@Component
@Scope("prototype")
public class ForkingComponent extends RecursiveAction {

    @Value("${jdbc.batch.size}")
    private int batchSize;

    @Value("${app.upload.delimiter}")
    private static final String DELIMITER = ";";

    @Autowired
    private JoiningComponent joiningComponent;

    @Autowired
    private ApplicationContext applicationContext;

    private final List<String> readLines;

    public ForkingComponent(List<String> readLines) {
        this.readLines = readLines;
    }

    @Override
    public void compute() {
        if (readLines.size() > batchSize) {
            ForkJoinTask.invokeAll(createSubtasks());
        } else {
            joiningComponent.executeBatch(getBatchDeal());
        }
    }

    private synchronized List<Deal> getBatchDeal() {
        List<Deal> deals = new ArrayList<>();
        for (String line : readLines) {
            if (!line.isEmpty()) {
                String data[] = line.split(DELIMITER);
                final long datum5 = Long.parseLong(data[5]);
                final ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(datum5), ZoneOffset.UTC);
                Deal deal = new Deal().tagId(data[0])
                        .fromIsoCode(data[1])
                        .fromCountry(data[2])
                        .toIsoCode(data[3])
                        .toCountry(data[4])
                        .time(dateTime)
                        .amount(new BigDecimal(data[6]))
                        .source("filePath")
                        .sourceFormat("FilenameUtils.getExtension(filePath)")
                        .uploadedDate(LocalDate.now());
                deals.add(deal);
            }
        }
        return deals;
    }

    private List<ForkingComponent> createSubtasks() {
        List<ForkingComponent> subtasks = new ArrayList<>();

        int size = readLines.size();

        List<String> jsonListOne = readLines.subList(0, (size + 1) / 2);
        List<String> jsonListTwo = readLines.subList((size + 1) / 2, size);

        subtasks.add(applicationContext.getBean(ForkingComponent.class, new ArrayList<>(jsonListOne)));
        subtasks.add(applicationContext.getBean(ForkingComponent.class, new ArrayList<>(jsonListTwo)));

        return subtasks;
    }
}
