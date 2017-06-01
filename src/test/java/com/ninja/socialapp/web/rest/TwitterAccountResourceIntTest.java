package com.ninja.socialapp.web.rest;

import com.ninja.socialapp.SocialappApp;

import com.ninja.socialapp.domain.TwitterAccount;
import com.ninja.socialapp.domain.Proxy;
import com.ninja.socialapp.repository.TwitterAccountRepository;
import com.ninja.socialapp.service.TwitterAccountService;
import com.ninja.socialapp.repository.search.TwitterAccountSearchRepository;
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
 * Test class for the TwitterAccountResource REST controller.
 *
 * @see TwitterAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialappApp.class)
public class TwitterAccountResourceIntTest {

    private static final String DEFAULT_EMAIL = "j3@[211.57.255.198]";
    private static final String UPDATED_EMAIL = "\"\"@[06.204.11.240]";

    private static final String DEFAULT_CONSUMER_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CONSUMER_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CONSUMER_SECRET = "AAAAAAAAAA";
    private static final String UPDATED_CONSUMER_SECRET = "BBBBBBBBBB";

    private static final String DEFAULT_ACCESS_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_ACCESS_TOKEN_SECRET = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_TOKEN_SECRET = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "https://www.aaa.com";
    private static final String UPDATED_URL = "https://www.aaabbbb.com";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    @Autowired
    private TwitterAccountRepository twitterAccountRepository;

    @Autowired
    private TwitterAccountService twitterAccountService;

    @Autowired
    private TwitterAccountSearchRepository twitterAccountSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTwitterAccountMockMvc;

    private TwitterAccount twitterAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TwitterAccountResource twitterAccountResource = new TwitterAccountResource(twitterAccountService);
        this.restTwitterAccountMockMvc = MockMvcBuilders.standaloneSetup(twitterAccountResource)
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
    public static TwitterAccount createEntity(EntityManager em) {
        TwitterAccount twitterAccount = new TwitterAccount()
            .email(DEFAULT_EMAIL)
            .consumerKey(DEFAULT_CONSUMER_KEY)
            .consumerSecret(DEFAULT_CONSUMER_SECRET)
            .accessToken(DEFAULT_ACCESS_TOKEN)
            .accessTokenSecret(DEFAULT_ACCESS_TOKEN_SECRET)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .url(DEFAULT_URL)
            .location(DEFAULT_LOCATION)
            .username(DEFAULT_USERNAME);
        // Add required entity
        Proxy proxy = ProxyResourceIntTest.createEntity(em);
        em.persist(proxy);
        em.flush();
        twitterAccount.setProxy(proxy);
        return twitterAccount;
    }

