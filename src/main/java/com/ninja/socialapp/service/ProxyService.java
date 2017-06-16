package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.Proxy;
import com.ninja.socialapp.repository.ProxyRepository;
import com.ninja.socialapp.repository.search.ProxySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Proxy.
 */
@Service
@Transactional
public class ProxyService {

    private final Logger log = LoggerFactory.getLogger(ProxyService.class);

    private final ProxyRepository proxyRepository;

    private final ProxySearchRepository proxySearchRepository;

    public ProxyService(ProxyRepository proxyRepository, ProxySearchRepository proxySearchRepository) {
        this.proxyRepository = proxyRepository;
        this.proxySearchRepository = proxySearchRepository;
    }

    /**
     * Save a proxy.
     *
     * @param proxy the entity to save
     * @return the persisted entity
     */
    public Proxy save(Proxy proxy) {
        log.debug("Request to save Proxy : {}", proxy);
        Proxy result = proxyRepository.save(proxy);
        proxySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the proxies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Proxy> findAll(Pageable pageable) {
        log.debug("Request to get all Proxies");
        return proxyRepository.findAll(pageable);
    }

    /**
     *  Get one proxy by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Proxy findOne(Long id) {
        log.debug("Request to get Proxy : {}", id);
        return proxyRepository.findOne(id);
    }

    /**
     *  Delete the  proxy by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Proxy : {}", id);
        proxyRepository.delete(id);
        proxySearchRepository.delete(id);
    }

    /**
     * Search for the proxy corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Proxy> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Proxies for query {}", query);
        Page<Proxy> result = proxySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    /**
     *  Get all the proxies under a certain number of uses.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Proxy> findAllRestrict() {
        log.debug("Request to get all Proxies Restrict");
        final Long MAX_ACCOUNTS = 15L;
        return proxyRepository.findAllRestrict(MAX_ACCOUNTS);
    }
}
