package com.cluster.warehouse.web.rest;

import com.cluster.warehouse.WarehouseApplication;
import com.cluster.warehouse.domain.RecordCount;
import com.cluster.warehouse.repository.RecordCountRepository;
import com.cluster.warehouse.service.RecordCountService;
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

/**
 * Test class for the RecordCountResource REST controller.
 *
 * @see RecordCountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WarehouseApplication.class)
public class RecordCountResourceIntTest {

    private static final String DEFAULT_CURRENCY_ISO_CODE = "AAA";
    private static final String UPDATED_CURRENCY_ISO_CODE = "BBB";

    private static final Long DEFAULT_DEALS_COUNT = 1L;
    private static final Long UPDATED_DEALS_COUNT = 2L;

    @Autowired
    private RecordCountRepository recordCountRepository;

    @Autowired
    private RecordCountService recordCountService;

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

    private MockMvc restRecordCountMockMvc;

    private RecordCount recordCount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecordCountResource recordCountResource = new RecordCountResource(recordCountService);
        this.restRecordCountMockMvc = MockMvcBuilders.standaloneSetup(recordCountResource)
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
    public static RecordCount createEntity(EntityManager em) {
        RecordCount recordCount = new RecordCount()
                .currencyISOCode(DEFAULT_CURRENCY_ISO_CODE)
                .dealsCount(DEFAULT_DEALS_COUNT);
        return recordCount;
    }

    @Before
    public void initTest() {
        recordCount = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecordCount() throws Exception {
        int databaseSizeBeforeCreate = recordCountRepository.findAll().size();

        // Create the RecordCount
        restRecordCountMockMvc.perform(post("/api/record-counts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recordCount)))
                .andExpect(status().isCreated());

        // Validate the RecordCount in the database
        List<RecordCount> recordCountList = recordCountRepository.findAll();
        assertThat(recordCountList).hasSize(databaseSizeBeforeCreate + 1);
        RecordCount testRecordCount = recordCountList.get(recordCountList.size() - 1);
        assertThat(testRecordCount.getCurrencyISOCode()).isEqualTo(DEFAULT_CURRENCY_ISO_CODE);
        assertThat(testRecordCount.getDealsCount()).isEqualTo(DEFAULT_DEALS_COUNT);
    }

    @Test
    @Transactional
    public void createRecordCountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recordCountRepository.findAll().size();

        // Create the RecordCount with an existing ID
        recordCount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecordCountMockMvc.perform(post("/api/record-counts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recordCount)))
                .andExpect(status().isBadRequest());

        // Validate the RecordCount in the database
        List<RecordCount> recordCountList = recordCountRepository.findAll();
        assertThat(recordCountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDealsCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordCountRepository.findAll().size();
        // set the field null
        recordCount.setDealsCount(null);

        // Create the RecordCount, which fails.

        restRecordCountMockMvc.perform(post("/api/record-counts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recordCount)))
                .andExpect(status().isBadRequest());

        List<RecordCount> recordCountList = recordCountRepository.findAll();
        assertThat(recordCountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecordCounts() throws Exception {
        // Initialize the database
        recordCountRepository.saveAndFlush(recordCount);

        // Get all the recordCountList
        restRecordCountMockMvc.perform(get("/api/record-counts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(recordCount.getId().intValue())))
                .andExpect(jsonPath("$.[*].currencyISOCode").value(hasItem(DEFAULT_CURRENCY_ISO_CODE.toString())))
                .andExpect(jsonPath("$.[*].dealsCount").value(hasItem(DEFAULT_DEALS_COUNT.intValue())));
    }

    @Test
    @Transactional
    public void getRecordCount() throws Exception {
        // Initialize the database
        recordCountRepository.saveAndFlush(recordCount);

        // Get the recordCount
        restRecordCountMockMvc.perform(get("/api/record-counts/{id}", recordCount.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(recordCount.getId().intValue()))
                .andExpect(jsonPath("$.currencyISOCode").value(DEFAULT_CURRENCY_ISO_CODE.toString()))
                .andExpect(jsonPath("$.dealsCount").value(DEFAULT_DEALS_COUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRecordCount() throws Exception {
        // Get the recordCount
        restRecordCountMockMvc.perform(get("/api/record-counts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecordCount() throws Exception {
        // Initialize the database
        recordCountService.save(recordCount);

        int databaseSizeBeforeUpdate = recordCountRepository.findAll().size();

        // Update the recordCount
        RecordCount updatedRecordCount = recordCountRepository.findById(recordCount.getId()).get();
        // Disconnect from session so that the updates on updatedRecordCount are not directly saved in db
        em.detach(updatedRecordCount);
        updatedRecordCount
                .currencyISOCode(UPDATED_CURRENCY_ISO_CODE)
                .dealsCount(UPDATED_DEALS_COUNT);

        restRecordCountMockMvc.perform(put("/api/record-counts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRecordCount)))
                .andExpect(status().isOk());

        // Validate the RecordCount in the database
        List<RecordCount> recordCountList = recordCountRepository.findAll();
        assertThat(recordCountList).hasSize(databaseSizeBeforeUpdate);
        RecordCount testRecordCount = recordCountList.get(recordCountList.size() - 1);
        assertThat(testRecordCount.getCurrencyISOCode()).isEqualTo(UPDATED_CURRENCY_ISO_CODE);
        assertThat(testRecordCount.getDealsCount()).isEqualTo(UPDATED_DEALS_COUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingRecordCount() throws Exception {
        int databaseSizeBeforeUpdate = recordCountRepository.findAll().size();

        // Create the RecordCount

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecordCountMockMvc.perform(put("/api/record-counts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recordCount)))
                .andExpect(status().isBadRequest());

        // Validate the RecordCount in the database
        List<RecordCount> recordCountList = recordCountRepository.findAll();
        assertThat(recordCountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRecordCount() throws Exception {
        // Initialize the database
        recordCountService.save(recordCount);

        int databaseSizeBeforeDelete = recordCountRepository.findAll().size();

        // Delete the recordCount
        restRecordCountMockMvc.perform(delete("/api/record-counts/{id}", recordCount.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RecordCount> recordCountList = recordCountRepository.findAll();
        assertThat(recordCountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecordCount.class);
        RecordCount recordCount1 = new RecordCount();
        recordCount1.setId(1L);
        RecordCount recordCount2 = new RecordCount();
        recordCount2.setId(recordCount1.getId());
        assertThat(recordCount1).isEqualTo(recordCount2);
        recordCount2.setId(2L);
        assertThat(recordCount1).isNotEqualTo(recordCount2);
        recordCount1.setId(null);
        assertThat(recordCount1).isNotEqualTo(recordCount2);
    }
}
