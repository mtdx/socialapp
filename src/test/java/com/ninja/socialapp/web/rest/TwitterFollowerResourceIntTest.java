package com.ninja.socialapp.web.rest;

import com.ninja.socialapp.SocialappApp;

import com.ninja.socialapp.domain.TwitterFollower;
import com.ninja.socialapp.repository.TwitterFollowerRepository;
import com.ninja.socialapp.service.TwitterFollowerService;
import com.ninja.socialapp.repository.search.TwitterFollowerSearchRepository;
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

/**
 * Test class for the TwitterFollowerResource REST controller.
 *
 * @see TwitterFollowerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialappApp.class)
public class TwitterFollowerResourceIntTest {

    private static final String DEFAULT_USERID = "52";
    private static final String UPDATED_USERID = "70";

    private static final Integer DEFAULT_ACCOUNT_AGE = 0;
    private static final Integer UPDATED_ACCOUNT_AGE = 1;

    private static final Integer DEFAULT_LIKES = 0;
    private static final Integer UPDATED_LIKES = 1;

    private static final Integer DEFAULT_FOLLOWERS = 0;
    private static final Integer UPDATED_FOLLOWERS = 1;

    private static final Integer DEFAULT_TWEETS = 0;
    private static final Integer UPDATED_TWEETS = 1;

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_LIKE = false;
    private static final Boolean UPDATED_LIKE = true;

    private static final Boolean DEFAULT_FOLLOW = false;
    private static final Boolean UPDATED_FOLLOW = true;

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private TwitterFollowerRepository twitterFollowerRepository;

    @Autowired
    private TwitterFollowerService twitterFollowerService;

    @Autowired
    private TwitterFollowerSearchRepository twitterFollowerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTwitterFollowerMockMvc;

    private TwitterFollower twitterFollower;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TwitterFollowerResource twitterFollowerResource = new TwitterFollowerResource(twitterFollowerService);
        this.restTwitterFollowerMockMvc = MockMvcBuilders.standaloneSetup(twitterFollowerResource)
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
    public static TwitterFollower createEntity(EntityManager em) {
        TwitterFollower twitterFollower = new TwitterFollower()
            .userid(DEFAULT_USERID)
            .accountAge(DEFAULT_ACCOUNT_AGE)
            .likes(DEFAULT_LIKES)
            .followers(DEFAULT_FOLLOWERS)
            .tweets(DEFAULT_TWEETS)
            .username(DEFAULT_USERNAME)
            .like(DEFAULT_LIKE)
            .follow(DEFAULT_FOLLOW)
            .updated(DEFAULT_UPDATED);
        return twitterFollower;
    }

    @Before
    public void initTest() {
        twitterFollowerSearchRepository.deleteAll();
        twitterFollower = createEntity(em);
    }

    @Test
    @Transactional
    public void createTwitterFollower() throws Exception {
        int databaseSizeBeforeCreate = twitterFollowerRepository.findAll().size();

        // Create the TwitterFollower
        restTwitterFollowerMockMvc.perform(post("/api/twitter-followers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterFollower)))
            .andExpect(status().isCreated());

        // Validate the TwitterFollower in the database
        List<TwitterFollower> twitterFollowerList = twitterFollowerRepository.findAll();
        assertThat(twitterFollowerList).hasSize(databaseSizeBeforeCreate + 1);
        TwitterFollower testTwitterFollower = twitterFollowerList.get(twitterFollowerList.size() - 1);
        assertThat(testTwitterFollower.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testTwitterFollower.getAccountAge()).isEqualTo(DEFAULT_ACCOUNT_AGE);
        assertThat(testTwitterFollower.getLikes()).isEqualTo(DEFAULT_LIKES);
        assertThat(testTwitterFollower.getFollowers()).isEqualTo(DEFAULT_FOLLOWERS);
        assertThat(testTwitterFollower.getTweets()).isEqualTo(DEFAULT_TWEETS);
        assertThat(testTwitterFollower.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testTwitterFollower.isLike()).isEqualTo(DEFAULT_LIKE);
        assertThat(testTwitterFollower.isFollow()).isEqualTo(DEFAULT_FOLLOW);
        assertThat(testTwitterFollower.getUpdated()).isEqualTo(DEFAULT_UPDATED);

        // Validate the TwitterFollower in Elasticsearch
        TwitterFollower twitterFollowerEs = twitterFollowerSearchRepository.findOne(testTwitterFollower.getId());
        assertThat(twitterFollowerEs).isEqualToComparingFieldByField(testTwitterFollower);
    }

    @Test
    @Transactional
    public void createTwitterFollowerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = twitterFollowerRepository.findAll().size();

        // Create the TwitterFollower with an existing ID
        twitterFollower.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTwitterFollowerMockMvc.perform(post("/api/twitter-followers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterFollower)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TwitterFollower> twitterFollowerList = twitterFollowerRepository.findAll();
        assertThat(twitterFollowerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUseridIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterFollowerRepository.findAll().size();
        // set the field null
        twitterFollower.setUserid(null);

        // Create the TwitterFollower, which fails.

        restTwitterFollowerMockMvc.perform(post("/api/twitter-followers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterFollower)))
            .andExpect(status().isBadRequest());

        List<TwitterFollower> twitterFollowerList = twitterFollowerRepository.findAll();
        assertThat(twitterFollowerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTwitterFollowers() throws Exception {
        // Initialize the database
        twitterFollowerRepository.saveAndFlush(twitterFollower);

        // Get all the twitterFollowerList
        restTwitterFollowerMockMvc.perform(get("/api/twitter-followers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(twitterFollower.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.toString())))
            .andExpect(jsonPath("$.[*].accountAge").value(hasItem(DEFAULT_ACCOUNT_AGE)))
            .andExpect(jsonPath("$.[*].likes").value(hasItem(DEFAULT_LIKES)))
            .andExpect(jsonPath("$.[*].followers").value(hasItem(DEFAULT_FOLLOWERS)))
            .andExpect(jsonPath("$.[*].tweets").value(hasItem(DEFAULT_TWEETS)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].like").value(hasItem(DEFAULT_LIKE.booleanValue())))
            .andExpect(jsonPath("$.[*].follow").value(hasItem(DEFAULT_FOLLOW.booleanValue())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));
    }

    @Test
    @Transactional
    public void getTwitterFollower() throws Exception {
        // Initialize the database
        twitterFollowerRepository.saveAndFlush(twitterFollower);

        // Get the twitterFollower
        restTwitterFollowerMockMvc.perform(get("/api/twitter-followers/{id}", twitterFollower.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(twitterFollower.getId().intValue()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.toString()))
            .andExpect(jsonPath("$.accountAge").value(DEFAULT_ACCOUNT_AGE))
            .andExpect(jsonPath("$.likes").value(DEFAULT_LIKES))
            .andExpect(jsonPath("$.followers").value(DEFAULT_FOLLOWERS))
            .andExpect(jsonPath("$.tweets").value(DEFAULT_TWEETS))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.like").value(DEFAULT_LIKE.booleanValue()))
            .andExpect(jsonPath("$.follow").value(DEFAULT_FOLLOW.booleanValue()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTwitterFollower() throws Exception {
        // Get the twitterFollower
        restTwitterFollowerMockMvc.perform(get("/api/twitter-followers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTwitterFollower() throws Exception {
        // Initialize the database
        twitterFollowerService.save(twitterFollower);

        int databaseSizeBeforeUpdate = twitterFollowerRepository.findAll().size();

        // Update the twitterFollower
        TwitterFollower updatedTwitterFollower = twitterFollowerRepository.findOne(twitterFollower.getId());
        updatedTwitterFollower
            .userid(UPDATED_USERID)
            .accountAge(UPDATED_ACCOUNT_AGE)
            .likes(UPDATED_LIKES)
            .followers(UPDATED_FOLLOWERS)
            .tweets(UPDATED_TWEETS)
            .username(UPDATED_USERNAME)
            .like(UPDATED_LIKE)
            .follow(UPDATED_FOLLOW)
            .updated(UPDATED_UPDATED);

        restTwitterFollowerMockMvc.perform(put("/api/twitter-followers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTwitterFollower)))
            .andExpect(status().isOk());

        // Validate the TwitterFollower in the database
        List<TwitterFollower> twitterFollowerList = twitterFollowerRepository.findAll();
        assertThat(twitterFollowerList).hasSize(databaseSizeBeforeUpdate);
        TwitterFollower testTwitterFollower = twitterFollowerList.get(twitterFollowerList.size() - 1);
        assertThat(testTwitterFollower.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testTwitterFollower.getAccountAge()).isEqualTo(UPDATED_ACCOUNT_AGE);
        assertThat(testTwitterFollower.getLikes()).isEqualTo(UPDATED_LIKES);
        assertThat(testTwitterFollower.getFollowers()).isEqualTo(UPDATED_FOLLOWERS);
        assertThat(testTwitterFollower.getTweets()).isEqualTo(UPDATED_TWEETS);
        assertThat(testTwitterFollower.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testTwitterFollower.isLike()).isEqualTo(UPDATED_LIKE);
        assertThat(testTwitterFollower.isFollow()).isEqualTo(UPDATED_FOLLOW);
        assertThat(testTwitterFollower.getUpdated()).isEqualTo(UPDATED_UPDATED);

        // Validate the TwitterFollower in Elasticsearch
        TwitterFollower twitterFollowerEs = twitterFollowerSearchRepository.findOne(testTwitterFollower.getId());
        assertThat(twitterFollowerEs).isEqualToComparingFieldByField(testTwitterFollower);
    }

    @Test
    @Transactional
    public void updateNonExistingTwitterFollower() throws Exception {
        int databaseSizeBeforeUpdate = twitterFollowerRepository.findAll().size();

        // Create the TwitterFollower

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTwitterFollowerMockMvc.perform(put("/api/twitter-followers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(twitterFollower)))
            .andExpect(status().isCreated());

        // Validate the TwitterFollower in the database
        List<TwitterFollower> twitterFollowerList = twitterFollowerRepository.findAll();
        assertThat(twitterFollowerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTwitterFollower() throws Exception {
        // Initialize the database
        twitterFollowerService.save(twitterFollower);

        int databaseSizeBeforeDelete = twitterFollowerRepository.findAll().size();

        // Get the twitterFollower
        restTwitterFollowerMockMvc.perform(delete("/api/twitter-followers/{id}", twitterFollower.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean twitterFollowerExistsInEs = twitterFollowerSearchRepository.exists(twitterFollower.getId());
        assertThat(twitterFollowerExistsInEs).isFalse();

        // Validate the database is empty
        List<TwitterFollower> twitterFollowerList = twitterFollowerRepository.findAll();
        assertThat(twitterFollowerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTwitterFollower() throws Exception {
        // Initialize the database
        twitterFollowerService.save(twitterFollower);

        // Search the twitterFollower
        restTwitterFollowerMockMvc.perform(get("/api/_search/twitter-followers?query=id:" + twitterFollower.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(twitterFollower.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.toString())))
            .andExpect(jsonPath("$.[*].accountAge").value(hasItem(DEFAULT_ACCOUNT_AGE)))
            .andExpect(jsonPath("$.[*].likes").value(hasItem(DEFAULT_LIKES)))
            .andExpect(jsonPath("$.[*].followers").value(hasItem(DEFAULT_FOLLOWERS)))
            .andExpect(jsonPath("$.[*].tweets").value(hasItem(DEFAULT_TWEETS)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].like").value(hasItem(DEFAULT_LIKE.booleanValue())))
            .andExpect(jsonPath("$.[*].follow").value(hasItem(DEFAULT_FOLLOW.booleanValue())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TwitterFollower.class);
        TwitterFollower twitterFollower1 = new TwitterFollower();
        twitterFollower1.setId(1L);
        TwitterFollower twitterFollower2 = new TwitterFollower();
        twitterFollower2.setId(twitterFollower1.getId());
        assertThat(twitterFollower1).isEqualTo(twitterFollower2);
        twitterFollower2.setId(2L);
        assertThat(twitterFollower1).isNotEqualTo(twitterFollower2);
        twitterFollower1.setId(null);
        assertThat(twitterFollower1).isNotEqualTo(twitterFollower2);
    }
}
