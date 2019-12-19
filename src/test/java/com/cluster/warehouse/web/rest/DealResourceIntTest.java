package com.cluster.warehouse.web.rest;


import com.cluster.warehouse.WarehouseApplication;
import com.cluster.warehouse.domain.Deal;
import com.cluster.warehouse.repository.DealRepository;
import com.cluster.warehouse.service.DealQueryService;
import com.cluster.warehouse.service.DealService;
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
 * Test class for the DealResource REST controller.
 *
 * @see DealResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WarehouseApplication.class)
public class DealResourceIntTest {

    private static final String DEFAULT_FROM_ISO_CODE = "AAA";
    private static final String UPDATED_FROM_ISO_CODE = "BBB";

    private static final String DEFAULT_TO_ISO_CODE = "AAA";
    private static final String UPDATED_TO_ISO_CODE = "BBB";

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";


    private static final String DEFAULT_TO_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_TO_COUNTRY = "BBBBBBBBBB";


    private static final String DEFAULT_TAG_ID = "AAAAAAAAAA";
    private static final String UPDATED_TAG_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FROM_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_FROM_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_FORMAT = "BBBBBBBBBB";

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private DealService dealService;

    @Autowired
    private DealQueryService dealQueryService;

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

    private MockMvc restDealMockMvc;

