package com.cluster.warehouse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Configure the converters to use the ISO format for dates by default.
 */
@Configuration
public class DateTimeFormatConfiguration implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
		registrar.setUseIsoFormat(true);
		registrar.registerFormatters(registry);
	}
}

class ZonedDateTimeReadConverter implements Converter<Date, ZonedDateTime> {
	@Override
	public ZonedDateTime convert(Date date) {
		return date.toInstant().atZone(ZoneOffset.UTC);
	}
}

class ZonedDateTimeWriteConverter implements Converter<ZonedDateTime, Date> {
	@Override
	public Date convert(ZonedDateTime zonedDateTime) {
		return Date.from(zonedDateTime.toInstant());
	}
}