package com.ninja.socialapp.web.rest;

import com.ninja.socialapp.SocialappApp;

import com.ninja.socialapp.domain.Header;
import com.ninja.socialapp.repository.HeaderRepository;
import com.ninja.socialapp.service.HeaderService;
import com.ninja.socialapp.repository.search.HeaderSearchRepository;
import com.ninja.socialapp.web.rest.errors.ExceptionTranslator;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HeaderResource REST controller.
 *
 * @see HeaderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialappApp.class)
public class HeaderResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(400000, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private HeaderRepository headerRepository;

    @Autowired
    private HeaderService headerService;

    @Autowired
    private HeaderSearchRepository headerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHeaderMockMvc;

    private Header header;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HeaderResource headerResource = new HeaderResource(headerService);
        this.restHeaderMockMvc = MockMvcBuilders.standaloneSetup(headerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Header createEntity(EntityManager em) {
        Header header = new Header()
            .name(DEFAULT_NAME)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return header;
    }

    @Before
    public void initTest() {
        headerSearchRepository.deleteAll();
        header = createEntity(em);
    }

    @Test
    @Transactional
    public void createHeader() throws Exception {
        int databaseSizeBeforeCreate = headerRepository.findAll().size();

        // Create the Header
        restHeaderMockMvc.perform(post("/api/headers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(header)))
            .andExpect(status().isCreated());

        // Validate the Header in the database
        List<Header> headerList = headerRepository.findAll();
        assertThat(headerList).hasSize(databaseSizeBeforeCreate + 1);
        Header testHeader = headerList.get(headerList.size() - 1);
        assertThat(testHeader.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHeader.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testHeader.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);

        // Validate the Header in Elasticsearch
        Header headerEs = headerSearchRepository.findOne(testHeader.getId());
        assertThat(headerEs).isEqualToComparingFieldByField(testHeader);
    }

    @Test
    @Transactional
    public void createHeaderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = headerRepository.findAll().size();

        // Create the Header with an existing ID
        header.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHeaderMockMvc.perform(post("/api/headers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(header)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Header> headerList = headerRepository.findAll();
        assertThat(headerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = headerRepository.findAll().size();
        // set the field null
        header.setName(null);

        // Create the Header, which fails.

        restHeaderMockMvc.perform(post("/api/headers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(header)))
            .andExpect(status().isBadRequest());

        List<Header> headerList = headerRepository.findAll();
        assertThat(headerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImageIsRequired() throws Exception {
        int databaseSizeBeforeTest = headerRepository.findAll().size();
        // set the field null
        header.setImage(null);

        // Create the Header, which fails.

        restHeaderMockMvc.perform(post("/api/headers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(header)))
            .andExpect(status().isBadRequest());

        List<Header> headerList = headerRepository.findAll();
        assertThat(headerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHeaders() throws Exception {
        // Initialize the database
        headerRepository.saveAndFlush(header);

        // Get all the headerList
        restHeaderMockMvc.perform(get("/api/headers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(header.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    public void getHeader() throws Exception {
        // Initialize the database
        headerRepository.saveAndFlush(header);

        // Get the header
        restHeaderMockMvc.perform(get("/api/headers/{id}", header.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(header.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingHeader() throws Exception {
        // Get the header
        restHeaderMockMvc.perform(get("/api/headers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHeader() throws Exception {
        // Initialize the database
        headerService.save(header);

        int databaseSizeBeforeUpdate = headerRepository.findAll().size();

        // Update the header
        Header updatedHeader = headerRepository.findOne(header.getId());
        updatedHeader
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restHeaderMockMvc.perform(put("/api/headers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHeader)))
            .andExpect(status().isOk());

        // Validate the Header in the database
        List<Header> headerList = headerRepository.findAll();
        assertThat(headerList).hasSize(databaseSizeBeforeUpdate);
        Header testHeader = headerList.get(headerList.size() - 1);
        assertThat(testHeader.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHeader.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testHeader.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);

        // Validate the Header in Elasticsearch
        Header headerEs = headerSearchRepository.findOne(testHeader.getId());
        assertThat(headerEs).isEqualToComparingFieldByField(testHeader);
    }

    @Test
    @Transactional
    public void updateNonExistingHeader() throws Exception {
        int databaseSizeBeforeUpdate = headerRepository.findAll().size();

        // Create the Header

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHeaderMockMvc.perform(put("/api/headers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(header)))
            .andExpect(status().isCreated());

        // Validate the Header in the database
        List<Header> headerList = headerRepository.findAll();
        assertThat(headerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHeader() throws Exception {
        // Initialize the database
        headerService.save(header);

        int databaseSizeBeforeDelete = headerRepository.findAll().size();

        // Get the header
        restHeaderMockMvc.perform(delete("/api/headers/{id}", header.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean headerExistsInEs = headerSearchRepository.exists(header.getId());
        assertThat(headerExistsInEs).isFalse();

        // Validate the database is empty
        List<Header> headerList = headerRepository.findAll();
        assertThat(headerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHeader() throws Exception {
        // Initialize the database
        headerService.save(header);

        // Search the header
        restHeaderMockMvc.perform(get("/api/_search/headers?query=id:" + header.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(header.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Header.class);
        Header header1 = new Header();
        header1.setId(1L);
        Header header2 = new Header();
        header2.setId(header1.getId());
        assertThat(header1).isEqualTo(header2);
        header2.setId(2L);
        assertThat(header1).isNotEqualTo(header2);
        header1.setId(null);
        assertThat(header1).isNotEqualTo(header2);
    }
}
