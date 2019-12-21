package com.cluster.warehouse.web.rest;

import com.cluster.warehouse.BaseResourceIntTest;
import com.cluster.warehouse.WarehouseApplication;
import com.cluster.warehouse.domain.Deal;
import com.cluster.warehouse.domain.InvalidDeal;
import com.cluster.warehouse.repository.InvalidDealRepository;
import com.cluster.warehouse.service.InvalidDealService;
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
 * Test class for the InvalidDealResource REST controller.
 *
 * @see InvalidDealResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WarehouseApplication.class)
public class InvalidDealResourceIntTest extends BaseResourceIntTest {

	private static final String DEFAULT_EXCEPTION = "AAAAAAAAAA";


    @Autowired
    private InvalidDealRepository invalidDealRepository;

    @Autowired
    private InvalidDealService invalidDealService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restInvalidDealMockMvc;

    private InvalidDeal invalidDeal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
		final InvalidDealResource invalidDealResource = new InvalidDealResource(invalidDealService);
        this.restInvalidDealMockMvc = MockMvcBuilders.standaloneSetup(invalidDealResource)
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
	public static InvalidDeal createEntity() {
		Deal deal = DealResourceIntTest.createEntity();
		return new InvalidDeal(deal, DEFAULT_EXCEPTION);
    }

    @Before
    public void initTest() {
		invalidDealRepository.deleteAll();
		invalidDeal = createEntity();
    }

    @Test
    public void getAllInvalidDeals() throws Exception {
        // Initialize the database
		invalidDealRepository.save(invalidDeal);

        // Get all the invalidDealList
        restInvalidDealMockMvc.perform(get("/api/invalid-deals?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(invalidDeal.getId())))
				.andExpect(jsonPath("$.[*].tagId").value(hasItem(DEFAULT_TAG_ID.toString())))
				.andExpect(jsonPath("$.[*].fromIsoCode").value(hasItem(DEFAULT_FROM_ISO_CODE.toString())))
				.andExpect(jsonPath("$.[*].toIsoCode").value(hasItem(DEFAULT_TO_ISO_CODE.toString())))
//            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))))
				.andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
				.andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
				.andExpect(jsonPath("$.[*].uploadedOn").value(hasItem(DEFAULT_UPLOADED_ON.toString())));
    }

	@Test
    public void getInvalidDeal() throws Exception {
        // Initialize the database
		invalidDealRepository.save(invalidDeal);

        // Get the invalidDeal
        restInvalidDealMockMvc.perform(get("/api/invalid-deals/{id}", invalidDeal.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.id").value(invalidDeal.getId()))
				.andExpect(jsonPath("$.tagId").value(DEFAULT_TAG_ID.toString()))
				.andExpect(jsonPath("$.fromIsoCode").value(DEFAULT_FROM_ISO_CODE.toString()))
				.andExpect(jsonPath("$.toIsoCode").value(DEFAULT_TO_ISO_CODE.toString()))
//            .andExpect(jsonPath("$.time").value(sameInstant(DEFAULT_TIME)))
				.andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
				.andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()))
				.andExpect(jsonPath("$.uploadedOn").value(DEFAULT_UPLOADED_ON.toString()));
    }

    @Test
	public void getNonExistingInvalidDeal() throws Exception {
		// Get the invalidDeal
		restInvalidDealMockMvc.perform(get("/api/invalid-deals/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
    }

	/**
	 * Executes the search, and checks that the default entity is returned
	 */
	@Test
	public void searchByFileName() throws Exception {
		// Initialize the database
		invalidDealRepository.deleteAll();
		final InvalidDeal invalidDeal = createEntity();
		invalidDealRepository.save(invalidDeal);

		//Make search request
		restInvalidDealMockMvc.perform(get("/api/_search/invalid-deals?sort=id,desc&query=" + invalidDeal.getSource()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(invalidDeal.getId())))
				.andExpect(jsonPath("$.[*].tagId").value(hasItem(DEFAULT_TAG_ID)))
				.andExpect(jsonPath("$.[*].fromIsoCode").value(hasItem(DEFAULT_FROM_ISO_CODE)))
				.andExpect(jsonPath("$.[*].toIsoCode").value(hasItem(DEFAULT_TO_ISO_CODE)))
//				.andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)))
				.andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
				.andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
				.andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE)));
	}

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvalidDeal.class);
        InvalidDeal invalidDeal1 = new InvalidDeal();
		invalidDeal1.setId("id1");
        InvalidDeal invalidDeal2 = new InvalidDeal();
        invalidDeal2.setId(invalidDeal1.getId());
        assertThat(invalidDeal1).isEqualTo(invalidDeal2);
		invalidDeal2.setId("id2");
        assertThat(invalidDeal1).isNotEqualTo(invalidDeal2);
        invalidDeal1.setId(null);
        assertThat(invalidDeal1).isNotEqualTo(invalidDeal2);
    }
}
