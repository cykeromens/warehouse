package com.cluster.warehouse;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

public class BaseResourceIntTest {

	protected static final String DEFAULT_TAG_ID = "AAAAAAAAAA";
	protected static final String FILE_NAME = "./text.csv";
	protected static final String FILE_DETAIL = "tagId;fromIsoCode;toIsoCode;time;amount;\n" +
			"vvjtynbiadcacauyzoaui;USD;USD;2019-12-21T10:04:46.801Z;90561.0";
	protected static final String PARAM_NAME = "file";

	protected static final String DEFAULT_FROM_ISO_CODE = "AAA";
	protected static final String DEFAULT_TO_ISO_CODE = "BBB";

	protected static final Date DEFAULT_TIME = Date.from(Instant.ofEpochSecond(10));

	protected static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);

	protected static final String DEFAULT_SOURCE = "AAAAAAAAAA";

	protected static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";

	protected static final LocalDate DEFAULT_UPLOADED_ON = LocalDate.ofEpochDay(0L);

	protected static final Long DEFAULT_TOTAL = 1L;

	protected static final LocalDate DEFAULT_LAST_UPDATED = LocalDate.now();

	protected static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";

	protected static final Double DEFAULT_DURATION = 1D;

	protected static final Long DEFAULT_DUPLICATE = 1L;

	protected static final Long DEFAULT_VALID = 1L;

	protected static final Long DEFAULT_INVALID = 1L;

	protected static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);

}
