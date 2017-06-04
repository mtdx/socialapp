package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.TwitterFollower;
import com.ninja.socialapp.repository.TwitterFollowerRepository;
import com.ninja.socialapp.repository.search.TwitterFollowerSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TwitterFollower.
 */
@Service
@Transactional
public class TwitterFollowerService {

    private final Logger log = LoggerFactory.getLogger(TwitterFollowerService.class);

    private final TwitterFollowerRepository twitterFollowerRepository;

    private final TwitterFollowerSearchRepository twitterFollowerSearchRepository;

    public TwitterFollowerService(TwitterFollowerRepository twitterFollowerRepository, TwitterFollowerSearchRepository twitterFollowerSearchRepository) {
        this.twitterFollowerRepository = twitterFollowerRepository;
        this.twitterFollowerSearchRepository = twitterFollowerSearchRepository;
    }

    /**
     * Save a twitterFollower.
     *
     * @param twitterFollower the entity to save
     * @return the persisted entity
     */
    public TwitterFollower save(TwitterFollower twitterFollower) {
        log.debug("Request to save TwitterFollower : {}", twitterFollower);
        TwitterFollower result = twitterFollowerRepository.save(twitterFollower);
        twitterFollowerSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the twitterFollowers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TwitterFollower> findAll(Pageable pageable) {
        log.debug("Request to get all TwitterFollowers");
        return twitterFollowerRepository.findAll(pageable);
    }

    /**
     *  Get one twitterFollower by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TwitterFollower findOne(Long id) {
        log.debug("Request to get TwitterFollower : {}", id);
        return twitterFollowerRepository.findOne(id);
    }

    /**
     *  Delete the  twitterFollower by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TwitterFollower : {}", id);
        twitterFollowerRepository.delete(id);
        twitterFollowerSearchRepository.delete(id);
    }

    /**
     * Search for the twitterFollower corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TwitterFollower> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TwitterFollowers for query {}", query);
        Page<TwitterFollower> result = twitterFollowerSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
