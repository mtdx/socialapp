package com.ninja.socialapp.web.rest;

import com.ninja.socialapp.SocialappApp;

import com.ninja.socialapp.domain.TwitterKeyword;
import com.ninja.socialapp.repository.TwitterKeywordRepository;
import com.ninja.socialapp.service.TwitterKeywordService;
import com.ninja.socialapp.repository.search.TwitterKeywordSearchRepository;
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

import com.ninja.socialapp.domain.enumeration.KeywordStatus;
/**
 * Test class for the TwitterKeywordResource REST controller.
 *
 * @see TwitterKeywordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialappApp.class)
public class TwitterKeywordResourceIntTest {

    private static final KeywordStatus DEFAULT_STATUS = KeywordStatus.IN_PROGRESS;
    private static final KeywordStatus UPDATED_STATUS = KeywordStatus.IN_PROGRESS;

    private static final String DEFAULT_KEYWORD = "aaaaaaaaaa";
    private static final String UPDATED_KEYWORD = "bbbbbbbbbb";

    private static final Integer DEFAULT_COMPETITORS = 0;
    private static final Integer UPDATED_COMPETITORS = 0;

    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer UPDATED_PAGE = 0;

    private static final Boolean DEFAULT_STOP = false;
    private static final Boolean UPDATED_STOP = false;

    private static final Boolean DEFAULT_RESET = false;
    private static final Boolean UPDATED_RESET = false;

    private static final Instant DEFAULT_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private TwitterKeywordRepository twitterKeywordRepository;

    @Autowired
    private TwitterKeywordService twitterKeywordService;

    @Autowired
    private TwitterKeywordSearchRepository twitterKeywordSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTwitterKeywordMockMvc;

    private TwitterKeyword twitterKeyword;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TwitterKeywordResource twitterKeywordResource = new TwitterKeywordResource(twitterKeywordService);
        this.restTwitterKeywordMockMvc = MockMvcBuilders.standaloneSetup(twitterKeywordResource)
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
    public static TwitterKeyword createEntity(EntityManager em) {
        TwitterKeyword twitterKeyword = new TwitterKeyword()
            .status(DEFAULT_STATUS)
            .keyword(DEFAULT_KEYWORD)
            .competitors(DEFAULT_COMPETITORS)
            .page(DEFAULT_PAGE)
            .stop(DEFAULT_STOP)
            .reset(DEFAULT_RESET)
            .created(DEFAULT_CREATED);
        return twitterKeyword;
    }

    @Before
    public void initTest() {
        twitterKeywordSearchRepository.deleteAll();
        twitterKeyword = createEntity(em);
    }

    @Test
    @Transactional
    public void createTwitterKeyword() throws Exception {
        int databaseSizeBeforeCreate = twitterKeywordRepository.findAll().size();

        // Create the TwitterKeyword
        restTwitterKeywordMockMvc.perform(post("/api/twitter-keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterKeyword)))
            .andExpect(status().isCreated());

        // Validate the TwitterKeyword in the database
        List<TwitterKeyword> twitterKeywordList = twitterKeywordRepository.findAll();
        assertThat(twitterKeywordList).hasSize(databaseSizeBeforeCreate + 1);
        TwitterKeyword testTwitterKeyword = twitterKeywordList.get(twitterKeywordList.size() - 1);
        assertThat(testTwitterKeyword.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTwitterKeyword.getKeyword()).isEqualTo(DEFAULT_KEYWORD);
        assertThat(testTwitterKeyword.getCompetitors()).isEqualTo(DEFAULT_COMPETITORS);
        assertThat(testTwitterKeyword.getPage()).isEqualTo(DEFAULT_PAGE);
        assertThat(testTwitterKeyword.isStop()).isEqualTo(DEFAULT_STOP);
        assertThat(testTwitterKeyword.isReset()).isEqualTo(DEFAULT_RESET);
     //   assertThat(testTwitterKeyword.getCreated()).isEqualTo(DEFAULT_CREATED);

        // Validate the TwitterKeyword in Elasticsearch
        TwitterKeyword twitterKeywordEs = twitterKeywordSearchRepository.findOne(testTwitterKeyword.getId());
        assertThat(twitterKeywordEs).isEqualToComparingFieldByField(testTwitterKeyword);
    }

    @Test
    @Transactional
    public void createTwitterKeywordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = twitterKeywordRepository.findAll().size();

        // Create the TwitterKeyword with an existing ID
        twitterKeyword.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTwitterKeywordMockMvc.perform(post("/api/twitter-keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterKeyword)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TwitterKeyword> twitterKeywordList = twitterKeywordRepository.findAll();
        assertThat(twitterKeywordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkKeywordIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterKeywordRepository.findAll().size();
        // set the field null
        twitterKeyword.setKeyword(null);

        // Create the TwitterKeyword, which fails.

        restTwitterKeywordMockMvc.perform(post("/api/twitter-keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterKeyword)))
            .andExpect(status().isBadRequest());

        List<TwitterKeyword> twitterKeywordList = twitterKeywordRepository.findAll();
        assertThat(twitterKeywordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTwitterKeywords() throws Exception {
        // Initialize the database
        twitterKeywordRepository.saveAndFlush(twitterKeyword);

        // Get all the twitterKeywordList
        restTwitterKeywordMockMvc.perform(get("/api/twitter-keywords?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(twitterKeyword.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD.toString())))
            .andExpect(jsonPath("$.[*].competitors").value(hasItem(DEFAULT_COMPETITORS)))
            .andExpect(jsonPath("$.[*].page").value(hasItem(DEFAULT_PAGE)))
            .andExpect(jsonPath("$.[*].stop").value(hasItem(DEFAULT_STOP.booleanValue())))
            .andExpect(jsonPath("$.[*].reset").value(hasItem(DEFAULT_RESET.booleanValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }

    @Test
    @Transactional
    public void getTwitterKeyword() throws Exception {
        // Initialize the database
        twitterKeywordRepository.saveAndFlush(twitterKeyword);

        // Get the twitterKeyword
        restTwitterKeywordMockMvc.perform(get("/api/twitter-keywords/{id}", twitterKeyword.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(twitterKeyword.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.keyword").value(DEFAULT_KEYWORD.toString()))
            .andExpect(jsonPath("$.competitors").value(DEFAULT_COMPETITORS))
            .andExpect(jsonPath("$.page").value(DEFAULT_PAGE))
            .andExpect(jsonPath("$.stop").value(DEFAULT_STOP.booleanValue()))
            .andExpect(jsonPath("$.reset").value(DEFAULT_RESET.booleanValue()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTwitterKeyword() throws Exception {
        // Get the twitterKeyword
        restTwitterKeywordMockMvc.perform(get("/api/twitter-keywords/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTwitterKeyword() throws Exception {
        // Initialize the database
        twitterKeywordService.save(twitterKeyword);

        int databaseSizeBeforeUpdate = twitterKeywordRepository.findAll().size();

        // Update the twitterKeyword
        TwitterKeyword updatedTwitterKeyword = twitterKeywordRepository.findOne(twitterKeyword.getId());
        updatedTwitterKeyword
            .status(UPDATED_STATUS)
            .keyword(UPDATED_KEYWORD)
            .competitors(UPDATED_COMPETITORS)
            .page(UPDATED_PAGE)
            .stop(UPDATED_STOP)
            .reset(UPDATED_RESET)
            .created(UPDATED_CREATED);

        restTwitterKeywordMockMvc.perform(put("/api/twitter-keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTwitterKeyword)))
            .andExpect(status().isOk());

        // Validate the TwitterKeyword in the database
        List<TwitterKeyword> twitterKeywordList = twitterKeywordRepository.findAll();
        assertThat(twitterKeywordList).hasSize(databaseSizeBeforeUpdate);
        TwitterKeyword testTwitterKeyword = twitterKeywordList.get(twitterKeywordList.size() - 1);
        assertThat(testTwitterKeyword.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTwitterKeyword.getKeyword()).isEqualTo(UPDATED_KEYWORD);
        assertThat(testTwitterKeyword.getCompetitors()).isEqualTo(UPDATED_COMPETITORS);
        assertThat(testTwitterKeyword.getPage()).isEqualTo(UPDATED_PAGE);
        assertThat(testTwitterKeyword.isStop()).isEqualTo(UPDATED_STOP);
        assertThat(testTwitterKeyword.isReset()).isEqualTo(UPDATED_RESET);
        assertThat(testTwitterKeyword.getCreated()).isEqualTo(UPDATED_CREATED);

        // Validate the TwitterKeyword in Elasticsearch
        TwitterKeyword twitterKeywordEs = twitterKeywordSearchRepository.findOne(testTwitterKeyword.getId());
        assertThat(twitterKeywordEs).isEqualToComparingFieldByField(testTwitterKeyword);
    }

    @Test
    @Transactional
    public void updateNonExistingTwitterKeyword() throws Exception {
        int databaseSizeBeforeUpdate = twitterKeywordRepository.findAll().size();

        // Create the TwitterKeyword

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTwitterKeywordMockMvc.perform(put("/api/twitter-keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterKeyword)))
            .andExpect(status().isCreated());

        // Validate the TwitterKeyword in the database
        List<TwitterKeyword> twitterKeywordList = twitterKeywordRepository.findAll();
        assertThat(twitterKeywordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTwitterKeyword() throws Exception {
        // Initialize the database
        twitterKeywordService.save(twitterKeyword);

        int databaseSizeBeforeDelete = twitterKeywordRepository.findAll().size();

        // Get the twitterKeyword
        restTwitterKeywordMockMvc.perform(delete("/api/twitter-keywords/{id}", twitterKeyword.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean twitterKeywordExistsInEs = twitterKeywordSearchRepository.exists(twitterKeyword.getId());
        assertThat(twitterKeywordExistsInEs).isFalse();

        // Validate the database is empty
        List<TwitterKeyword> twitterKeywordList = twitterKeywordRepository.findAll();
        assertThat(twitterKeywordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTwitterKeyword() throws Exception {
        // Initialize the database
        twitterKeywordService.save(twitterKeyword);

        // Search the twitterKeyword
        restTwitterKeywordMockMvc.perform(get("/api/_search/twitter-keywords?query=id:" + twitterKeyword.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(twitterKeyword.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD.toString())))
            .andExpect(jsonPath("$.[*].competitors").value(hasItem(DEFAULT_COMPETITORS)))
            .andExpect(jsonPath("$.[*].page").value(hasItem(DEFAULT_PAGE)))
            .andExpect(jsonPath("$.[*].stop").value(hasItem(DEFAULT_STOP.booleanValue())))
            .andExpect(jsonPath("$.[*].reset").value(hasItem(DEFAULT_RESET.booleanValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TwitterKeyword.class);
        TwitterKeyword twitterKeyword1 = new TwitterKeyword();
        twitterKeyword1.setId(1L);
        TwitterKeyword twitterKeyword2 = new TwitterKeyword();
        twitterKeyword2.setId(twitterKeyword1.getId());
        assertThat(twitterKeyword1).isEqualTo(twitterKeyword2);
        twitterKeyword2.setId(2L);
        assertThat(twitterKeyword1).isNotEqualTo(twitterKeyword2);
        twitterKeyword1.setId(null);
        assertThat(twitterKeyword1).isNotEqualTo(twitterKeyword2);
    }
}
