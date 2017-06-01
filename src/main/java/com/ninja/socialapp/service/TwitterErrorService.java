package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.TwitterError;
import com.ninja.socialapp.repository.TwitterErrorRepository;
import com.ninja.socialapp.repository.search.TwitterErrorSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TwitterError.
 */
@Service
@Transactional
public class TwitterErrorService {

    private final Logger log = LoggerFactory.getLogger(TwitterErrorService.class);

    private final TwitterErrorRepository twitterErrorRepository;

    private final TwitterErrorSearchRepository twitterErrorSearchRepository;

    public TwitterErrorService(TwitterErrorRepository twitterErrorRepository, TwitterErrorSearchRepository twitterErrorSearchRepository) {
        this.twitterErrorRepository = twitterErrorRepository;
        this.twitterErrorSearchRepository = twitterErrorSearchRepository;
    }

    /**
     * Save a twitterError.
     *
     * @param twitterError the entity to save
     * @return the persisted entity
     */
    public TwitterError save(TwitterError twitterError) {
        log.debug("Request to save TwitterError : {}", twitterError);
        TwitterError result = twitterErrorRepository.save(twitterError);
        twitterErrorSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the twitterErrors.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TwitterError> findAll(Pageable pageable) {
        log.debug("Request to get all TwitterErrors");
        return twitterErrorRepository.findAll(pageable);
    }

    /**
     *  Get one twitterError by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TwitterError findOne(Long id) {
        log.debug("Request to get TwitterError : {}", id);
        return twitterErrorRepository.findOne(id);
    }

    /**
     *  Delete the  twitterError by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TwitterError : {}", id);
        twitterErrorRepository.delete(id);
        twitterErrorSearchRepository.delete(id);
    }

    /**
     * Search for the twitterError corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TwitterError> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TwitterErrors for query {}", query);
        Page<TwitterError> result = twitterErrorSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
