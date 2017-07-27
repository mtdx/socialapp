package com.ninja.socialapp.web.rest;

import com.ninja.socialapp.SocialappApp;

import com.ninja.socialapp.domain.RetweetAccount;
import com.ninja.socialapp.repository.RetweetAccountRepository;
import com.ninja.socialapp.service.RetweetAccountService;
import com.ninja.socialapp.repository.search.RetweetAccountSearchRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ninja.socialapp.domain.enumeration.RetweetAccountStatus;
/**
 * Test class for the RetweetAccountResource REST controller.
 *
 * @see RetweetAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialappApp.class)
public class RetweetAccountResourceIntTest {

    private static final RetweetAccountStatus DEFAULT_STATUS = RetweetAccountStatus.IN_PROGRESS;
    private static final RetweetAccountStatus UPDATED_STATUS = RetweetAccountStatus.STOPPED;

    private static final String DEFAULT_USERID = "37";
    private static final String UPDATED_USERID = "2";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_KEYWORDS = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORDS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STOP = false;
    private static final Boolean UPDATED_STOP = true;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private RetweetAccountRepository retweetAccountRepository;

    @Autowired
    private RetweetAccountService retweetAccountService;

    @Autowired
    private RetweetAccountSearchRepository retweetAccountSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRetweetAccountMockMvc;

    private RetweetAccount retweetAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RetweetAccountResource retweetAccountResource = new RetweetAccountResource(retweetAccountService);
        this.restRetweetAccountMockMvc = MockMvcBuilders.standaloneSetup(retweetAccountResource)
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
    public static RetweetAccount createEntity(EntityManager em) {
        RetweetAccount retweetAccount = new RetweetAccount()
            .status(DEFAULT_STATUS)
            .userid(DEFAULT_USERID)
            .username(DEFAULT_USERNAME)
            .keywords(DEFAULT_KEYWORDS)
            .stop(DEFAULT_STOP)
            .created(DEFAULT_CREATED);
        return retweetAccount;
    }

    @Before
    public void initTest() {
        retweetAccountSearchRepository.deleteAll();
        retweetAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createRetweetAccount() throws Exception {
        int databaseSizeBeforeCreate = retweetAccountRepository.findAll().size();

        // Create the RetweetAccount
        restRetweetAccountMockMvc.perform(post("/api/retweet-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(retweetAccount)))
            .andExpect(status().isCreated());

        // Validate the RetweetAccount in the database
        List<RetweetAccount> retweetAccountList = retweetAccountRepository.findAll();
        assertThat(retweetAccountList).hasSize(databaseSizeBeforeCreate + 1);
        RetweetAccount testRetweetAccount = retweetAccountList.get(retweetAccountList.size() - 1);
        assertThat(testRetweetAccount.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRetweetAccount.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testRetweetAccount.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testRetweetAccount.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testRetweetAccount.isStop()).isEqualTo(DEFAULT_STOP);
        assertThat(testRetweetAccount.getCreated()).isEqualTo(DEFAULT_CREATED);

        // Validate the RetweetAccount in Elasticsearch
        RetweetAccount retweetAccountEs = retweetAccountSearchRepository.findOne(testRetweetAccount.getId());
        assertThat(retweetAccountEs).isEqualToComparingFieldByField(testRetweetAccount);
    }

    @Test
    @Transactional
    public void createRetweetAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = retweetAccountRepository.findAll().size();

        // Create the RetweetAccount with an existing ID
        retweetAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRetweetAccountMockMvc.perform(post("/api/retweet-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(retweetAccount)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RetweetAccount> retweetAccountList = retweetAccountRepository.findAll();
        assertThat(retweetAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUseridIsRequired() throws Exception {
        int databaseSizeBeforeTest = retweetAccountRepository.findAll().size();
        // set the field null
        retweetAccount.setUserid(null);

        // Create the RetweetAccount, which fails.

        restRetweetAccountMockMvc.perform(post("/api/retweet-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(retweetAccount)))
            .andExpect(status().isBadRequest());

        List<RetweetAccount> retweetAccountList = retweetAccountRepository.findAll();
        assertThat(retweetAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = retweetAccountRepository.findAll().size();
        // set the field null
        retweetAccount.setUsername(null);

        // Create the RetweetAccount, which fails.

        restRetweetAccountMockMvc.perform(post("/api/retweet-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(retweetAccount)))
            .andExpect(status().isBadRequest());

        List<RetweetAccount> retweetAccountList = retweetAccountRepository.findAll();
        assertThat(retweetAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRetweetAccounts() throws Exception {
        // Initialize the database
        retweetAccountRepository.saveAndFlush(retweetAccount);

        // Get all the retweetAccountList
        restRetweetAccountMockMvc.perform(get("/api/retweet-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(retweetAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].keywords").value(hasItem(DEFAULT_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].stop").value(hasItem(DEFAULT_STOP.booleanValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }

    @Test
    @Transactional
    public void getRetweetAccount() throws Exception {
        // Initialize the database
        retweetAccountRepository.saveAndFlush(retweetAccount);

        // Get the retweetAccount
        restRetweetAccountMockMvc.perform(get("/api/retweet-accounts/{id}", retweetAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(retweetAccount.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.keywords").value(DEFAULT_KEYWORDS.toString()))
            .andExpect(jsonPath("$.stop").value(DEFAULT_STOP.booleanValue()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRetweetAccount() throws Exception {
        // Get the retweetAccount
        restRetweetAccountMockMvc.perform(get("/api/retweet-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRetweetAccount() throws Exception {
        // Initialize the database
        retweetAccountService.save(retweetAccount);

        int databaseSizeBeforeUpdate = retweetAccountRepository.findAll().size();

        // Update the retweetAccount
        RetweetAccount updatedRetweetAccount = retweetAccountRepository.findOne(retweetAccount.getId());
        updatedRetweetAccount
            .status(UPDATED_STATUS)
            .userid(UPDATED_USERID)
            .username(UPDATED_USERNAME)
            .keywords(UPDATED_KEYWORDS)
            .stop(UPDATED_STOP)
            .created(UPDATED_CREATED);

        restRetweetAccountMockMvc.perform(put("/api/retweet-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRetweetAccount)))
            .andExpect(status().isOk());

        // Validate the RetweetAccount in the database
        List<RetweetAccount> retweetAccountList = retweetAccountRepository.findAll();
        assertThat(retweetAccountList).hasSize(databaseSizeBeforeUpdate);
        RetweetAccount testRetweetAccount = retweetAccountList.get(retweetAccountList.size() - 1);
        assertThat(testRetweetAccount.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRetweetAccount.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testRetweetAccount.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testRetweetAccount.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testRetweetAccount.isStop()).isEqualTo(UPDATED_STOP);
        assertThat(testRetweetAccount.getCreated()).isEqualTo(UPDATED_CREATED);

        // Validate the RetweetAccount in Elasticsearch
        RetweetAccount retweetAccountEs = retweetAccountSearchRepository.findOne(testRetweetAccount.getId());
        assertThat(retweetAccountEs).isEqualToComparingFieldByField(testRetweetAccount);
    }

    @Test
    @Transactional
    public void updateNonExistingRetweetAccount() throws Exception {
        int databaseSizeBeforeUpdate = retweetAccountRepository.findAll().size();

        // Create the RetweetAccount

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRetweetAccountMockMvc.perform(put("/api/retweet-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(retweetAccount)))
            .andExpect(status().isCreated());

        // Validate the RetweetAccount in the database
        List<RetweetAccount> retweetAccountList = retweetAccountRepository.findAll();
        assertThat(retweetAccountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRetweetAccount() throws Exception {
        // Initialize the database
        retweetAccountService.save(retweetAccount);

        int databaseSizeBeforeDelete = retweetAccountRepository.findAll().size();

        // Get the retweetAccount
        restRetweetAccountMockMvc.perform(delete("/api/retweet-accounts/{id}", retweetAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean retweetAccountExistsInEs = retweetAccountSearchRepository.exists(retweetAccount.getId());
        assertThat(retweetAccountExistsInEs).isFalse();

        // Validate the database is empty
        List<RetweetAccount> retweetAccountList = retweetAccountRepository.findAll();
        assertThat(retweetAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRetweetAccount() throws Exception {
        // Initialize the database
        retweetAccountService.save(retweetAccount);

        // Search the retweetAccount
        restRetweetAccountMockMvc.perform(get("/api/_search/retweet-accounts?query=id:" + retweetAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(retweetAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].keywords").value(hasItem(DEFAULT_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].stop").value(hasItem(DEFAULT_STOP.booleanValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RetweetAccount.class);
        RetweetAccount retweetAccount1 = new RetweetAccount();
        retweetAccount1.setId(1L);
        RetweetAccount retweetAccount2 = new RetweetAccount();
        retweetAccount2.setId(retweetAccount1.getId());
        assertThat(retweetAccount1).isEqualTo(retweetAccount2);
        retweetAccount2.setId(2L);
        assertThat(retweetAccount1).isNotEqualTo(retweetAccount2);
        retweetAccount1.setId(null);
        assertThat(retweetAccount1).isNotEqualTo(retweetAccount2);
    }
}
