package com.cluster.warehouse.web.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.util.Map;

import static com.cluster.warehouse.config.Constants.INVALID_COUNT;
import static com.cluster.warehouse.config.Constants.VALID_COUNT;

/**
 * Utility class for HTTP headers creation.
 */
public final class HeaderUtil {

    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    private static final String APPLICATION_NAME = "warehouseApp";

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-alert", message);
        headers.add("X-" + APPLICATION_NAME + "-params", param);
        return headers;
    }

	public static HttpHeaders createFileUploadAlert(Map<String, Integer> result, String param) {
		int valid = result.get(VALID_COUNT);
		int invalid = result.get(INVALID_COUNT);

		int total = invalid + valid;
		if (result.size() > 2) {
			return createAlert("This file " + param + " has been uploaded before. Valid: " +
					valid + " Invalid: " +
					invalid, param);
		}
		return createAlert("Total file count: " + total + " Valid: " +
				valid + " Invalid: " +
				invalid, param);
	}

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-error", defaultMessage);
        headers.add("X-" + APPLICATION_NAME + "-params", entityName);
        return headers;
    }
}
