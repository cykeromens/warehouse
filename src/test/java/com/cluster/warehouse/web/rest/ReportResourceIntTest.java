package com.cluster.warehouse.web.rest;

import com.cluster.warehouse.BaseResourceIntTest;
import com.cluster.warehouse.WarehouseApplication;
import com.cluster.warehouse.domain.Report;
import com.cluster.warehouse.repository.ReportRepository;
import com.cluster.warehouse.service.ReportService;
import com.cluster.warehouse.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import static com.cluster.warehouse.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReportResource REST controller.
 *
 * @see ReportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WarehouseApplication.class)
public class ReportResourceIntTest extends BaseResourceIntTest {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private ReportService reportService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private Validator validator;

	private MockMvc restReportMockMvc;

	private Report report;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final ReportResource reportResource = new ReportResource(reportService);
		this.restReportMockMvc = MockMvcBuilders.standaloneSetup(reportResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setConversionService(createFormattingConversionService())
				.setMessageConverters(jacksonMessageConverter)
				.setValidator(validator).build();
	}

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static Report createEntity() {
		Report report = new Report()
				.fromIsoCode(DEFAULT_FROM_ISO_CODE)
				.toIsoCode(DEFAULT_TO_ISO_CODE)
				.total(DEFAULT_TOTAL)
				.lastUpdated(DEFAULT_LAST_UPDATED);
		return report;
	}

	@Before
	public void initTest() {
		reportRepository.deleteAll();
		report = createEntity();
	}

	@Test
	public void getAllReports() throws Exception {
		// Initialize the database
		reportRepository.save(report);

		// Get all the reportList
		restReportMockMvc.perform(get("/api/reports?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(report.getId())))
				.andExpect(jsonPath("$.[*].fromIsoCode").value(hasItem(DEFAULT_FROM_ISO_CODE.toString())))
				.andExpect(jsonPath("$.[*].toIsoCode").value(hasItem(DEFAULT_TO_ISO_CODE.toString())))
				.andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())))
				.andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
	}

	@Test
	public void getReport() throws Exception {
		// Initialize the database
		reportRepository.save(report);

		// Get the report
		restReportMockMvc.perform(get("/api/reports/{id}", report.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.id").value(report.getId()))
				.andExpect(jsonPath("$.fromIsoCode").value(DEFAULT_FROM_ISO_CODE.toString()))
				.andExpect(jsonPath("$.toIsoCode").value(DEFAULT_TO_ISO_CODE.toString()))
				.andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()))
				.andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
	}

	@Test
	public void getNonExistingReport() throws Exception {
		// Get the report
		restReportMockMvc.perform(get("/api/reports/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Report.class);
		Report report1 = new Report();
		report1.setId("id1");
		Report report2 = new Report();
		report2.setId(report1.getId());
		assertThat(report1).isEqualTo(report2);
		report2.setId("id2");
		assertThat(report1).isNotEqualTo(report2);
		report1.setId(null);
		assertThat(report1).isNotEqualTo(report2);
	}
}
