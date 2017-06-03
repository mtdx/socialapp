package com.ninja.socialapp.web.rest;

import com.ninja.socialapp.SocialappApp;

import com.ninja.socialapp.domain.TwitterError;
import com.ninja.socialapp.repository.TwitterErrorRepository;
import com.ninja.socialapp.service.TwitterErrorService;
import com.ninja.socialapp.repository.search.TwitterErrorSearchRepository;
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

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ninja.socialapp.domain.enumeration.TwitterErrorType;
/**
 * Test class for the TwitterErrorResource REST controller.
 *
 * @see TwitterErrorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialappApp.class)
public class TwitterErrorResourceIntTest {

    private static final TwitterErrorType DEFAULT_TYPE = TwitterErrorType.UPDATE;
    private static final TwitterErrorType UPDATED_TYPE = TwitterErrorType.LIKE;

    private static final Integer DEFAULT_ERROR_CODE = 1;
    private static final Integer UPDATED_ERROR_CODE = 2;

    private static final String DEFAULT_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_RATE_LIMIT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_RATE_LIMIT_STATUS = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS_CODE = 1;
    private static final Integer UPDATED_STATUS_CODE = 2;

    @Autowired
    private TwitterErrorRepository twitterErrorRepository;

    @Autowired
    private TwitterErrorService twitterErrorService;

    @Autowired
    private TwitterErrorSearchRepository twitterErrorSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTwitterErrorMockMvc;

    private TwitterError twitterError;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TwitterErrorResource twitterErrorResource = new TwitterErrorResource(twitterErrorService);
        this.restTwitterErrorMockMvc = MockMvcBuilders.standaloneSetup(twitterErrorResource)
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
    public static TwitterError createEntity(EntityManager em) {
        TwitterError twitterError = new TwitterError()
            .type(DEFAULT_TYPE)
            .errorCode(DEFAULT_ERROR_CODE)
            .account(DEFAULT_ACCOUNT)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .message(DEFAULT_MESSAGE)
            .rateLimitStatus(DEFAULT_RATE_LIMIT_STATUS)
            .statusCode(DEFAULT_STATUS_CODE);
        return twitterError;
    }

    @Before
    public void initTest() {
        twitterErrorSearchRepository.deleteAll();
        twitterError = createEntity(em);
    }

    @Test
    @Transactional
    public void createTwitterError() throws Exception {
        int databaseSizeBeforeCreate = twitterErrorRepository.findAll().size();

        // Create the TwitterError
        restTwitterErrorMockMvc.perform(post("/api/twitter-errors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterError)))
            .andExpect(status().isCreated());

        // Validate the TwitterError in the database
        List<TwitterError> twitterErrorList = twitterErrorRepository.findAll();
        assertThat(twitterErrorList).hasSize(databaseSizeBeforeCreate + 1);
        TwitterError testTwitterError = twitterErrorList.get(twitterErrorList.size() - 1);
        assertThat(testTwitterError.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTwitterError.getErrorCode()).isEqualTo(DEFAULT_ERROR_CODE);
        assertThat(testTwitterError.getAccount()).isEqualTo(DEFAULT_ACCOUNT);
        assertThat(testTwitterError.getErrorMessage()).isEqualTo(DEFAULT_ERROR_MESSAGE);
        assertThat(testTwitterError.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testTwitterError.getRateLimitStatus()).isEqualTo(DEFAULT_RATE_LIMIT_STATUS);
        assertThat(testTwitterError.getStatusCode()).isEqualTo(DEFAULT_STATUS_CODE);

        // Validate the TwitterError in Elasticsearch
        TwitterError twitterErrorEs = twitterErrorSearchRepository.findOne(testTwitterError.getId());
        assertThat(twitterErrorEs).isEqualToComparingFieldByField(testTwitterError);
    }

    @Test
    @Transactional
    public void createTwitterErrorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = twitterErrorRepository.findAll().size();

        // Create the TwitterError with an existing ID
        twitterError.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTwitterErrorMockMvc.perform(post("/api/twitter-errors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterError)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TwitterError> twitterErrorList = twitterErrorRepository.findAll();
        assertThat(twitterErrorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTwitterErrors() throws Exception {
        // Initialize the database
        twitterErrorRepository.saveAndFlush(twitterError);

        // Get all the twitterErrorList
        restTwitterErrorMockMvc.perform(get("/api/twitter-errors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(twitterError.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].errorCode").value(hasItem(DEFAULT_ERROR_CODE)))
            .andExpect(jsonPath("$.[*].account").value(hasItem(DEFAULT_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].rateLimitStatus").value(hasItem(DEFAULT_RATE_LIMIT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].statusCode").value(hasItem(DEFAULT_STATUS_CODE)));
    }

    @Test
    @Transactional
    public void getTwitterError() throws Exception {
        // Initialize the database
        twitterErrorRepository.saveAndFlush(twitterError);

        // Get the twitterError
        restTwitterErrorMockMvc.perform(get("/api/twitter-errors/{id}", twitterError.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(twitterError.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.errorCode").value(DEFAULT_ERROR_CODE))
            .andExpect(jsonPath("$.account").value(DEFAULT_ACCOUNT.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.rateLimitStatus").value(DEFAULT_RATE_LIMIT_STATUS.toString()))
            .andExpect(jsonPath("$.statusCode").value(DEFAULT_STATUS_CODE));
    }

    @Test
    @Transactional
    public void getNonExistingTwitterError() throws Exception {
        // Get the twitterError
        restTwitterErrorMockMvc.perform(get("/api/twitter-errors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTwitterError() throws Exception {
        // Initialize the database
        twitterErrorService.save(twitterError);

        int databaseSizeBeforeUpdate = twitterErrorRepository.findAll().size();

        // Update the twitterError
        TwitterError updatedTwitterError = twitterErrorRepository.findOne(twitterError.getId());
        updatedTwitterError
            .type(UPDATED_TYPE)
            .errorCode(UPDATED_ERROR_CODE)
            .account(UPDATED_ACCOUNT)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .message(UPDATED_MESSAGE)
            .rateLimitStatus(UPDATED_RATE_LIMIT_STATUS)
            .statusCode(UPDATED_STATUS_CODE);

        restTwitterErrorMockMvc.perform(put("/api/twitter-errors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTwitterError)))
            .andExpect(status().isOk());

        // Validate the TwitterError in the database
        List<TwitterError> twitterErrorList = twitterErrorRepository.findAll();
        assertThat(twitterErrorList).hasSize(databaseSizeBeforeUpdate);
        TwitterError testTwitterError = twitterErrorList.get(twitterErrorList.size() - 1);
        assertThat(testTwitterError.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTwitterError.getErrorCode()).isEqualTo(UPDATED_ERROR_CODE);
        assertThat(testTwitterError.getAccount()).isEqualTo(UPDATED_ACCOUNT);
        assertThat(testTwitterError.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);
        assertThat(testTwitterError.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testTwitterError.getRateLimitStatus()).isEqualTo(UPDATED_RATE_LIMIT_STATUS);
        assertThat(testTwitterError.getStatusCode()).isEqualTo(UPDATED_STATUS_CODE);

        // Validate the TwitterError in Elasticsearch
        TwitterError twitterErrorEs = twitterErrorSearchRepository.findOne(testTwitterError.getId());
        assertThat(twitterErrorEs).isEqualToComparingFieldByField(testTwitterError);
    }

    @Test
    @Transactional
    public void updateNonExistingTwitterError() throws Exception {
        int databaseSizeBeforeUpdate = twitterErrorRepository.findAll().size();

        // Create the TwitterError

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTwitterErrorMockMvc.perform(put("/api/twitter-errors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterError)))
            .andExpect(status().isCreated());

        // Validate the TwitterError in the database
        List<TwitterError> twitterErrorList = twitterErrorRepository.findAll();
        assertThat(twitterErrorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTwitterError() throws Exception {
        // Initialize the database
        twitterErrorService.save(twitterError);

        int databaseSizeBeforeDelete = twitterErrorRepository.findAll().size();

        // Get the twitterError
        restTwitterErrorMockMvc.perform(delete("/api/twitter-errors/{id}", twitterError.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean twitterErrorExistsInEs = twitterErrorSearchRepository.exists(twitterError.getId());
        assertThat(twitterErrorExistsInEs).isFalse();

        // Validate the database is empty
        List<TwitterError> twitterErrorList = twitterErrorRepository.findAll();
        assertThat(twitterErrorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTwitterError() throws Exception {
        // Initialize the database
        twitterErrorService.save(twitterError);

        // Search the twitterError
        restTwitterErrorMockMvc.perform(get("/api/_search/twitter-errors?query=id:" + twitterError.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(twitterError.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].errorCode").value(hasItem(DEFAULT_ERROR_CODE)))
            .andExpect(jsonPath("$.[*].account").value(hasItem(DEFAULT_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].rateLimitStatus").value(hasItem(DEFAULT_RATE_LIMIT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].statusCode").value(hasItem(DEFAULT_STATUS_CODE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TwitterError.class);
        TwitterError twitterError1 = new TwitterError();
        twitterError1.setId(1L);
        TwitterError twitterError2 = new TwitterError();
        twitterError2.setId(twitterError1.getId());
        assertThat(twitterError1).isEqualTo(twitterError2);
        twitterError2.setId(2L);
        assertThat(twitterError1).isNotEqualTo(twitterError2);
        twitterError1.setId(null);
        assertThat(twitterError1).isNotEqualTo(twitterError2);
    }
}
