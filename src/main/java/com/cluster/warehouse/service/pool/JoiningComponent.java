package com.cluster.warehouse.service.pool;

import com.cluster.warehouse.domain.Deal;
import com.cluster.warehouse.domain.InvalidDeal;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class JoiningComponent {

    private final Logger log = LoggerFactory.getLogger(JoiningComponent.class);
	final private ObjectMapper mapper;
	final private MongoTemplate mongoTemplate;

	public JoiningComponent(ObjectMapper mapper, MongoTemplate mongoTemplate) {
		this.mapper = mapper;
		this.mongoTemplate = mongoTemplate;
    }

	synchronized Map<String, Integer> executeBatch(List<Map<?, ?>> dealsMap, Path path) {
		AtomicInteger failedCount = new AtomicInteger();
		AtomicInteger affectedCount = new AtomicInteger();
        Map<String, Integer> result = new HashMap<>();

		try {
			Path fileName = path.getFileName();
			String extension = FilenameUtils.getExtension(fileName.toString());
			log.info("About to save batch.");
			String jsonString = mapper.writeValueAsString(dealsMap);
			List<Deal> deals = mapper.readValue(jsonString, new TypeReference<List<Deal>>() {
			});
			BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Deal.class);
			BulkOperations bulkOpsInvalid = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, InvalidDeal.class);

			deals.stream().filter(Objects::nonNull).forEach(deal -> {

				try {
					deal.source(fileName.toString()).fileType(extension).uploadedOn(LocalDate.now());
					bulkOps.insert(deal);
					affectedCount.addAndGet(1);
				} catch (Exception ex) {
					log.error("{0} has occured, will save deal {1} on invalid table", ex.getMessage(), deal);
					try {
						bulkOpsInvalid.insert(new InvalidDeal(deal, ex.getStackTrace()[0].toString()));
						failedCount.addAndGet(1);
						log.info(("Deal saved succesfully on Invalid Document"));
					} catch (Exception e) {
						log.error("File {} not supported or required delimiter not set.", extension);
						throw new RuntimeException(e.getMessage());
					}
				}
			});
			if (affectedCount.get() > 0) {
				bulkOps.execute();
            }
			if (failedCount.get() > 0) {
				bulkOpsInvalid.execute();
			}
			log.info("Deal saving batch!");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		result.put("valid", affectedCount.intValue());
		result.put("invalid", failedCount.intValue());
        return result;
    }

}
