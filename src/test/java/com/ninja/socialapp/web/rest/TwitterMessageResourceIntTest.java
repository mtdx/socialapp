package com.ninja.socialapp.web.rest;

import com.ninja.socialapp.SocialappApp;

import com.ninja.socialapp.domain.TwitterMessage;
import com.ninja.socialapp.repository.TwitterMessageRepository;
import com.ninja.socialapp.service.TwitterMessageService;
import com.ninja.socialapp.repository.search.TwitterMessageSearchRepository;
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

/**
 * Test class for the TwitterMessageResource REST controller.
 *
 * @see TwitterMessageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialappApp.class)
public class TwitterMessageResourceIntTest {

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_URL = "NdHzQwIDc:u.Ih+20y#e2BmpGIkd4nzFGkd%hJaRGkrUudhZQ3EgUx12Gtr@K~umWENz%3cUCYJ1DYG3ssKQwIIAdQKiN6=d30T-8hhs=ZJCH7f9YZHrnrSY.wk";
    private static final String UPDATED_ACCOUNT_URL = "C3.f2lCAxQHSbOwphhwg_.yr%lxr-vdm=NrEK1DDO.i945VXt_qw+wHWAjk~j60boWcgaR6yjixG9%4ogoDe1X~.UQw6VDYpnd:oZYi1m4mYVt17Z4gSmx7%LuXcZAd=qw:v4uYHPM+KnnIlFGRVJ.XSWpTUisZPs~AYOWNe7gQHk.pzoawcy";

    private static final String DEFAULT_ACCOUNT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_LOCATION = "BBBBBBBBBB";

    @Autowired
    private TwitterMessageRepository twitterMessageRepository;

    @Autowired
    private TwitterMessageService twitterMessageService;

    @Autowired
    private TwitterMessageSearchRepository twitterMessageSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTwitterMessageMockMvc;

    private TwitterMessage twitterMessage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TwitterMessageResource twitterMessageResource = new TwitterMessageResource(twitterMessageService);
        this.restTwitterMessageMockMvc = MockMvcBuilders.standaloneSetup(twitterMessageResource)
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
    public static TwitterMessage createEntity(EntityManager em) {
        TwitterMessage twitterMessage = new TwitterMessage()
            .accountName(DEFAULT_ACCOUNT_NAME)
            .accountDescription(DEFAULT_ACCOUNT_DESCRIPTION)
            .accountUrl(DEFAULT_ACCOUNT_URL)
            .accountLocation(DEFAULT_ACCOUNT_LOCATION);
        return twitterMessage;
    }

    @Before
    public void initTest() {
        twitterMessageSearchRepository.deleteAll();
        twitterMessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createTwitterMessage() throws Exception {
        int databaseSizeBeforeCreate = twitterMessageRepository.findAll().size();

        // Create the TwitterMessage
        restTwitterMessageMockMvc.perform(post("/api/twitter-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterMessage)))
            .andExpect(status().isCreated());

        // Validate the TwitterMessage in the database
        List<TwitterMessage> twitterMessageList = twitterMessageRepository.findAll();
        assertThat(twitterMessageList).hasSize(databaseSizeBeforeCreate + 1);
        TwitterMessage testTwitterMessage = twitterMessageList.get(twitterMessageList.size() - 1);
        assertThat(testTwitterMessage.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testTwitterMessage.getAccountDescription()).isEqualTo(DEFAULT_ACCOUNT_DESCRIPTION);
        assertThat(testTwitterMessage.getAccountUrl()).isEqualTo(DEFAULT_ACCOUNT_URL);
        assertThat(testTwitterMessage.getAccountLocation()).isEqualTo(DEFAULT_ACCOUNT_LOCATION);

        // Validate the TwitterMessage in Elasticsearch
        TwitterMessage twitterMessageEs = twitterMessageSearchRepository.findOne(testTwitterMessage.getId());
        assertThat(twitterMessageEs).isEqualToComparingFieldByField(testTwitterMessage);
    }

    @Test
    @Transactional
    public void createTwitterMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = twitterMessageRepository.findAll().size();

        // Create the TwitterMessage with an existing ID
        twitterMessage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTwitterMessageMockMvc.perform(post("/api/twitter-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterMessage)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TwitterMessage> twitterMessageList = twitterMessageRepository.findAll();
        assertThat(twitterMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAccountNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterMessageRepository.findAll().size();
        // set the field null
        twitterMessage.setAccountName(null);

        // Create the TwitterMessage, which fails.

        restTwitterMessageMockMvc.perform(post("/api/twitter-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterMessage)))
            .andExpect(status().isBadRequest());

        List<TwitterMessage> twitterMessageList = twitterMessageRepository.findAll();
        assertThat(twitterMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTwitterMessages() throws Exception {
        // Initialize the database
        twitterMessageRepository.saveAndFlush(twitterMessage);

        // Get all the twitterMessageList
        restTwitterMessageMockMvc.perform(get("/api/twitter-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(twitterMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME.toString())))
            .andExpect(jsonPath("$.[*].accountDescription").value(hasItem(DEFAULT_ACCOUNT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].accountUrl").value(hasItem(DEFAULT_ACCOUNT_URL.toString())))
            .andExpect(jsonPath("$.[*].accountLocation").value(hasItem(DEFAULT_ACCOUNT_LOCATION.toString())));
    }

    @Test
    @Transactional
    public void getTwitterMessage() throws Exception {
        // Initialize the database
        twitterMessageRepository.saveAndFlush(twitterMessage);

        // Get the twitterMessage
        restTwitterMessageMockMvc.perform(get("/api/twitter-messages/{id}", twitterMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(twitterMessage.getId().intValue()))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME.toString()))
            .andExpect(jsonPath("$.accountDescription").value(DEFAULT_ACCOUNT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.accountUrl").value(DEFAULT_ACCOUNT_URL.toString()))
            .andExpect(jsonPath("$.accountLocation").value(DEFAULT_ACCOUNT_LOCATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTwitterMessage() throws Exception {
        // Get the twitterMessage
        restTwitterMessageMockMvc.perform(get("/api/twitter-messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTwitterMessage() throws Exception {
        // Initialize the database
        twitterMessageService.save(twitterMessage);

        int databaseSizeBeforeUpdate = twitterMessageRepository.findAll().size();

        // Update the twitterMessage
        TwitterMessage updatedTwitterMessage = twitterMessageRepository.findOne(twitterMessage.getId());
        updatedTwitterMessage
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountDescription(UPDATED_ACCOUNT_DESCRIPTION)
            .accountUrl(UPDATED_ACCOUNT_URL)
            .accountLocation(UPDATED_ACCOUNT_LOCATION);

        restTwitterMessageMockMvc.perform(put("/api/twitter-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTwitterMessage)))
            .andExpect(status().isOk());

        // Validate the TwitterMessage in the database
        List<TwitterMessage> twitterMessageList = twitterMessageRepository.findAll();
        assertThat(twitterMessageList).hasSize(databaseSizeBeforeUpdate);
        TwitterMessage testTwitterMessage = twitterMessageList.get(twitterMessageList.size() - 1);
        assertThat(testTwitterMessage.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testTwitterMessage.getAccountDescription()).isEqualTo(UPDATED_ACCOUNT_DESCRIPTION);
        assertThat(testTwitterMessage.getAccountUrl()).isEqualTo(UPDATED_ACCOUNT_URL);
        assertThat(testTwitterMessage.getAccountLocation()).isEqualTo(UPDATED_ACCOUNT_LOCATION);

        // Validate the TwitterMessage in Elasticsearch
        TwitterMessage twitterMessageEs = twitterMessageSearchRepository.findOne(testTwitterMessage.getId());
        assertThat(twitterMessageEs).isEqualToComparingFieldByField(testTwitterMessage);
    }

    @Test
    @Transactional
    public void updateNonExistingTwitterMessage() throws Exception {
        int databaseSizeBeforeUpdate = twitterMessageRepository.findAll().size();

        // Create the TwitterMessage

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTwitterMessageMockMvc.perform(put("/api/twitter-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterMessage)))
            .andExpect(status().isCreated());

        // Validate the TwitterMessage in the database
        List<TwitterMessage> twitterMessageList = twitterMessageRepository.findAll();
        assertThat(twitterMessageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTwitterMessage() throws Exception {
        // Initialize the database
        twitterMessageService.save(twitterMessage);

        int databaseSizeBeforeDelete = twitterMessageRepository.findAll().size();

        // Get the twitterMessage
        restTwitterMessageMockMvc.perform(delete("/api/twitter-messages/{id}", twitterMessage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean twitterMessageExistsInEs = twitterMessageSearchRepository.exists(twitterMessage.getId());
        assertThat(twitterMessageExistsInEs).isFalse();

        // Validate the database is empty
        List<TwitterMessage> twitterMessageList = twitterMessageRepository.findAll();
        assertThat(twitterMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTwitterMessage() throws Exception {
        // Initialize the database
        twitterMessageService.save(twitterMessage);

        // Search the twitterMessage
        restTwitterMessageMockMvc.perform(get("/api/_search/twitter-messages?query=id:" + twitterMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(twitterMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME.toString())))
            .andExpect(jsonPath("$.[*].accountDescription").value(hasItem(DEFAULT_ACCOUNT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].accountUrl").value(hasItem(DEFAULT_ACCOUNT_URL.toString())))
            .andExpect(jsonPath("$.[*].accountLocation").value(hasItem(DEFAULT_ACCOUNT_LOCATION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TwitterMessage.class);
        TwitterMessage twitterMessage1 = new TwitterMessage();
        twitterMessage1.setId(1L);
        TwitterMessage twitterMessage2 = new TwitterMessage();
        twitterMessage2.setId(twitterMessage1.getId());
        assertThat(twitterMessage1).isEqualTo(twitterMessage2);
        twitterMessage2.setId(2L);
        assertThat(twitterMessage1).isNotEqualTo(twitterMessage2);
        twitterMessage1.setId(null);
        assertThat(twitterMessage1).isNotEqualTo(twitterMessage2);
    }
}
