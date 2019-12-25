package com.cluster.warehouse.service.pool;

import com.cluster.warehouse.domain.Deal;
import com.cluster.warehouse.domain.DealValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cluster.warehouse.config.Constants.*;
import static com.cluster.warehouse.service.pool.ForkJoinService.extension;
import static com.cluster.warehouse.service.pool.ForkJoinService.fileName;

@Component
public class JoiningComponent {

    private final Logger log = LoggerFactory.getLogger(JoiningComponent.class);
	final private ObjectMapper mapper;
	private MongoTemplate mongoTemplate;

	public JoiningComponent(MongoTemplate mongoTemplate, ObjectMapper mapper) {
		this.mapper = new ObjectMapper();
		this.mongoTemplate = mongoTemplate;
		final DateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
		mapper.setDateFormat(df);
	}

	synchronized Map<String, Integer> executeBatch(List<Map<String, String>> dealsMap) {
		log.info("About to process set with size: {}", dealsMap.size());
		Map<String, Integer> result = new HashMap<>();
		result.put(VALID_COUNT, 0);
		result.put(INVALID_COUNT, 0);
		List<Document> validDeals = new ArrayList<>();
		List<Document> invalidDeals = new ArrayList<>();

		dealsMap.forEach(f -> {
			f.put(FILE_SOURCE, fileName);
			f.put(FILE_TYPE, extension);
			f.put(UPLOADED_ON, LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
			try {
				String jsonString = mapper.writeValueAsString(f);
				String error = isValidJSON(jsonString);
				if (error.isEmpty()) { //checks if a json is valid
					Deal deal = mapper.readValue(jsonString, Deal.class);
					Document document = getDocument(deal);
					validDeals.add(document);
				} else {
					JsonNode jsonNode = mapper.readTree(jsonString);
					((ObjectNode) jsonNode).put(FAILED_REASON, error);
//					InvalidDeal invalidDeal = mapper.readValue(jsonString, InvalidDeal.class);
					Document document = Document.parse(jsonString.toString());
					invalidDeals.add(document);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		if (!validDeals.isEmpty()) {
			mongoTemplate.getCollection(VALID_DEAL).insertMany(validDeals);
		}
		if (!invalidDeals.isEmpty()) {
			mongoTemplate.getCollection(INVALID_DEAL).insertMany(invalidDeals);
		}
		result.put(VALID_COUNT, validDeals.size());
		result.put(INVALID_COUNT, invalidDeals.size());

		return result;
    }

	private synchronized String isValidJSON(final String jsonString) {
		try {
			Deal deal = mapper.readValue(jsonString, Deal.class);
			return DealValidator.validate(deal);
		} catch (Exception e) {
			if (e instanceof InvalidFormatException) {
				InvalidFormatException ief = (InvalidFormatException) e;
				Throwable cause = ief.getCause();
				if (cause instanceof DateTimeParseException) {
					return FAILED_REQUIRED_TIME;
				}
				return FAILED_REQUIRED_AMOUNT;
			}
			return e.getMessage();
		}
	}

	private synchronized Document getDocument(Deal deal) {
		Map<String, Object> values = new HashMap<>();
		values.put(_TO_ISO_CODE, deal.getToIsoCode());
		values.put(_FROM_ISO_CODE, deal.getFromIsoCode());
		values.put(FILE_SOURCE, deal.getSource());
		values.put(_FILE_TYPE, deal.getFileType());
		values.put(AMOUNT, deal.getAmount());
		values.put(TIME, deal.getTime());
		values.put(_UPLOADED_ON, deal.getUploadedOn());
		values.put(ID, deal.getId());
		return new Document(values);
	}
}
