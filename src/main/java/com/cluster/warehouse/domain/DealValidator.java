package com.cluster.warehouse.domain;

import org.springframework.stereotype.Component;

import javax.validation.Validator;

@Component
public class DealValidator {

	private static Validator validator;

	public DealValidator(Validator validator) {
		DealValidator.validator = validator;
	}

	public static String validate(Deal deal) {
		try {
			DealValidator.validator.validate(deal);
			return "";
		} catch (Exception ex) {
			return ex.getMessage();
		}
	}
}
