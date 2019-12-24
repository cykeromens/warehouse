package com.cluster.warehouse.service.pool;

import com.cluster.warehouse.domain.Deal;
import com.cluster.warehouse.domain.InvalidDeal;
import com.cluster.warehouse.repository.DealRepository;
import com.cluster.warehouse.repository.InvalidDealRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cluster.warehouse.config.Constants.INVALID_COUNT;
import static com.cluster.warehouse.config.Constants.VALID_COUNT;
import static com.cluster.warehouse.service.pool.ForkJoinService.extension;
import static com.cluster.warehouse.service.pool.ForkJoinService.fileName;

@Component
public class JoiningComponent {

    private final Logger log = LoggerFactory.getLogger(JoiningComponent.class);
	final private ObjectMapper mapper;
	//	final private DealRepository dealRepository;
	final private InvalidDealRepository invalidDealRepository;

	public JoiningComponent(ObjectMapper mapper,
							InvalidDealRepository invalidDealRepository,
							DealRepository dealRepository) {
		this.mapper = mapper;
//		this.dealRepository = dealRepository;
		this.invalidDealRepository = invalidDealRepository;
	}

	synchronized Map<String, Integer> executeBatch(List<Map<String, String>> dealsMap) {
		log.info("About to process set with size: {}", dealsMap.size());
		Map<String, Integer> result = new HashMap<>();
		result.put(VALID_COUNT, 0);
		result.put(INVALID_COUNT, 0);

		List<Deal> validDeals = new ArrayList<>();
		List<InvalidDeal> invalidDeals = new ArrayList<>();

		dealsMap.forEach(f -> {
			f.put("source", fileName);
			f.put("fileType", extension);
			f.put("uploadedOn", LocalDate.now().toString());
			try {
				String jsonString = mapper.writeValueAsString(f);
				String error = isValidJSON(jsonString);
				if (error.isEmpty()) { //checks if a json is valid
					Deal deal = mapper.readValue(jsonString, Deal.class);
					validDeals.add(deal);
				} else {
					JsonNode jsonNode = mapper.readTree(jsonString);
					((ObjectNode) jsonNode).put("reason", error);
					invalidDeals.add(mapper.readValue(jsonString, InvalidDeal.class));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
//		dealRepository.saveAll(validDeals);
//		invalidDealRepository.saveAll(invalidDeals);

		result.put(VALID_COUNT, validDeals.size());
		result.put(INVALID_COUNT, invalidDeals.size());

		return result;
    }

	private synchronized String isValidJSON(final String json) {
		try {
			JsonNode jsonNode = mapper.readTree(json);
			if (jsonNode.size() != 8) {
				return "Some field elements are missing";
			}
			JsonNode fromIsoCode = jsonNode.get("fromIsoCode");
			JsonNode toIsoCode = jsonNode.get("toIsoCode");
			if (fromIsoCode.asText().length() != 3 || toIsoCode.asText().length() != 3) {
				return "ISO code for currency should be 3";
			}
		} catch (JsonProcessingException e) {
			return "Error processing file";
		}
		return "";
	}
}