    private Deal deal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DealResource dealResource = new DealResource(dealService, dealQueryService);
        this.restDealMockMvc = MockMvcBuilders.standaloneSetup(dealResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
                .setConversionService(TestUtil.createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deal createEntity(EntityManager em) {
        Deal deal = new Deal()
            .tagId(DEFAULT_TAG_ID)
            .fromIsoCode(DEFAULT_FROM_ISO_CODE)
            .toIsoCode(DEFAULT_TO_ISO_CODE)
            .time(DEFAULT_TIME)
            .amount(DEFAULT_AMOUNT)
            .source(DEFAULT_SOURCE)
            .fromCountry(DEFAULT_FROM_COUNTRY)
            .toCountry(DEFAULT_TO_COUNTRY)
            .sourceFormat(DEFAULT_SOURCE_FORMAT);
        return deal;
    }

    @Before
    public void initTest() {
        deal = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeal() throws Exception {
        int databaseSizeBeforeCreate = dealRepository.findAll().size();

        // Create the Deal
        restDealMockMvc.perform(post("/api/deals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deal)))
            .andExpect(status().isCreated());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeCreate + 1);
        Deal testDeal = dealList.get(dealList.size() - 1);
        assertThat(testDeal.getTagId()).isEqualTo(DEFAULT_TAG_ID);
        assertThat(testDeal.getFromIsoCode()).isEqualTo(DEFAULT_FROM_ISO_CODE);
        assertThat(testDeal.getToIsoCode()).isEqualTo(DEFAULT_TO_ISO_CODE);
        assertThat(testDeal.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testDeal.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testDeal.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testDeal.getSourceFormat()).isEqualTo(DEFAULT_SOURCE_FORMAT);
    }

    @Test
    @Transactional
    public void createDealWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dealRepository.findAll().size();

        // Create the Deal with an existing ID
        deal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDealMockMvc.perform(post("/api/deals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deal)))
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTagIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = dealRepository.findAll().size();
        // set the field null
        deal.setTagId(null);

        // Create the Deal, which fails.

        restDealMockMvc.perform(post("/api/deals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deal)))
            .andExpect(status().isBadRequest());

        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFromIsoCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dealRepository.findAll().size();
        // set the field null
        deal.setFromIsoCode(null);

        // Create the Deal, which fails.

        restDealMockMvc.perform(post("/api/deals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deal)))
            .andExpect(status().isBadRequest());

        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkToIsoCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dealRepository.findAll().size();
        // set the field null
        deal.setToIsoCode(null);

        // Create the Deal, which fails.

        restDealMockMvc.perform(post("/api/deals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deal)))
            .andExpect(status().isBadRequest());

        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dealRepository.findAll().size();
        // set the field null
        deal.setTime(null);

        // Create the Deal, which fails.

        restDealMockMvc.perform(post("/api/deals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deal)))
            .andExpect(status().isBadRequest());

        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = dealRepository.findAll().size();
        // set the field null
        deal.setAmount(null);

        // Create the Deal, which fails.

        restDealMockMvc.perform(post("/api/deals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deal)))
            .andExpect(status().isBadRequest());

        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSourceIsRequired() throws Exception {
        int databaseSizeBeforeTest = dealRepository.findAll().size();
        // set the field null
        deal.setSource(null);

        // Create the Deal, which fails.

        restDealMockMvc.perform(post("/api/deals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deal)))
            .andExpect(status().isBadRequest());

        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSourceFormatIsRequired() throws Exception {
        int databaseSizeBeforeTest = dealRepository.findAll().size();
        // set the field null
        deal.setSourceFormat(null);

        // Create the Deal, which fails.

        restDealMockMvc.perform(post("/api/deals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deal)))
            .andExpect(status().isBadRequest());

        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDeals() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList
        restDealMockMvc.perform(get("/api/deals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deal.getId().intValue())))
            .andExpect(jsonPath("$.[*].tagId").value(hasItem(DEFAULT_TAG_ID)))
            .andExpect(jsonPath("$.[*].fromIsoCode").value(hasItem(DEFAULT_FROM_ISO_CODE.toString())))
            .andExpect(jsonPath("$.[*].toIsoCode").value(hasItem(DEFAULT_TO_ISO_CODE.toString())))
                .andExpect(jsonPath("$.[*].time").value(Matchers.hasItem(TestUtil.sameInstant(DEFAULT_TIME))))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].sourceFormat").value(hasItem(DEFAULT_SOURCE_FORMAT.toString())));
    }
    
    @Test
    @Transactional
    public void getDeal() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get the deal
        restDealMockMvc.perform(get("/api/deals/{id}", deal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deal.getId().intValue()))
            .andExpect(jsonPath("$.tagId").value(DEFAULT_TAG_ID))
            .andExpect(jsonPath("$.fromIsoCode").value(DEFAULT_FROM_ISO_CODE.toString()))
            .andExpect(jsonPath("$.toIsoCode").value(DEFAULT_TO_ISO_CODE.toString()))
                .andExpect(jsonPath("$.time").value(TestUtil.sameInstant(DEFAULT_TIME)))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()))
            .andExpect(jsonPath("$.sourceFormat").value(DEFAULT_SOURCE_FORMAT.toString()));
    }

    @Test
    @Transactional
    public void getAllDealsByTagIdIsEqualToSomething() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where tagId equals to DEFAULT_TAG_ID
        defaultDealShouldBeFound("tagId.equals=" + DEFAULT_TAG_ID);

        // Get all the dealList where tagId equals to UPDATED_TAG_ID
        defaultDealShouldNotBeFound("tagId.equals=" + UPDATED_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllDealsByTagIdIsInShouldWork() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where tagId in DEFAULT_TAG_ID or UPDATED_TAG_ID
        defaultDealShouldBeFound("tagId.in=" + DEFAULT_TAG_ID + "," + UPDATED_TAG_ID);

        // Get all the dealList where tagId equals to UPDATED_TAG_ID
        defaultDealShouldNotBeFound("tagId.in=" + UPDATED_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllDealsByTagIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where tagId is not null
        defaultDealShouldBeFound("tagId.specified=true");

        // Get all the dealList where tagId is null
        defaultDealShouldNotBeFound("tagId.specified=false");
    }

    @Test
    @Transactional
    public void getAllDealsByTagIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where tagId greater than or equals to DEFAULT_TAG_ID
        defaultDealShouldBeFound("tagId.greaterOrEqualThan=" + DEFAULT_TAG_ID);

        // Get all the dealList where tagId greater than or equals to UPDATED_TAG_ID
        defaultDealShouldNotBeFound("tagId.greaterOrEqualThan=" + UPDATED_TAG_ID);
    }

    @Test
    @Transactional
    public void getAllDealsByTagIdIsLessThanSomething() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where tagId less than or equals to DEFAULT_TAG_ID
        defaultDealShouldNotBeFound("tagId.lessThan=" + DEFAULT_TAG_ID);

        // Get all the dealList where tagId less than or equals to UPDATED_TAG_ID
        defaultDealShouldBeFound("tagId.lessThan=" + UPDATED_TAG_ID);
    }


    @Test
    @Transactional
    public void getAllDealsByFromIsoCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where fromIsoCode equals to DEFAULT_FROM_ISO_CODE
        defaultDealShouldBeFound("fromIsoCode.equals=" + DEFAULT_FROM_ISO_CODE);

        // Get all the dealList where fromIsoCode equals to UPDATED_FROM_ISO_CODE
        defaultDealShouldNotBeFound("fromIsoCode.equals=" + UPDATED_FROM_ISO_CODE);
    }

    @Test
    @Transactional
    public void getAllDealsByFromIsoCodeIsInShouldWork() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where fromIsoCode in DEFAULT_FROM_ISO_CODE or UPDATED_FROM_ISO_CODE
        defaultDealShouldBeFound("fromIsoCode.in=" + DEFAULT_FROM_ISO_CODE + "," + UPDATED_FROM_ISO_CODE);

        // Get all the dealList where fromIsoCode equals to UPDATED_FROM_ISO_CODE
        defaultDealShouldNotBeFound("fromIsoCode.in=" + UPDATED_FROM_ISO_CODE);
    }

    @Test
    @Transactional
    public void getAllDealsByFromIsoCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where fromIsoCode is not null
        defaultDealShouldBeFound("fromIsoCode.specified=true");

        // Get all the dealList where fromIsoCode is null
        defaultDealShouldNotBeFound("fromIsoCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllDealsByToIsoCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where toIsoCode equals to DEFAULT_TO_ISO_CODE
        defaultDealShouldBeFound("toIsoCode.equals=" + DEFAULT_TO_ISO_CODE);

        // Get all the dealList where toIsoCode equals to UPDATED_TO_ISO_CODE
        defaultDealShouldNotBeFound("toIsoCode.equals=" + UPDATED_TO_ISO_CODE);
    }

    @Test
    @Transactional
    public void getAllDealsByToIsoCodeIsInShouldWork() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where toIsoCode in DEFAULT_TO_ISO_CODE or UPDATED_TO_ISO_CODE
        defaultDealShouldBeFound("toIsoCode.in=" + DEFAULT_TO_ISO_CODE + "," + UPDATED_TO_ISO_CODE);

        // Get all the dealList where toIsoCode equals to UPDATED_TO_ISO_CODE
        defaultDealShouldNotBeFound("toIsoCode.in=" + UPDATED_TO_ISO_CODE);
    }

    @Test
    @Transactional
    public void getAllDealsByToIsoCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where toIsoCode is not null
        defaultDealShouldBeFound("toIsoCode.specified=true");

        // Get all the dealList where toIsoCode is null
        defaultDealShouldNotBeFound("toIsoCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllDealsByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where time equals to DEFAULT_TIME
        defaultDealShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the dealList where time equals to UPDATED_TIME
        defaultDealShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllDealsByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where time in DEFAULT_TIME or UPDATED_TIME
        defaultDealShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the dealList where time equals to UPDATED_TIME
        defaultDealShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllDealsByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where time is not null
        defaultDealShouldBeFound("time.specified=true");

        // Get all the dealList where time is null
        defaultDealShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    public void getAllDealsByTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where time greater than or equals to DEFAULT_TIME
        defaultDealShouldBeFound("time.greaterOrEqualThan=" + DEFAULT_TIME);

        // Get all the dealList where time greater than or equals to UPDATED_TIME
        defaultDealShouldNotBeFound("time.greaterOrEqualThan=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllDealsByTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where time less than or equals to DEFAULT_TIME
        defaultDealShouldNotBeFound("time.lessThan=" + DEFAULT_TIME);

        // Get all the dealList where time less than or equals to UPDATED_TIME
        defaultDealShouldBeFound("time.lessThan=" + UPDATED_TIME);
    }


    @Test
    @Transactional
    public void getAllDealsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where amount equals to DEFAULT_AMOUNT
        defaultDealShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the dealList where amount equals to UPDATED_AMOUNT
        defaultDealShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDealsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultDealShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the dealList where amount equals to UPDATED_AMOUNT
        defaultDealShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDealsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where amount is not null
        defaultDealShouldBeFound("amount.specified=true");

        // Get all the dealList where amount is null
        defaultDealShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllDealsBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where source equals to DEFAULT_SOURCE
        defaultDealShouldBeFound("source.equals=" + DEFAULT_SOURCE);

        // Get all the dealList where source equals to UPDATED_SOURCE
        defaultDealShouldNotBeFound("source.equals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    public void getAllDealsBySourceIsInShouldWork() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where source in DEFAULT_SOURCE or UPDATED_SOURCE
        defaultDealShouldBeFound("source.in=" + DEFAULT_SOURCE + "," + UPDATED_SOURCE);

        // Get all the dealList where source equals to UPDATED_SOURCE
        defaultDealShouldNotBeFound("source.in=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    public void getAllDealsBySourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where source is not null
        defaultDealShouldBeFound("source.specified=true");

        // Get all the dealList where source is null
        defaultDealShouldNotBeFound("source.specified=false");
    }

    @Test
    @Transactional
    public void getAllDealsBySourceFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where sourceFormat equals to DEFAULT_SOURCE_FORMAT
        defaultDealShouldBeFound("sourceFormat.equals=" + DEFAULT_SOURCE_FORMAT);

        // Get all the dealList where sourceFormat equals to UPDATED_SOURCE_FORMAT
        defaultDealShouldNotBeFound("sourceFormat.equals=" + UPDATED_SOURCE_FORMAT);
    }

    @Test
    @Transactional
    public void getAllDealsBySourceFormatIsInShouldWork() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where sourceFormat in DEFAULT_SOURCE_FORMAT or UPDATED_SOURCE_FORMAT
        defaultDealShouldBeFound("sourceFormat.in=" + DEFAULT_SOURCE_FORMAT + "," + UPDATED_SOURCE_FORMAT);

        // Get all the dealList where sourceFormat equals to UPDATED_SOURCE_FORMAT
        defaultDealShouldNotBeFound("sourceFormat.in=" + UPDATED_SOURCE_FORMAT);
    }

    @Test
    @Transactional
    public void getAllDealsBySourceFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList where sourceFormat is not null
        defaultDealShouldBeFound("sourceFormat.specified=true");

        // Get all the dealList where sourceFormat is null
        defaultDealShouldNotBeFound("sourceFormat.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDealShouldBeFound(String filter) throws Exception {
        restDealMockMvc.perform(get("/api/deals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deal.getId().intValue())))
            .andExpect(jsonPath("$.[*].tagId").value(hasItem(DEFAULT_TAG_ID)))
            .andExpect(jsonPath("$.[*].fromIsoCode").value(hasItem(DEFAULT_FROM_ISO_CODE)))
            .andExpect(jsonPath("$.[*].toIsoCode").value(hasItem(DEFAULT_TO_ISO_CODE)))
                .andExpect(jsonPath("$.[*].time").value(Matchers.hasItem(TestUtil.sameInstant(DEFAULT_TIME))))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].sourceFormat").value(hasItem(DEFAULT_SOURCE_FORMAT)));

        // Check, that the count call also returns 1
        restDealMockMvc.perform(get("/api/deals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDealShouldNotBeFound(String filter) throws Exception {
        restDealMockMvc.perform(get("/api/deals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDealMockMvc.perform(get("/api/deals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDeal() throws Exception {
        // Get the deal
        restDealMockMvc.perform(get("/api/deals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeal() throws Exception {
        // Initialize the database
        dealService.save(deal);

        int databaseSizeBeforeUpdate = dealRepository.findAll().size();

        // Update the deal
        Deal updatedDeal = dealRepository.findById(deal.getId()).get();
        // Disconnect from session so that the updates on updatedDeal are not directly saved in db
        em.detach(updatedDeal);
        updatedDeal
            .tagId(UPDATED_TAG_ID)
            .fromIsoCode(UPDATED_FROM_ISO_CODE)
            .toIsoCode(UPDATED_TO_ISO_CODE)
            .time(UPDATED_TIME)
            .amount(UPDATED_AMOUNT)
            .source(UPDATED_SOURCE)
            .fromCountry(UPDATED_FROM_COUNTRY)
            .toCountry(UPDATED_TO_COUNTRY)
            .sourceFormat(UPDATED_SOURCE_FORMAT);

        restDealMockMvc.perform(put("/api/deals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDeal)))
            .andExpect(status().isOk());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeUpdate);
        Deal testDeal = dealList.get(dealList.size() - 1);
        assertThat(testDeal.getTagId()).isEqualTo(UPDATED_TAG_ID);
        assertThat(testDeal.getFromIsoCode()).isEqualTo(UPDATED_FROM_ISO_CODE);
        assertThat(testDeal.getToIsoCode()).isEqualTo(UPDATED_TO_ISO_CODE);
        assertThat(testDeal.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testDeal.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testDeal.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testDeal.getSourceFormat()).isEqualTo(UPDATED_SOURCE_FORMAT);
    }

    @Test
    @Transactional
    public void updateNonExistingDeal() throws Exception {
        int databaseSizeBeforeUpdate = dealRepository.findAll().size();

        // Create the Deal

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealMockMvc.perform(put("/api/deals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deal)))
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDeal() throws Exception {
        // Initialize the database
        dealService.save(deal);

        int databaseSizeBeforeDelete = dealRepository.findAll().size();

        // Delete the deal
        restDealMockMvc.perform(delete("/api/deals/{id}", deal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deal.class);
        Deal deal1 = new Deal();
        deal1.setId(1L);
        Deal deal2 = new Deal();
        deal2.setId(deal1.getId());
        assertThat(deal1).isEqualTo(deal2);
        deal2.setId(2L);
        assertThat(deal1).isNotEqualTo(deal2);
        deal1.setId(null);
        assertThat(deal1).isNotEqualTo(deal2);
    }
}
