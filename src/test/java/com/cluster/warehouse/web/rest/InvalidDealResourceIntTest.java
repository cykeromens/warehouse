package com.cluster.warehouse.web.rest;

import com.cluster.warehouse.WarehouseApplication;
import com.cluster.warehouse.domain.InvalidDeal;
import com.cluster.warehouse.repository.InvalidDealRepository;
import com.cluster.warehouse.service.InvalidDealQueryService;
import com.cluster.warehouse.service.InvalidDealService;
import com.cluster.warehouse.web.rest.errors.ExceptionTranslator;
import org.hamcrest.Matchers;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InvalidDealResource REST controller.
 *
 * @see InvalidDealResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WarehouseApplication.class)
public class InvalidDealResourceIntTest {

    private static final String DEFAULT_TAG_ID = "AAAAAAAAAA";
    private static final String UPDATED_TAG_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FROM_ISO_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROM_ISO_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TO_ISO_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TO_ISO_CODE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    @Autowired
    private InvalidDealRepository invalidDealRepository;

    @Autowired
    private InvalidDealService invalidDealService;

    @Autowired
    private InvalidDealQueryService invalidDealQueryService;

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

    private MockMvc restInvalidDealMockMvc;

    private InvalidDeal invalidDeal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvalidDealResource invalidDealResource = new InvalidDealResource(invalidDealService, invalidDealQueryService);
        this.restInvalidDealMockMvc = MockMvcBuilders.standaloneSetup(invalidDealResource)
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
    public static InvalidDeal createEntity(EntityManager em) {
        InvalidDeal invalidDeal = new InvalidDeal()
                .tagId(DEFAULT_TAG_ID)
                .fromIsoCode(DEFAULT_FROM_ISO_CODE)
                .toIsoCode(DEFAULT_TO_ISO_CODE)
                .time(DEFAULT_TIME)
                .amount(DEFAULT_AMOUNT)
                .source(DEFAULT_SOURCE)
                .sourceFormat(DEFAULT_SOURCE_FORMAT)
                .reason(DEFAULT_REASON);
        return invalidDeal;
    }

