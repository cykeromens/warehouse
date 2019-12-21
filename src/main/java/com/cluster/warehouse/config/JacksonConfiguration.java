package com.cluster.warehouse.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@Configuration
public class JacksonConfiguration {

	/**
	 * Support for Java date and time API.
	 *
	 * @return the corresponding Jackson module.
	 */
	@Bean
	public JavaTimeModule javaTimeModule() {
		return new JavaTimeModule();
	}

	@Bean
	public Jdk8Module jdk8TimeModule() {
		return new Jdk8Module();
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper build = JsonMapper.builder() // or different mapper for other format
				.addModule(new ParameterNamesModule())
				.addModule(new Jdk8Module())
				.addModule(new JavaTimeModule())
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.build();
//        SimpleModule module = new SimpleModule();
//        module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
//        build.registerModule(module);
		return build;
	}


	/*
	 * Jackson Afterburner module to speed up serialization/deserialization.
	 */
	@Bean
	public AfterburnerModule afterburnerModule() {
		return new AfterburnerModule();
	}

	/*
	 * Module for serialization/deserialization of RFC7807 Problem.
	 */
	@Bean
	ProblemModule problemModule() {
		return new ProblemModule();
	}

	/*
	 * Module for serialization/deserialization of ConstraintViolationProblem.
	 */
	@Bean
	ConstraintViolationProblemModule constraintViolationProblemModule() {
		return new ConstraintViolationProblemModule();
	}

}