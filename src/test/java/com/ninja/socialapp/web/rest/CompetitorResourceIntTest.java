package com.ninja.socialapp.web.rest;

import com.ninja.socialapp.SocialappApp;

import com.ninja.socialapp.domain.Competitor;
import com.ninja.socialapp.repository.CompetitorRepository;
import com.ninja.socialapp.service.CompetitorService;
import com.ninja.socialapp.repository.search.CompetitorSearchRepository;
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

import com.ninja.socialapp.domain.enumeration.CompetitorStatus;
/**
 * Test class for the CompetitorResource REST controller.
 *
 * @see CompetitorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialappApp.class)
public class CompetitorResourceIntTest {

    private static final CompetitorStatus DEFAULT_STATUS = CompetitorStatus.IN_PROGRESS;
    private static final CompetitorStatus UPDATED_STATUS = CompetitorStatus.DONE;

    private static final String DEFAULT_USERID = "25";
    private static final String UPDATED_USERID = "15";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final Long DEFAULT_CURSOR = -1L;
    private static final Long UPDATED_CURSOR = 2L;

    @Autowired
    private CompetitorRepository competitorRepository;

    @Autowired
    private CompetitorService competitorService;

    @Autowired
    private CompetitorSearchRepository competitorSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompetitorMockMvc;

    private Competitor competitor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompetitorResource competitorResource = new CompetitorResource(competitorService);
        this.restCompetitorMockMvc = MockMvcBuilders.standaloneSetup(competitorResource)
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
    public static Competitor createEntity(EntityManager em) {
        Competitor competitor = new Competitor()
            .status(DEFAULT_STATUS)
            .userid(DEFAULT_USERID)
            .username(DEFAULT_USERNAME)
            .cursor(DEFAULT_CURSOR);
        return competitor;
    }

    @Before
    public void initTest() {
        competitorSearchRepository.deleteAll();
        competitor = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompetitor() throws Exception {
        int databaseSizeBeforeCreate = competitorRepository.findAll().size();

        // Create the Competitor
        restCompetitorMockMvc.perform(post("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitor)))
            .andExpect(status().isCreated());

        // Validate the Competitor in the database
        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeCreate + 1);
        Competitor testCompetitor = competitorList.get(competitorList.size() - 1);
        assertThat(testCompetitor.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCompetitor.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testCompetitor.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testCompetitor.getCursor()).isEqualTo(DEFAULT_CURSOR);

        // Validate the Competitor in Elasticsearch
        Competitor competitorEs = competitorSearchRepository.findOne(testCompetitor.getId());
        assertThat(competitorEs).isEqualToComparingFieldByField(testCompetitor);
    }

    @Test
    @Transactional
    public void createCompetitorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = competitorRepository.findAll().size();

        // Create the Competitor with an existing ID
        competitor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompetitorMockMvc.perform(post("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitor)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUseridIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitorRepository.findAll().size();
        // set the field null
        competitor.setUserid(null);

        // Create the Competitor, which fails.

        restCompetitorMockMvc.perform(post("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitor)))
            .andExpect(status().isBadRequest());

        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitorRepository.findAll().size();
        // set the field null
        competitor.setUsername(null);

        // Create the Competitor, which fails.

        restCompetitorMockMvc.perform(post("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitor)))
            .andExpect(status().isBadRequest());

        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompetitors() throws Exception {
        // Initialize the database
        competitorRepository.saveAndFlush(competitor);

        // Get all the competitorList
        restCompetitorMockMvc.perform(get("/api/competitors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competitor.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].cursor").value(hasItem(DEFAULT_CURSOR.intValue())));
    }

    @Test
    @Transactional
    public void getCompetitor() throws Exception {
        // Initialize the database
        competitorRepository.saveAndFlush(competitor);

        // Get the competitor
        restCompetitorMockMvc.perform(get("/api/competitors/{id}", competitor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(competitor.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.cursor").value(DEFAULT_CURSOR.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCompetitor() throws Exception {
        // Get the competitor
        restCompetitorMockMvc.perform(get("/api/competitors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompetitor() throws Exception {
        // Initialize the database
        competitorService.save(competitor);

        int databaseSizeBeforeUpdate = competitorRepository.findAll().size();

        // Update the competitor
        Competitor updatedCompetitor = competitorRepository.findOne(competitor.getId());
        updatedCompetitor
            .status(UPDATED_STATUS)
            .userid(UPDATED_USERID)
            .username(UPDATED_USERNAME)
            .cursor(UPDATED_CURSOR);

        restCompetitorMockMvc.perform(put("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompetitor)))
            .andExpect(status().isOk());

        // Validate the Competitor in the database
        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeUpdate);
        Competitor testCompetitor = competitorList.get(competitorList.size() - 1);
        assertThat(testCompetitor.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCompetitor.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testCompetitor.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testCompetitor.getCursor()).isEqualTo(UPDATED_CURSOR);

        // Validate the Competitor in Elasticsearch
        Competitor competitorEs = competitorSearchRepository.findOne(testCompetitor.getId());
        assertThat(competitorEs).isEqualToComparingFieldByField(testCompetitor);
    }

    @Test
    @Transactional
    public void updateNonExistingCompetitor() throws Exception {
        int databaseSizeBeforeUpdate = competitorRepository.findAll().size();

        // Create the Competitor

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompetitorMockMvc.perform(put("/api/competitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitor)))
            .andExpect(status().isCreated());

        // Validate the Competitor in the database
        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCompetitor() throws Exception {
        // Initialize the database
        competitorService.save(competitor);

        int databaseSizeBeforeDelete = competitorRepository.findAll().size();

        // Get the competitor
        restCompetitorMockMvc.perform(delete("/api/competitors/{id}", competitor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean competitorExistsInEs = competitorSearchRepository.exists(competitor.getId());
        assertThat(competitorExistsInEs).isFalse();

        // Validate the database is empty
        List<Competitor> competitorList = competitorRepository.findAll();
        assertThat(competitorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompetitor() throws Exception {
        // Initialize the database
        competitorService.save(competitor);

        // Search the competitor
        restCompetitorMockMvc.perform(get("/api/_search/competitors?query=id:" + competitor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competitor.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].cursor").value(hasItem(DEFAULT_CURSOR.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Competitor.class);
        Competitor competitor1 = new Competitor();
        competitor1.setId(1L);
        Competitor competitor2 = new Competitor();
        competitor2.setId(competitor1.getId());
        assertThat(competitor1).isEqualTo(competitor2);
        competitor2.setId(2L);
        assertThat(competitor1).isNotEqualTo(competitor2);
        competitor1.setId(null);
        assertThat(competitor1).isNotEqualTo(competitor2);
    }
}
