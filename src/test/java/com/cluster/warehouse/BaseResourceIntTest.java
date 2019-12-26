package com.cluster.warehouse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.cluster.warehouse.config.Constants.DATE_TIME_FORMAT;

public class BaseResourceIntTest {

	private static final String DATE = "25-12-2019 12:00:00";
	protected static final String DEFAULT_ID = "AAAAAAAAA";
	protected static final String DEFAULT_DEAL_ID = "AAA";
	protected static final String FILE_NAME = "./text.csv";
	protected static final String FILE_DETAIL = "tagId;fromIsoCode;toIsoCode;time;amount;\n" +
			"vvjtynbiadcacauyzoaui;USD;USD;2019-12-21T10:04:46.801Z;90561.0";
	protected static final String PARAM_NAME = "file";

	protected static final String DEFAULT_FROM_ISO_CODE = "AAA";
	protected static final String DEFAULT_TO_ISO_CODE = "BBB";

	protected static final LocalDateTime DEFAULT_TIME = LocalDateTime.now();
	protected static final String DEFAULT_STRING_TIME = "AAAAAAAAAA";

	protected static final Double DEFAULT_AMOUNT = 1.0;
	protected static final String DEFAULT_STRING_AMOUNT = "AAAAAAAAAA";

	protected static final String DEFAULT_SOURCE = "AAAAAAAAAA";

	protected static final String DEFAULT_EXTENSION = "AAAAAAAAAA";
	protected static final String DEFAULT_REASON = "AAAAAAAAAA";

	protected static final LocalDateTime DEFAULT_UPLOADED_ON = LocalDateTime.now();
	protected static final String DEFAULT_STRING_UPLOADED_ON = "AAAAAAAAAA";

	protected static final Long DEFAULT_TOTAL = 1L;

	protected static final LocalDate DEFAULT_LAST_UPDATED = LocalDate.ofEpochDay(0L);

	protected static final Double DEFAULT_DURATION = 1D;

	protected static final Long DEFAULT_DUPLICATE = 1L;

	protected static final Long DEFAULT_VALID = 1L;

	protected static final Long DEFAULT_INVALID = 1L;

	protected static final LocalDateTime DEFAULT_DATE_TIME = LocalDateTime.now();

	protected String localDateTime(LocalDateTime localDateTime) {
		return localDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
	}
}
