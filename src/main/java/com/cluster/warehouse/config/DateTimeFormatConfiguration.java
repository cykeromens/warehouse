package com.cluster.warehouse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

//class LocalDateTimeReadConverter implements Converter<String, LocalDateTime> {
//	@Override
//	public LocalDateTime convert(String date) {
//		String format = new SimpleDateFormat(DATE_TIME_FORMAT).format(date);
//		return LocalDateTime.parse(format);
//	}
//}
//
//class LocalDateTimeWriteConverter implements Converter<LocalDateTime, String> {
//	@Override
//	public String convert(LocalDateTime localDateTime) {
//		return new SimpleDateFormat(DATE_TIME_FORMAT).format(localDateTime);
//	}
//}