    @Before
    public void initTest() {
        invalidDeal = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvalidDeal() throws Exception {
        int databaseSizeBeforeCreate = invalidDealRepository.findAll().size();

        // Create the InvalidDeal
        restInvalidDealMockMvc.perform(post("/api/invalid-deals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invalidDeal)))
                .andExpect(status().isCreated());

        // Validate the InvalidDeal in the database
        List<InvalidDeal> invalidDealList = invalidDealRepository.findAll();
        assertThat(invalidDealList).hasSize(databaseSizeBeforeCreate + 1);
        InvalidDeal testInvalidDeal = invalidDealList.get(invalidDealList.size() - 1);
        assertThat(testInvalidDeal.getTagId()).isEqualTo(DEFAULT_TAG_ID);
        assertThat(testInvalidDeal.getFromIsoCode()).isEqualTo(DEFAULT_FROM_ISO_CODE);
        assertThat(testInvalidDeal.getToIsoCode()).isEqualTo(DEFAULT_TO_ISO_CODE);
        assertThat(testInvalidDeal.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testInvalidDeal.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testInvalidDeal.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testInvalidDeal.getSourceFormat()).isEqualTo(DEFAULT_SOURCE_FORMAT);
        assertThat(testInvalidDeal.getReason()).isEqualTo(DEFAULT_REASON);
    }

    @Test
    @Transactional
    public void createInvalidDealWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invalidDealRepository.findAll().size();

        // Create the InvalidDeal with an existing ID
        invalidDeal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvalidDealMockMvc.perform(post("/api/invalid-deals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invalidDeal)))
                .andExpect(status().isBadRequest());

        // Validate the InvalidDeal in the database
        List<InvalidDeal> invalidDealList = invalidDealRepository.findAll();
        assertThat(invalidDealList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInvalidDeals() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList
        restInvalidDealMockMvc.perform(get("/api/invalid-deals?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(invalidDeal.getId().intValue())))
                .andExpect(jsonPath("$.[*].tagId").value(hasItem(DEFAULT_TAG_ID)))
                .andExpect(jsonPath("$.[*].fromIsoCode").value(hasItem(DEFAULT_FROM_ISO_CODE.toString())))
                .andExpect(jsonPath("$.[*].toIsoCode").value(hasItem(DEFAULT_TO_ISO_CODE.toString())))
                .andExpect(jsonPath("$.[*].time").value(Matchers.hasItem(TestUtil.sameInstant(DEFAULT_TIME))))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
                .andExpect(jsonPath("$.[*].sourceFormat").value(hasItem(DEFAULT_SOURCE_FORMAT.toString())))
                .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())));
    }

    @Test
    @Transactional
    public void getInvalidDeal() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get the invalidDeal
        restInvalidDealMockMvc.perform(get("/api/invalid-deals/{id}", invalidDeal.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(invalidDeal.getId().intValue()))
                .andExpect(jsonPath("$.tagId").value(DEFAULT_TAG_ID))
                .andExpect(jsonPath("$.fromIsoCode").value(DEFAULT_FROM_ISO_CODE.toString()))
                .andExpect(jsonPath("$.toIsoCode").value(DEFAULT_TO_ISO_CODE.toString()))
                .andExpect(jsonPath("$.time").value(TestUtil.sameInstant(DEFAULT_TIME)))
                .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
                .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()))
                .andExpect(jsonPath("$.sourceFormat").value(DEFAULT_SOURCE_FORMAT.toString()))
                .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()));
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByTagIdIsEqualToSomething() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where tagId equals to DEFAULT_TAG_ID
        defaultInvalidDealShouldBeFound("tagId.equals=" + DEFAULT_TAG_ID);

        // Get all the invalidDealList where tagId equals to UPDATED_TAG_ID
        defaultInvalidDealShouldNotBeFound("tagId.equals=" + UPDATED_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByTagIdIsInShouldWork() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where tagId in DEFAULT_TAG_ID or UPDATED_TAG_ID
        defaultInvalidDealShouldBeFound("tagId.in=" + DEFAULT_TAG_ID + "," + UPDATED_TAG_ID);

        // Get all the invalidDealList where tagId equals to UPDATED_TAG_ID
        defaultInvalidDealShouldNotBeFound("tagId.in=" + UPDATED_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByTagIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where tagId is not null
        defaultInvalidDealShouldBeFound("tagId.specified=true");

        // Get all the invalidDealList where tagId is null
        defaultInvalidDealShouldNotBeFound("tagId.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByTagIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where tagId greater than or equals to DEFAULT_TAG_ID
        defaultInvalidDealShouldBeFound("tagId.greaterOrEqualThan=" + DEFAULT_TAG_ID);

        // Get all the invalidDealList where tagId greater than or equals to UPDATED_TAG_ID
        defaultInvalidDealShouldNotBeFound("tagId.greaterOrEqualThan=" + UPDATED_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByTagIdIsLessThanSomething() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where tagId less than or equals to DEFAULT_TAG_ID
        defaultInvalidDealShouldNotBeFound("tagId.lessThan=" + DEFAULT_TAG_ID);

        // Get all the invalidDealList where tagId less than or equals to UPDATED_TAG_ID
        defaultInvalidDealShouldBeFound("tagId.lessThan=" + UPDATED_TAG_ID);
    }


    @Test
    @Transactional
    public void getAllInvalidDealsByFromIsoCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where fromIsoCode equals to DEFAULT_FROM_ISO_CODE
        defaultInvalidDealShouldBeFound("fromIsoCode.equals=" + DEFAULT_FROM_ISO_CODE);

        // Get all the invalidDealList where fromIsoCode equals to UPDATED_FROM_ISO_CODE
        defaultInvalidDealShouldNotBeFound("fromIsoCode.equals=" + UPDATED_FROM_ISO_CODE);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByFromIsoCodeIsInShouldWork() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where fromIsoCode in DEFAULT_FROM_ISO_CODE or UPDATED_FROM_ISO_CODE
        defaultInvalidDealShouldBeFound("fromIsoCode.in=" + DEFAULT_FROM_ISO_CODE + "," + UPDATED_FROM_ISO_CODE);

        // Get all the invalidDealList where fromIsoCode equals to UPDATED_FROM_ISO_CODE
        defaultInvalidDealShouldNotBeFound("fromIsoCode.in=" + UPDATED_FROM_ISO_CODE);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByFromIsoCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where fromIsoCode is not null
        defaultInvalidDealShouldBeFound("fromIsoCode.specified=true");

        // Get all the invalidDealList where fromIsoCode is null
        defaultInvalidDealShouldNotBeFound("fromIsoCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByToIsoCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where toIsoCode equals to DEFAULT_TO_ISO_CODE
        defaultInvalidDealShouldBeFound("toIsoCode.equals=" + DEFAULT_TO_ISO_CODE);

        // Get all the invalidDealList where toIsoCode equals to UPDATED_TO_ISO_CODE
        defaultInvalidDealShouldNotBeFound("toIsoCode.equals=" + UPDATED_TO_ISO_CODE);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByToIsoCodeIsInShouldWork() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where toIsoCode in DEFAULT_TO_ISO_CODE or UPDATED_TO_ISO_CODE
        defaultInvalidDealShouldBeFound("toIsoCode.in=" + DEFAULT_TO_ISO_CODE + "," + UPDATED_TO_ISO_CODE);

        // Get all the invalidDealList where toIsoCode equals to UPDATED_TO_ISO_CODE
        defaultInvalidDealShouldNotBeFound("toIsoCode.in=" + UPDATED_TO_ISO_CODE);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByToIsoCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where toIsoCode is not null
        defaultInvalidDealShouldBeFound("toIsoCode.specified=true");

        // Get all the invalidDealList where toIsoCode is null
        defaultInvalidDealShouldNotBeFound("toIsoCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where time equals to DEFAULT_TIME
        defaultInvalidDealShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the invalidDealList where time equals to UPDATED_TIME
        defaultInvalidDealShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where time in DEFAULT_TIME or UPDATED_TIME
        defaultInvalidDealShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the invalidDealList where time equals to UPDATED_TIME
        defaultInvalidDealShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where time is not null
        defaultInvalidDealShouldBeFound("time.specified=true");

        // Get all the invalidDealList where time is null
        defaultInvalidDealShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where time greater than or equals to DEFAULT_TIME
        defaultInvalidDealShouldBeFound("time.greaterOrEqualThan=" + DEFAULT_TIME);

        // Get all the invalidDealList where time greater than or equals to UPDATED_TIME
        defaultInvalidDealShouldNotBeFound("time.greaterOrEqualThan=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where time less than or equals to DEFAULT_TIME
        defaultInvalidDealShouldNotBeFound("time.lessThan=" + DEFAULT_TIME);

        // Get all the invalidDealList where time less than or equals to UPDATED_TIME
        defaultInvalidDealShouldBeFound("time.lessThan=" + UPDATED_TIME);
    }


    @Test
    @Transactional
    public void getAllInvalidDealsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where amount equals to DEFAULT_AMOUNT
        defaultInvalidDealShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the invalidDealList where amount equals to UPDATED_AMOUNT
        defaultInvalidDealShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultInvalidDealShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the invalidDealList where amount equals to UPDATED_AMOUNT
        defaultInvalidDealShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where amount is not null
        defaultInvalidDealShouldBeFound("amount.specified=true");

        // Get all the invalidDealList where amount is null
        defaultInvalidDealShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvalidDealsBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where source equals to DEFAULT_SOURCE
        defaultInvalidDealShouldBeFound("source.equals=" + DEFAULT_SOURCE);

        // Get all the invalidDealList where source equals to UPDATED_SOURCE
        defaultInvalidDealShouldNotBeFound("source.equals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsBySourceIsInShouldWork() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where source in DEFAULT_SOURCE or UPDATED_SOURCE
        defaultInvalidDealShouldBeFound("source.in=" + DEFAULT_SOURCE + "," + UPDATED_SOURCE);

        // Get all the invalidDealList where source equals to UPDATED_SOURCE
        defaultInvalidDealShouldNotBeFound("source.in=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsBySourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where source is not null
        defaultInvalidDealShouldBeFound("source.specified=true");

        // Get all the invalidDealList where source is null
        defaultInvalidDealShouldNotBeFound("source.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvalidDealsBySourceFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where sourceFormat equals to DEFAULT_SOURCE_FORMAT
        defaultInvalidDealShouldBeFound("sourceFormat.equals=" + DEFAULT_SOURCE_FORMAT);

        // Get all the invalidDealList where sourceFormat equals to UPDATED_SOURCE_FORMAT
        defaultInvalidDealShouldNotBeFound("sourceFormat.equals=" + UPDATED_SOURCE_FORMAT);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsBySourceFormatIsInShouldWork() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where sourceFormat in DEFAULT_SOURCE_FORMAT or UPDATED_SOURCE_FORMAT
        defaultInvalidDealShouldBeFound("sourceFormat.in=" + DEFAULT_SOURCE_FORMAT + "," + UPDATED_SOURCE_FORMAT);

        // Get all the invalidDealList where sourceFormat equals to UPDATED_SOURCE_FORMAT
        defaultInvalidDealShouldNotBeFound("sourceFormat.in=" + UPDATED_SOURCE_FORMAT);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsBySourceFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where sourceFormat is not null
        defaultInvalidDealShouldBeFound("sourceFormat.specified=true");

        // Get all the invalidDealList where sourceFormat is null
        defaultInvalidDealShouldNotBeFound("sourceFormat.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where reason equals to DEFAULT_REASON
        defaultInvalidDealShouldBeFound("reason.equals=" + DEFAULT_REASON);

        // Get all the invalidDealList where reason equals to UPDATED_REASON
        defaultInvalidDealShouldNotBeFound("reason.equals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByReasonIsInShouldWork() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where reason in DEFAULT_REASON or UPDATED_REASON
        defaultInvalidDealShouldBeFound("reason.in=" + DEFAULT_REASON + "," + UPDATED_REASON);

        // Get all the invalidDealList where reason equals to UPDATED_REASON
        defaultInvalidDealShouldNotBeFound("reason.in=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    public void getAllInvalidDealsByReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        invalidDealRepository.saveAndFlush(invalidDeal);

        // Get all the invalidDealList where reason is not null
        defaultInvalidDealShouldBeFound("reason.specified=true");

        // Get all the invalidDealList where reason is null
        defaultInvalidDealShouldNotBeFound("reason.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInvalidDealShouldBeFound(String filter) throws Exception {
        restInvalidDealMockMvc.perform(get("/api/invalid-deals?sort=id,desc&" + filter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(invalidDeal.getId().intValue())))
                .andExpect(jsonPath("$.[*].tagId").value(hasItem(DEFAULT_TAG_ID)))
                .andExpect(jsonPath("$.[*].fromIsoCode").value(hasItem(DEFAULT_FROM_ISO_CODE)))
                .andExpect(jsonPath("$.[*].toIsoCode").value(hasItem(DEFAULT_TO_ISO_CODE)))
                .andExpect(jsonPath("$.[*].time").value(Matchers.hasItem(TestUtil.sameInstant(DEFAULT_TIME))))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
                .andExpect(jsonPath("$.[*].sourceFormat").value(hasItem(DEFAULT_SOURCE_FORMAT)))
                .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));

        // Check, that the count call also returns 1
        restInvalidDealMockMvc.perform(get("/api/invalid-deals/count?sort=id,desc&" + filter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInvalidDealShouldNotBeFound(String filter) throws Exception {
        restInvalidDealMockMvc.perform(get("/api/invalid-deals?sort=id,desc&" + filter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvalidDealMockMvc.perform(get("/api/invalid-deals/count?sort=id,desc&" + filter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingInvalidDeal() throws Exception {
        // Get the invalidDeal
        restInvalidDealMockMvc.perform(get("/api/invalid-deals/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvalidDeal() throws Exception {
        // Initialize the database
        invalidDealService.save(invalidDeal);

        int databaseSizeBeforeUpdate = invalidDealRepository.findAll().size();

        // Update the invalidDeal
        InvalidDeal updatedInvalidDeal = invalidDealRepository.findById(invalidDeal.getId()).get();
        // Disconnect from session so that the updates on updatedInvalidDeal are not directly saved in db
        em.detach(updatedInvalidDeal);
        updatedInvalidDeal
                .tagId(UPDATED_TAG_ID)
                .fromIsoCode(UPDATED_FROM_ISO_CODE)
                .toIsoCode(UPDATED_TO_ISO_CODE)
                .time(UPDATED_TIME)
                .amount(UPDATED_AMOUNT)
                .source(UPDATED_SOURCE)
                .sourceFormat(UPDATED_SOURCE_FORMAT)
                .reason(UPDATED_REASON);

        restInvalidDealMockMvc.perform(put("/api/invalid-deals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInvalidDeal)))
                .andExpect(status().isOk());

        // Validate the InvalidDeal in the database
        List<InvalidDeal> invalidDealList = invalidDealRepository.findAll();
        assertThat(invalidDealList).hasSize(databaseSizeBeforeUpdate);
        InvalidDeal testInvalidDeal = invalidDealList.get(invalidDealList.size() - 1);
        assertThat(testInvalidDeal.getTagId()).isEqualTo(UPDATED_TAG_ID);
        assertThat(testInvalidDeal.getFromIsoCode()).isEqualTo(UPDATED_FROM_ISO_CODE);
        assertThat(testInvalidDeal.getToIsoCode()).isEqualTo(UPDATED_TO_ISO_CODE);
        assertThat(testInvalidDeal.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testInvalidDeal.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testInvalidDeal.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testInvalidDeal.getSourceFormat()).isEqualTo(UPDATED_SOURCE_FORMAT);
        assertThat(testInvalidDeal.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    public void updateNonExistingInvalidDeal() throws Exception {
        int databaseSizeBeforeUpdate = invalidDealRepository.findAll().size();

        // Create the InvalidDeal

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvalidDealMockMvc.perform(put("/api/invalid-deals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invalidDeal)))
                .andExpect(status().isBadRequest());

        // Validate the InvalidDeal in the database
        List<InvalidDeal> invalidDealList = invalidDealRepository.findAll();
        assertThat(invalidDealList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvalidDeal() throws Exception {
        // Initialize the database
        invalidDealService.save(invalidDeal);

        int databaseSizeBeforeDelete = invalidDealRepository.findAll().size();

        // Delete the invalidDeal
        restInvalidDealMockMvc.perform(delete("/api/invalid-deals/{id}", invalidDeal.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<InvalidDeal> invalidDealList = invalidDealRepository.findAll();
        assertThat(invalidDealList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvalidDeal.class);
        InvalidDeal invalidDeal1 = new InvalidDeal();
        invalidDeal1.setId(1L);
        InvalidDeal invalidDeal2 = new InvalidDeal();
        invalidDeal2.setId(invalidDeal1.getId());
        assertThat(invalidDeal1).isEqualTo(invalidDeal2);
        invalidDeal2.setId(2L);
        assertThat(invalidDeal1).isNotEqualTo(invalidDeal2);
        invalidDeal1.setId(null);
        assertThat(invalidDeal1).isNotEqualTo(invalidDeal2);
    }
}
