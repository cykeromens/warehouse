package com.cluster.warehouse;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class BaseResourceIntTest {

	protected static final String DEFAULT_TAG_ID = "AAAAAAAAAA";
	protected static final String FILE_NAME = "./text.csv";
	protected static final String FILE_DETAIL = "tagId;fromIsoCode;toIsoCode;time;amount;\n" +
			"vvjtynbiadcacauyzoaui;USD;USD;2019-12-21T10:04:46.801Z;90561.0";
	protected static final String PARAM_NAME = "file";

	protected static final String DEFAULT_FROM_ISO_CODE = "AAA";
	protected static final String DEFAULT_TO_ISO_CODE = "BBB";

	protected static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);

	protected static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);

	protected static final String DEFAULT_SOURCE = "AAAAAAAAAA";

	protected static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";

	protected static final LocalDate DEFAULT_UPLOADED_ON = LocalDate.ofEpochDay(0L);

	protected static final Long DEFAULT_TOTAL = 1L;

	protected static final LocalDate DEFAULT_LAST_UPDATED = LocalDate.now();
}
