package com.cluster.warehouse.web.rest;

import com.cluster.warehouse.BaseResourceIntTest;
import com.cluster.warehouse.WarehouseApplication;
import com.cluster.warehouse.domain.Summary;
import com.cluster.warehouse.repository.SummaryRepository;
import com.cluster.warehouse.service.SummaryService;
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
import static com.cluster.warehouse.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SummaryResource REST controller.
 *
 * @see SummaryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WarehouseApplication.class)
public class SummaryResourceIntTest extends BaseResourceIntTest {

    @Autowired
    private SummaryRepository summaryRepository;

    @Autowired
    private SummaryService summaryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restSummaryMockMvc;

    private Summary summary;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SummaryResource summaryResource = new SummaryResource(summaryService);
        this.restSummaryMockMvc = MockMvcBuilders.standaloneSetup(summaryResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setConversionService(createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Summary createEntity() {
        return new Summary()
                .source(DEFAULT_SOURCE)
                .duration(DEFAULT_DURATION)
                .total(DEFAULT_TOTAL)
                .valid(DEFAULT_VALID)
                .invalid(DEFAULT_INVALID)
				.duplicate(DEFAULT_DUPLICATE)
                .date(DEFAULT_DATE_TIME);
    }

    @Before
    public void initTest() {
        summaryRepository.deleteAll();
        summary = createEntity();
    }

    @Test
    public void getAllSummaries() throws Exception {
        // Initialize the database
        summaryRepository.save(summary);

        // Get all the summaryList
        restSummaryMockMvc.perform(get("/api/summaries?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(summary.getId())))
                .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
                .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
                .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())))
                .andExpect(jsonPath("$.[*].valid").value(hasItem(DEFAULT_VALID.intValue())))
                .andExpect(jsonPath("$.[*].invalid").value(hasItem(DEFAULT_INVALID.intValue())))
				.andExpect(jsonPath("$.[*].duplicate").value(hasItem(DEFAULT_DUPLICATE.intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE_TIME))));
    }

    @Test
    public void getSummary() throws Exception {
        // Initialize the database
        summaryRepository.save(summary);

        // Get the summary
        restSummaryMockMvc.perform(get("/api/summaries/{id}", summary.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(summary.getId()))
                .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
                .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
                .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()))
                .andExpect(jsonPath("$.valid").value(DEFAULT_VALID.intValue()))
                .andExpect(jsonPath("$.invalid").value(DEFAULT_INVALID.intValue()))
				.andExpect(jsonPath("$.duplicate").value(DEFAULT_DUPLICATE.intValue()))
                .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE_TIME)));
    }

    @Test
    public void getNonExistingSummary() throws Exception {
        // Get the summary
        restSummaryMockMvc.perform(get("/api/summaries/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Summary.class);
        Summary summary1 = new Summary();
        summary1.setId("id1");
        Summary summary2 = new Summary();
        summary2.setId(summary1.getId());
        assertThat(summary1).isEqualTo(summary2);
        summary2.setId("id2");
        assertThat(summary1).isNotEqualTo(summary2);
        summary1.setId(null);
        assertThat(summary1).isNotEqualTo(summary2);
    }
}
