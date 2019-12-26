package com.cluster.warehouse.service.impl;


import com.cluster.warehouse.BaseResourceIntTest;
import com.cluster.warehouse.WarehouseApplication;
import com.cluster.warehouse.domain.Deal;
import com.cluster.warehouse.domain.Report;
import com.cluster.warehouse.repository.DealRepository;
import com.cluster.warehouse.repository.ReportRepository;
import com.cluster.warehouse.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WarehouseApplication.class)
public class ReportServiceImplTest extends BaseResourceIntTest {

	@Autowired
	private ReportRepository reportRepository;
	@Autowired
	private DealRepository dealRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private ObjectMapper mapper;

	private ReportService reportService;

	@Rule
	public final ErrorCollector collector = new ErrorCollector();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		reportService = new ReportServiceImpl(reportRepository, mongoTemplate);
	}

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static Deal createEntity() {
		return new Deal()
				.id(DEFAULT_ID)
				.fromIsoCode(DEFAULT_FROM_ISO_CODE)
				.toIsoCode(DEFAULT_TO_ISO_CODE)
				.time(DEFAULT_TIME)
				.amount(DEFAULT_AMOUNT)
				.source(DEFAULT_SOURCE)
				.extension(DEFAULT_EXTENSION)
				.uploadedOn(DEFAULT_UPLOADED_ON);
	}

	@Test
	public void distinctFromIsoOrdering() {
		//Arrange
		reportRepository.deleteAll();
		final Deal deal = createEntity();
		dealRepository.save(deal);

		// Read upload reports.
		final List<Report> reportList = reportService.generateReport();

		// Validate the Report in the database
		Report testReport = reportList.get(reportList.size() - 1);
		assertThat(testReport.getFromIsoCode()).isEqualTo(DEFAULT_FROM_ISO_CODE);
		assertThat(testReport.getToIsoCode()).isEqualTo(DEFAULT_TO_ISO_CODE);
		assertThat(testReport.getTotal()).isEqualTo(DEFAULT_TOTAL);
	}

//	@Test
//	public void whenSerializingDateToISO8601_thenSerializedToText()
//			throws JsonProcessingException, ParseException {
//		SimpleDateFormat df = new SimpleDateFormat(DATETIME_FORMAT);
//
////		String toParse = "20-12-2014 02:3";
//		String toParse = "6783423000";
//		Date date = df.parse(toParse);
////		Date date = df.parse(toParse);
//
//		Deal event = new Deal().time(date).source("ext");
//
//		String result = mapper.writeValueAsString(event);
//		assertThat(result).contains(toParse);
//	}
}