    @Before
    public void initTest() {
        twitterAccountSearchRepository.deleteAll();
        twitterAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createTwitterAccount() throws Exception {
        int databaseSizeBeforeCreate = twitterAccountRepository.findAll().size();

        // Create the TwitterAccount
        restTwitterAccountMockMvc.perform(post("/api/twitter-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterAccount)))
            .andExpect(status().isCreated());

        // Validate the TwitterAccount in the database
        List<TwitterAccount> twitterAccountList = twitterAccountRepository.findAll();
        assertThat(twitterAccountList).hasSize(databaseSizeBeforeCreate + 1);
        TwitterAccount testTwitterAccount = twitterAccountList.get(twitterAccountList.size() - 1);
        assertThat(testTwitterAccount.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTwitterAccount.getConsumerKey()).isEqualTo(DEFAULT_CONSUMER_KEY);
        assertThat(testTwitterAccount.getConsumerSecret()).isEqualTo(DEFAULT_CONSUMER_SECRET);
        assertThat(testTwitterAccount.getAccessToken()).isEqualTo(DEFAULT_ACCESS_TOKEN);
        assertThat(testTwitterAccount.getAccessTokenSecret()).isEqualTo(DEFAULT_ACCESS_TOKEN_SECRET);
        assertThat(testTwitterAccount.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTwitterAccount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTwitterAccount.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testTwitterAccount.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testTwitterAccount.getUsername()).isEqualTo(DEFAULT_USERNAME);

        // Validate the TwitterAccount in Elasticsearch
        TwitterAccount twitterAccountEs = twitterAccountSearchRepository.findOne(testTwitterAccount.getId());
        assertThat(twitterAccountEs).isEqualToComparingFieldByField(testTwitterAccount);
    }

    @Test
    @Transactional
    public void createTwitterAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = twitterAccountRepository.findAll().size();

        // Create the TwitterAccount with an existing ID
        twitterAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTwitterAccountMockMvc.perform(post("/api/twitter-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterAccount)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TwitterAccount> twitterAccountList = twitterAccountRepository.findAll();
        assertThat(twitterAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterAccountRepository.findAll().size();
        // set the field null
        twitterAccount.setEmail(null);

        // Create the TwitterAccount, which fails.

        restTwitterAccountMockMvc.perform(post("/api/twitter-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterAccount)))
            .andExpect(status().isBadRequest());

        List<TwitterAccount> twitterAccountList = twitterAccountRepository.findAll();
        assertThat(twitterAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConsumerKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterAccountRepository.findAll().size();
        // set the field null
        twitterAccount.setConsumerKey(null);

        // Create the TwitterAccount, which fails.

        restTwitterAccountMockMvc.perform(post("/api/twitter-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterAccount)))
            .andExpect(status().isBadRequest());

        List<TwitterAccount> twitterAccountList = twitterAccountRepository.findAll();
        assertThat(twitterAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConsumerSecretIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterAccountRepository.findAll().size();
        // set the field null
        twitterAccount.setConsumerSecret(null);

        // Create the TwitterAccount, which fails.

        restTwitterAccountMockMvc.perform(post("/api/twitter-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterAccount)))
            .andExpect(status().isBadRequest());

        List<TwitterAccount> twitterAccountList = twitterAccountRepository.findAll();
        assertThat(twitterAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccessTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterAccountRepository.findAll().size();
        // set the field null
        twitterAccount.setAccessToken(null);

        // Create the TwitterAccount, which fails.

        restTwitterAccountMockMvc.perform(post("/api/twitter-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterAccount)))
            .andExpect(status().isBadRequest());

        List<TwitterAccount> twitterAccountList = twitterAccountRepository.findAll();
        assertThat(twitterAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccessTokenSecretIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterAccountRepository.findAll().size();
        // set the field null
        twitterAccount.setAccessTokenSecret(null);

        // Create the TwitterAccount, which fails.

        restTwitterAccountMockMvc.perform(post("/api/twitter-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterAccount)))
            .andExpect(status().isBadRequest());

        List<TwitterAccount> twitterAccountList = twitterAccountRepository.findAll();
        assertThat(twitterAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterAccountRepository.findAll().size();
        // set the field null
        twitterAccount.setName(null);

        // Create the TwitterAccount, which fails.

        restTwitterAccountMockMvc.perform(post("/api/twitter-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterAccount)))
            .andExpect(status().isBadRequest());

        List<TwitterAccount> twitterAccountList = twitterAccountRepository.findAll();
        assertThat(twitterAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterAccountRepository.findAll().size();
        // set the field null
        twitterAccount.setUsername(null);

        // Create the TwitterAccount, which fails.

        restTwitterAccountMockMvc.perform(post("/api/twitter-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterAccount)))
            .andExpect(status().isBadRequest());

        List<TwitterAccount> twitterAccountList = twitterAccountRepository.findAll();
        assertThat(twitterAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTwitterAccounts() throws Exception {
        // Initialize the database
        twitterAccountRepository.saveAndFlush(twitterAccount);

        // Get all the twitterAccountList
        restTwitterAccountMockMvc.perform(get("/api/twitter-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(twitterAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].consumerKey").value(hasItem(DEFAULT_CONSUMER_KEY.toString())))
            .andExpect(jsonPath("$.[*].consumerSecret").value(hasItem(DEFAULT_CONSUMER_SECRET.toString())))
            .andExpect(jsonPath("$.[*].accessToken").value(hasItem(DEFAULT_ACCESS_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].accessTokenSecret").value(hasItem(DEFAULT_ACCESS_TOKEN_SECRET.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())));
    }

    @Test
    @Transactional
    public void getTwitterAccount() throws Exception {
        // Initialize the database
        twitterAccountRepository.saveAndFlush(twitterAccount);

        // Get the twitterAccount
        restTwitterAccountMockMvc.perform(get("/api/twitter-accounts/{id}", twitterAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(twitterAccount.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.consumerKey").value(DEFAULT_CONSUMER_KEY.toString()))
            .andExpect(jsonPath("$.consumerSecret").value(DEFAULT_CONSUMER_SECRET.toString()))
            .andExpect(jsonPath("$.accessToken").value(DEFAULT_ACCESS_TOKEN.toString()))
            .andExpect(jsonPath("$.accessTokenSecret").value(DEFAULT_ACCESS_TOKEN_SECRET.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTwitterAccount() throws Exception {
        // Get the twitterAccount
        restTwitterAccountMockMvc.perform(get("/api/twitter-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTwitterAccount() throws Exception {
        // Initialize the database
        twitterAccountService.save(twitterAccount);

        int databaseSizeBeforeUpdate = twitterAccountRepository.findAll().size();

        // Update the twitterAccount
        TwitterAccount updatedTwitterAccount = twitterAccountRepository.findOne(twitterAccount.getId());
        updatedTwitterAccount
            .email(UPDATED_EMAIL)
            .consumerKey(UPDATED_CONSUMER_KEY)
            .consumerSecret(UPDATED_CONSUMER_SECRET)
            .accessToken(UPDATED_ACCESS_TOKEN)
            .accessTokenSecret(UPDATED_ACCESS_TOKEN_SECRET)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .location(UPDATED_LOCATION)
            .username(UPDATED_USERNAME);

        restTwitterAccountMockMvc.perform(put("/api/twitter-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTwitterAccount)))
            .andExpect(status().isOk());

        // Validate the TwitterAccount in the database
        List<TwitterAccount> twitterAccountList = twitterAccountRepository.findAll();
        assertThat(twitterAccountList).hasSize(databaseSizeBeforeUpdate);
        TwitterAccount testTwitterAccount = twitterAccountList.get(twitterAccountList.size() - 1);
        assertThat(testTwitterAccount.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTwitterAccount.getConsumerKey()).isEqualTo(UPDATED_CONSUMER_KEY);
        assertThat(testTwitterAccount.getConsumerSecret()).isEqualTo(UPDATED_CONSUMER_SECRET);
        assertThat(testTwitterAccount.getAccessToken()).isEqualTo(UPDATED_ACCESS_TOKEN);
        assertThat(testTwitterAccount.getAccessTokenSecret()).isEqualTo(UPDATED_ACCESS_TOKEN_SECRET);
        assertThat(testTwitterAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTwitterAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTwitterAccount.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testTwitterAccount.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testTwitterAccount.getUsername()).isEqualTo(UPDATED_USERNAME);

        // Validate the TwitterAccount in Elasticsearch
        TwitterAccount twitterAccountEs = twitterAccountSearchRepository.findOne(testTwitterAccount.getId());
        assertThat(twitterAccountEs).isEqualToComparingFieldByField(testTwitterAccount);
    }

    @Test
    @Transactional
    public void updateNonExistingTwitterAccount() throws Exception {
        int databaseSizeBeforeUpdate = twitterAccountRepository.findAll().size();

        // Create the TwitterAccount

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTwitterAccountMockMvc.perform(put("/api/twitter-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterAccount)))
            .andExpect(status().isCreated());

        // Validate the TwitterAccount in the database
        List<TwitterAccount> twitterAccountList = twitterAccountRepository.findAll();
        assertThat(twitterAccountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTwitterAccount() throws Exception {
        // Initialize the database
        twitterAccountService.save(twitterAccount);

        int databaseSizeBeforeDelete = twitterAccountRepository.findAll().size();

        // Get the twitterAccount
        restTwitterAccountMockMvc.perform(delete("/api/twitter-accounts/{id}", twitterAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean twitterAccountExistsInEs = twitterAccountSearchRepository.exists(twitterAccount.getId());
        assertThat(twitterAccountExistsInEs).isFalse();

        // Validate the database is empty
        List<TwitterAccount> twitterAccountList = twitterAccountRepository.findAll();
        assertThat(twitterAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTwitterAccount() throws Exception {
        // Initialize the database
        twitterAccountService.save(twitterAccount);

        // Search the twitterAccount
        restTwitterAccountMockMvc.perform(get("/api/_search/twitter-accounts?query=id:" + twitterAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(twitterAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].consumerKey").value(hasItem(DEFAULT_CONSUMER_KEY.toString())))
            .andExpect(jsonPath("$.[*].consumerSecret").value(hasItem(DEFAULT_CONSUMER_SECRET.toString())))
            .andExpect(jsonPath("$.[*].accessToken").value(hasItem(DEFAULT_ACCESS_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].accessTokenSecret").value(hasItem(DEFAULT_ACCESS_TOKEN_SECRET.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TwitterAccount.class);
        TwitterAccount twitterAccount1 = new TwitterAccount();
        twitterAccount1.setId(1L);
        TwitterAccount twitterAccount2 = new TwitterAccount();
        twitterAccount2.setId(twitterAccount1.getId());
        assertThat(twitterAccount1).isEqualTo(twitterAccount2);
        twitterAccount2.setId(2L);
        assertThat(twitterAccount1).isNotEqualTo(twitterAccount2);
        twitterAccount1.setId(null);
        assertThat(twitterAccount1).isNotEqualTo(twitterAccount2);
    }
}
