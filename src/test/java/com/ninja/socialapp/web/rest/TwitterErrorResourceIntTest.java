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
