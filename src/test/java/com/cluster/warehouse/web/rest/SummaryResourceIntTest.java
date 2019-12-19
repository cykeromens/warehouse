package com.cluster.warehouse.web.rest;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

;

/**
 * Test class for the SummaryResource REST controller.
 *
 * @see SummaryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WarehouseApplication.class)
public class SummaryResourceIntTest {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PROCESS_DURATION = 1_0D;
    private static final Double UPDATED_PROCESS_DURATION = 2_0D;

    private static final Integer DEFAULT_TOTAL_IMPORTED = 1;
    private static final Integer UPDATED_TOTAL_IMPORTED = 2;

    private static final Integer DEFAULT_TOTAL_VALID = 1;
    private static final Integer UPDATED_TOTAL_VALID = 2;

    private static final Integer DEFAULT_TOTAL_NOT_VALID = 1;
    private static final Integer UPDATED_TOTAL_NOT_VALID = 2;

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
    private EntityManager em;

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
                .setConversionService(TestUtil.createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Summary createEntity(EntityManager em) {
        Summary summary = new Summary()
                .fileName(DEFAULT_FILE_NAME)
                .processDuration(DEFAULT_PROCESS_DURATION)
                .totalImported(DEFAULT_TOTAL_IMPORTED)
                .totalValid(DEFAULT_TOTAL_VALID)
                .totalNotValid(DEFAULT_TOTAL_NOT_VALID);
        return summary;
    }

    @Before
    public void initTest() {
        summary = createEntity(em);
    }

    @Test
    @Transactional
    public void createSummary() throws Exception {
        int databaseSizeBeforeCreate = summaryRepository.findAll().size();

        // Create the Summary
        restSummaryMockMvc.perform(post("/api/summaries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(summary)))
                .andExpect(status().isCreated());

        // Validate the Summary in the database
        List<Summary> summaryList = summaryRepository.findAll();
        assertThat(summaryList).hasSize(databaseSizeBeforeCreate + 1);
        Summary testSummary = summaryList.get(summaryList.size() - 1);
        assertThat(testSummary.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testSummary.getProcessDuration()).isEqualTo(DEFAULT_PROCESS_DURATION);
        assertThat(testSummary.getTotalImported()).isEqualTo(DEFAULT_TOTAL_IMPORTED);
        assertThat(testSummary.getTotalValid()).isEqualTo(DEFAULT_TOTAL_VALID);
        assertThat(testSummary.getTotalNotValid()).isEqualTo(DEFAULT_TOTAL_NOT_VALID);
    }

    @Test
    @Transactional
    public void createSummaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = summaryRepository.findAll().size();

        // Create the Summary with an existing ID
        summary.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSummaryMockMvc.perform(post("/api/summaries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(summary)))
                .andExpect(status().isBadRequest());

        // Validate the Summary in the database
        List<Summary> summaryList = summaryRepository.findAll();
        assertThat(summaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = summaryRepository.findAll().size();
        // set the field null
        summary.setFileName(null);

        // Create the Summary, which fails.

        restSummaryMockMvc.perform(post("/api/summaries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(summary)))
                .andExpect(status().isBadRequest());

        List<Summary> summaryList = summaryRepository.findAll();
        assertThat(summaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProcessDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = summaryRepository.findAll().size();
        // set the field null
        summary.setProcessDuration(null);

        // Create the Summary, which fails.

        restSummaryMockMvc.perform(post("/api/summaries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(summary)))
                .andExpect(status().isBadRequest());

        List<Summary> summaryList = summaryRepository.findAll();
        assertThat(summaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalImportedIsRequired() throws Exception {
        int databaseSizeBeforeTest = summaryRepository.findAll().size();
        // set the field null
        summary.setTotalImported(null);

        // Create the Summary, which fails.

        restSummaryMockMvc.perform(post("/api/summaries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(summary)))
                .andExpect(status().isBadRequest());

        List<Summary> summaryList = summaryRepository.findAll();
        assertThat(summaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalValidIsRequired() throws Exception {
        int databaseSizeBeforeTest = summaryRepository.findAll().size();
        // set the field null
        summary.setTotalValid(null);

        // Create the Summary, which fails.

        restSummaryMockMvc.perform(post("/api/summaries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(summary)))
                .andExpect(status().isBadRequest());

        List<Summary> summaryList = summaryRepository.findAll();
        assertThat(summaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSummaries() throws Exception {
        // Initialize the database
        summaryRepository.saveAndFlush(summary);

        // Get all the summaryList
        restSummaryMockMvc.perform(get("/api/summaries?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(summary.getId().intValue())))
                .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
                .andExpect(jsonPath("$.[*].processDuration").value(hasItem(DEFAULT_PROCESS_DURATION)))
                .andExpect(jsonPath("$.[*].totalImported").value(hasItem(DEFAULT_TOTAL_IMPORTED)))
                .andExpect(jsonPath("$.[*].totalValid").value(hasItem(DEFAULT_TOTAL_VALID)))
                .andExpect(jsonPath("$.[*].totalNotValid").value(hasItem(DEFAULT_TOTAL_NOT_VALID)));
    }

    @Test
    @Transactional
    public void getSummary() throws Exception {
        // Initialize the database
        summaryRepository.saveAndFlush(summary);

        // Get the summary
        restSummaryMockMvc.perform(get("/api/summaries/{id}", summary.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(summary.getId().intValue()))
                .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
                .andExpect(jsonPath("$.processDuration").value(DEFAULT_PROCESS_DURATION))
                .andExpect(jsonPath("$.totalImported").value(DEFAULT_TOTAL_IMPORTED))
                .andExpect(jsonPath("$.totalValid").value(DEFAULT_TOTAL_VALID))
                .andExpect(jsonPath("$.totalNotValid").value(DEFAULT_TOTAL_NOT_VALID));
    }

    @Test
    @Transactional
    public void getNonExistingSummary() throws Exception {
        // Get the summary
        restSummaryMockMvc.perform(get("/api/summaries/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSummary() throws Exception {
        // Initialize the database
        summaryService.save(summary);

        int databaseSizeBeforeUpdate = summaryRepository.findAll().size();

        // Update the summary
        Summary updatedSummary = summaryRepository.findById(summary.getId()).get();
        // Disconnect from session so that the updates on updatedSummary are not directly saved in db
        em.detach(updatedSummary);
        updatedSummary
                .fileName(UPDATED_FILE_NAME)
                .processDuration(UPDATED_PROCESS_DURATION)
                .totalImported(UPDATED_TOTAL_IMPORTED)
                .totalValid(UPDATED_TOTAL_VALID)
                .totalNotValid(UPDATED_TOTAL_NOT_VALID);

        restSummaryMockMvc.perform(put("/api/summaries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSummary)))
                .andExpect(status().isOk());

        // Validate the Summary in the database
        List<Summary> summaryList = summaryRepository.findAll();
        assertThat(summaryList).hasSize(databaseSizeBeforeUpdate);
        Summary testSummary = summaryList.get(summaryList.size() - 1);
        assertThat(testSummary.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testSummary.getProcessDuration()).isEqualTo(UPDATED_PROCESS_DURATION);
        assertThat(testSummary.getTotalImported()).isEqualTo(UPDATED_TOTAL_IMPORTED);
        assertThat(testSummary.getTotalValid()).isEqualTo(UPDATED_TOTAL_VALID);
        assertThat(testSummary.getTotalNotValid()).isEqualTo(UPDATED_TOTAL_NOT_VALID);
    }

    @Test
    @Transactional
    public void updateNonExistingSummary() throws Exception {
        int databaseSizeBeforeUpdate = summaryRepository.findAll().size();

        // Create the Summary

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSummaryMockMvc.perform(put("/api/summaries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(summary)))
                .andExpect(status().isBadRequest());

        // Validate the Summary in the database
        List<Summary> summaryList = summaryRepository.findAll();
        assertThat(summaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSummary() throws Exception {
        // Initialize the database
        summaryService.save(summary);

        int databaseSizeBeforeDelete = summaryRepository.findAll().size();

        // Delete the summary
        restSummaryMockMvc.perform(delete("/api/summaries/{id}", summary.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Summary> summaryList = summaryRepository.findAll();
        assertThat(summaryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Summary.class);
        Summary summary1 = new Summary();
        summary1.setId(1L);
        Summary summary2 = new Summary();
        summary2.setId(summary1.getId());
        assertThat(summary1).isEqualTo(summary2);
        summary2.setId(2L);
        assertThat(summary1).isNotEqualTo(summary2);
        summary1.setId(null);
        assertThat(summary1).isNotEqualTo(summary2);
    }
}
