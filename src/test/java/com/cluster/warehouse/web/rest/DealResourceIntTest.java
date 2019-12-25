package com.cluster.warehouse.web.rest;

import com.cluster.warehouse.BaseResourceIntTest;
import com.cluster.warehouse.WarehouseApplication;
import com.cluster.warehouse.config.ApplicationProperties;
import com.cluster.warehouse.domain.Deal;
import com.cluster.warehouse.repository.DealRepository;
import com.cluster.warehouse.service.DealService;
import com.cluster.warehouse.web.rest.errors.ExceptionTranslator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.validation.Validator;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static com.cluster.warehouse.web.rest.TestUtil.createFormattingConversionService;
import static com.cluster.warehouse.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DealResource REST controller.
 *
 * @see DealResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WarehouseApplication.class)
public class DealResourceIntTest extends BaseResourceIntTest {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private DealService dealService;

    @Autowired
    private ApplicationProperties prop;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restDealMockMvc;

    private Deal deal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DealResource dealResource = new DealResource(dealService);
        this.restDealMockMvc = MockMvcBuilders.standaloneSetup(dealResource)
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
    public static Deal createEntity() {
        Deal deal = new Deal()
				.id(DEFAULT_ID)
            .fromIsoCode(DEFAULT_FROM_ISO_CODE)
            .toIsoCode(DEFAULT_TO_ISO_CODE)
            .time(DEFAULT_TIME)
            .amount(DEFAULT_AMOUNT)
            .source(DEFAULT_SOURCE)
                .fileType(DEFAULT_FILE_TYPE)
                .uploadedOn(DEFAULT_UPLOADED_ON);
        return deal;
    }

    @Before
    public void initTest() {
        dealRepository.deleteAll();
        deal = createEntity();
    }

    @After
    public void tearDown() {
        Path path = Paths.get(prop.getBatch().getUpload().getDir(), File.separator, StringUtils.cleanPath(
                Objects.requireNonNull(FILE_NAME)));
        File f = path.toFile();
        if (path.toFile().exists()) {
            boolean delete = path.toFile().delete();
        } else {
            boolean delete = f.delete();
        }
    }

    @Test
    public void uploadDealLog() throws Exception {
        //Multipart file
        File d = new File(prop.getBatch().getUpload().getDir());
        boolean mkdir = d.exists() ? d.exists() : d.mkdirs();
        assertThat(mkdir).isTrue();

        final MockMultipartFile file = new MockMultipartFile(PARAM_NAME, FILE_NAME,
                MediaType.TEXT_PLAIN_VALUE, FILE_DETAIL.getBytes());

        restDealMockMvc.perform(multipart("/api/deals").file(file))
                .andExpect(status().isOk());
        assertThat(d.exists()).isTrue();
    }

    @Test
    public void uploadDealLogExpectBadRequest() throws Exception {
        //Multipart file
        File d = new File(prop.getBatch().getUpload().getDir());
        boolean mkdir = d.exists() ? d.exists() : d.mkdirs();
        assertThat(mkdir).isTrue();

        final File f = Paths.get(prop.getBatch().getUpload().getDir(), File.separator, StringUtils.cleanPath(
                Objects.requireNonNull(FILE_NAME))).toFile();
        boolean newFile = f.exists() ? f.exists() : f.createNewFile();
        assertThat(newFile).isTrue();

        final MockMultipartFile file = new MockMultipartFile(PARAM_NAME, FILE_NAME,
                MediaType.TEXT_PLAIN_VALUE, FILE_DETAIL.getBytes());

        restDealMockMvc.perform(multipart("/api/deals").file(file))
                .andExpect(status().isBadRequest());

        assertThat(f.delete()).isTrue();
        assertThat(d.delete()).isTrue();
    }

    @Test
    public void uploadExistingDealLog() throws Exception {
        // Initialize file path
        File d = new File(prop.getBatch().getUpload().getDir());
        boolean mkdir = d.exists() ? d.exists() : d.mkdirs();
        assertThat(mkdir).isTrue();
        final File f = Paths.get(prop.getBatch().getUpload().getDir(), File.separator, StringUtils.cleanPath(
                Objects.requireNonNull(FILE_NAME))).toFile();
        boolean newFile = f.createNewFile();
        assertThat(newFile).isTrue();

        final boolean exists = f.exists();

        // Upload existing log
        final MockMultipartFile file = new MockMultipartFile(PARAM_NAME, FILE_NAME,
                MediaType.TEXT_PLAIN_VALUE, FILE_DETAIL.getBytes());

        restDealMockMvc.perform(multipart("/api/deals").file(file))
                .andExpect(status().isBadRequest());

        assertThat(exists).isSameAs(f.exists());
        assertThat(f.delete()).isTrue();
        assertThat(d.delete()).isTrue();
    }

    @Test
    public void getAllDeals() throws Exception {
        // Initialize the database
        dealRepository.save(deal);

        // Get all the dealList
        restDealMockMvc.perform(get("/api/deals?sort=id,desc"))
            .andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(DEFAULT_ID)))
            .andExpect(jsonPath("$.[*].fromIsoCode").value(hasItem(DEFAULT_FROM_ISO_CODE.toString())))
            .andExpect(jsonPath("$.[*].toIsoCode").value(hasItem(DEFAULT_TO_ISO_CODE.toString())))
				.andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))))
				.andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
				.andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())))
				.andExpect(jsonPath("$.[*].uploadedOn").value(hasItem(sameInstant(DEFAULT_UPLOADED_ON))));
    }
    
    @Test
    public void getDeal() throws Exception {
        // Initialize the database
        dealRepository.save(deal);

        // Get the deal
        restDealMockMvc.perform(get("/api/deals/{id}", deal.getId()))
            .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.id").value(DEFAULT_ID))
				.andExpect(jsonPath("$.fromIsoCode").value(DEFAULT_FROM_ISO_CODE.toString()))
				.andExpect(jsonPath("$.toIsoCode").value(DEFAULT_TO_ISO_CODE.toString()))
				.andExpect(jsonPath("$.time").value(sameInstant(DEFAULT_TIME)))
				.andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
				.andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()))
				.andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE.toString()))
				.andExpect(jsonPath("$.uploadedOn").value(sameInstant(DEFAULT_UPLOADED_ON)));
    }

    @Test
    public void getNonExistingDeal() throws Exception {
        // Get the deal
        restDealMockMvc.perform(get("/api/deals/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    @Test
    public void searchByFileName() throws Exception {
        // Initialize the database
        dealRepository.deleteAll();
        final Deal deal = createEntity();
        dealRepository.save(deal);

        //Make search request
        restDealMockMvc.perform(get("/api/_search/deals?sort=id,desc&query=" + deal.getSource()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(DEFAULT_ID)))
                .andExpect(jsonPath("$.[*].fromIsoCode").value(hasItem(DEFAULT_FROM_ISO_CODE)))
                .andExpect(jsonPath("$.[*].toIsoCode").value(hasItem(DEFAULT_TO_ISO_CODE)))
				.andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))))
				.andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
                .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
                .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deal.class);
        Deal deal1 = new Deal();
        deal1.setId("id1");
        Deal deal2 = new Deal();
        deal2.setId(deal1.getId());
        assertThat(deal1).isEqualTo(deal2);
        deal2.setId("id2");
        assertThat(deal1).isNotEqualTo(deal2);
        deal1.setId(null);
        assertThat(deal1).isNotEqualTo(deal2);
    }
}
