package com.ninja.socialapp.web.rest;

import com.ninja.socialapp.SocialappApp;

import com.ninja.socialapp.domain.Proxy;
import com.ninja.socialapp.repository.ProxyRepository;
import com.ninja.socialapp.service.ProxyService;
import com.ninja.socialapp.repository.search.ProxySearchRepository;
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
 * Test class for the ProxyResource REST controller.
 *
 * @see ProxyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialappApp.class)
public class ProxyResourceIntTest {

    private static final String DEFAULT_HOST = "219.29.41.205";
    private static final String UPDATED_HOST = "12.112.154.208";

    private static final Integer DEFAULT_PORT = 0;
    private static final Integer UPDATED_PORT = 1;

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    @Autowired
    private ProxyRepository proxyRepository;

    @Autowired
    private ProxyService proxyService;

    @Autowired
    private ProxySearchRepository proxySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProxyMockMvc;

    private Proxy proxy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProxyResource proxyResource = new ProxyResource(proxyService);
        this.restProxyMockMvc = MockMvcBuilders.standaloneSetup(proxyResource)
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
    public static Proxy createEntity(EntityManager em) {
        Proxy proxy = new Proxy()
            .host(DEFAULT_HOST)
            .port(DEFAULT_PORT)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD);
        return proxy;
    }

    @Before
    public void initTest() {
        proxySearchRepository.deleteAll();
        proxy = createEntity(em);
    }

    @Test
    @Transactional
    public void createProxy() throws Exception {
        int databaseSizeBeforeCreate = proxyRepository.findAll().size();

        // Create the Proxy
        restProxyMockMvc.perform(post("/api/proxies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proxy)))
            .andExpect(status().isCreated());

        // Validate the Proxy in the database
        List<Proxy> proxyList = proxyRepository.findAll();
        assertThat(proxyList).hasSize(databaseSizeBeforeCreate + 1);
        Proxy testProxy = proxyList.get(proxyList.size() - 1);
        assertThat(testProxy.getHost()).isEqualTo(DEFAULT_HOST);
        assertThat(testProxy.getPort()).isEqualTo(DEFAULT_PORT);
        assertThat(testProxy.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testProxy.getPassword()).isEqualTo(DEFAULT_PASSWORD);

        // Validate the Proxy in Elasticsearch
        Proxy proxyEs = proxySearchRepository.findOne(testProxy.getId());
        assertThat(proxyEs).isEqualToComparingFieldByField(testProxy);
    }

    @Test
    @Transactional
    public void createProxyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = proxyRepository.findAll().size();

        // Create the Proxy with an existing ID
        proxy.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProxyMockMvc.perform(post("/api/proxies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proxy)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Proxy> proxyList = proxyRepository.findAll();
        assertThat(proxyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkHostIsRequired() throws Exception {
        int databaseSizeBeforeTest = proxyRepository.findAll().size();
        // set the field null
        proxy.setHost(null);

        // Create the Proxy, which fails.

        restProxyMockMvc.perform(post("/api/proxies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proxy)))
            .andExpect(status().isBadRequest());

        List<Proxy> proxyList = proxyRepository.findAll();
        assertThat(proxyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPortIsRequired() throws Exception {
        int databaseSizeBeforeTest = proxyRepository.findAll().size();
        // set the field null
        proxy.setPort(null);

        // Create the Proxy, which fails.

        restProxyMockMvc.perform(post("/api/proxies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proxy)))
            .andExpect(status().isBadRequest());

        List<Proxy> proxyList = proxyRepository.findAll();
        assertThat(proxyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProxies() throws Exception {
        // Initialize the database
        proxyRepository.saveAndFlush(proxy);

        // Get all the proxyList
        restProxyMockMvc.perform(get("/api/proxies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proxy.getId().intValue())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())));
    }

    @Test
    @Transactional
    public void getProxy() throws Exception {
        // Initialize the database
        proxyRepository.saveAndFlush(proxy);

        // Get the proxy
        restProxyMockMvc.perform(get("/api/proxies/{id}", proxy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(proxy.getId().intValue()))
            .andExpect(jsonPath("$.host").value(DEFAULT_HOST.toString()))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProxy() throws Exception {
        // Get the proxy
        restProxyMockMvc.perform(get("/api/proxies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProxy() throws Exception {
        // Initialize the database
        proxyService.save(proxy);

        int databaseSizeBeforeUpdate = proxyRepository.findAll().size();

        // Update the proxy
        Proxy updatedProxy = proxyRepository.findOne(proxy.getId());
        updatedProxy
            .host(UPDATED_HOST)
            .port(UPDATED_PORT)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD);

        restProxyMockMvc.perform(put("/api/proxies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProxy)))
            .andExpect(status().isOk());

        // Validate the Proxy in the database
        List<Proxy> proxyList = proxyRepository.findAll();
        assertThat(proxyList).hasSize(databaseSizeBeforeUpdate);
        Proxy testProxy = proxyList.get(proxyList.size() - 1);
        assertThat(testProxy.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testProxy.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testProxy.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testProxy.getPassword()).isEqualTo(UPDATED_PASSWORD);

        // Validate the Proxy in Elasticsearch
        Proxy proxyEs = proxySearchRepository.findOne(testProxy.getId());
        assertThat(proxyEs).isEqualToComparingFieldByField(testProxy);
    }

    @Test
    @Transactional
    public void updateNonExistingProxy() throws Exception {
        int databaseSizeBeforeUpdate = proxyRepository.findAll().size();

        // Create the Proxy

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProxyMockMvc.perform(put("/api/proxies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proxy)))
            .andExpect(status().isCreated());

        // Validate the Proxy in the database
        List<Proxy> proxyList = proxyRepository.findAll();
        assertThat(proxyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProxy() throws Exception {
        // Initialize the database
        proxyService.save(proxy);

        int databaseSizeBeforeDelete = proxyRepository.findAll().size();

        // Get the proxy
        restProxyMockMvc.perform(delete("/api/proxies/{id}", proxy.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean proxyExistsInEs = proxySearchRepository.exists(proxy.getId());
        assertThat(proxyExistsInEs).isFalse();

        // Validate the database is empty
        List<Proxy> proxyList = proxyRepository.findAll();
        assertThat(proxyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProxy() throws Exception {
        // Initialize the database
        proxyService.save(proxy);

        // Search the proxy
        restProxyMockMvc.perform(get("/api/_search/proxies?query=id:" + proxy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proxy.getId().intValue())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proxy.class);
        Proxy proxy1 = new Proxy();
        proxy1.setId(1L);
        Proxy proxy2 = new Proxy();
        proxy2.setId(proxy1.getId());
        assertThat(proxy1).isEqualTo(proxy2);
        proxy2.setId(2L);
        assertThat(proxy1).isNotEqualTo(proxy2);
        proxy1.setId(null);
        assertThat(proxy1).isNotEqualTo(proxy2);
    }
}
