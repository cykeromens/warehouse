//package com.cluster.warehouse.service.impl;
//
//
//
//import com.cluster.warehouse.WarehouseApplication;
//import com.cluster.warehouse.config.ApplicationProperties;
//import com.cluster.warehouse.domain.Deal;
//import com.cluster.warehouse.domain.Report;
//import com.cluster.warehouse.repository.DealRepository;
//import com.cluster.warehouse.service.DealService;
//import com.cluster.warehouse.service.pool.ForkJoinService;
//import com.cluster.warehouse.BaseResourceIntTest;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ErrorCollector;
//import org.junit.runner.RunWith;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = WarehouseApplication.class)
//public class DealServiceImplTest extends BaseResourceIntTest {
//
//	@Autowired
//	private ApplicationProperties props;
//	@Autowired
//	private ForkJoinService forkJoinService;
//	@Autowired
//	private DealRepository dealRepository;
//	@Autowired
//	private MongoTemplate mongoTemplate;
//
//	private DealService dealService;
//
//	@Rule
//	public final ErrorCollector collector = new ErrorCollector();
//
//	@Before
//	public void setUp() {
//		MockitoAnnotations.initMocks(this);
//		dealService = new DealServiceImpl(dealRepository,forkJoinService, props);
//	}
//
//	/**
//	 * Create an entity for this test.
//	 *
//	 * This is a static method, as tests for other entities might also need it,
//	 * if they test an entity which requires the current entity.
//	 */
//	public static Deal createEntity() {
//		Deal deal = new Deal()
//				.tagId(DEFAULT_TAG_ID)
//				.fromIsoCode(DEFAULT_FROM_ISO_CODE)
//				.toIsoCode(DEFAULT_TO_ISO_CODE)
//				.time(DEFAULT_TIME)
//				.amount(DEFAULT_AMOUNT)
//				.source(DEFAULT_SOURCE)
//				.fileType(DEFAULT_FILE_TYPE)
//				.uploadedOn(DEFAULT_UPLOADED_ON);
//		return deal;
//	}
//
//	@Test
//	public void distinctFromIsoOrdering() {
//		//Arrange
//		dealRepository.deleteAll();
//		final Deal deal = createEntity();
//		dealRepository.save(deal);
//
//		// Read upload reports.
//		final List<Report> reportList = dealService.getReports();
//
//		// Validate the Report in the database
//		Report testReport = reportList.get(reportList.size() - 1);
//		assertThat(testReport.getFromIsoCode()).isEqualTo(DEFAULT_FROM_ISO_CODE);
//		assertThat(testReport.getToIsoCode()).isEqualTo(DEFAULT_TO_ISO_CODE);
//		assertThat(testReport.getTotal()).isEqualTo(DEFAULT_TOTAL);
//	}
//}