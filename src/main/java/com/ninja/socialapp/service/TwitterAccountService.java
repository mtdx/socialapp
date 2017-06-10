package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.TwitterAccount;
import com.ninja.socialapp.repository.TwitterAccountRepository;
import com.ninja.socialapp.repository.search.TwitterAccountSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TwitterAccount.
 */
@Service
@Transactional
public class TwitterAccountService {

    private final Logger log = LoggerFactory.getLogger(TwitterAccountService.class);

    private final TwitterAccountRepository twitterAccountRepository;

    private final TwitterAccountSearchRepository twitterAccountSearchRepository;

    public TwitterAccountService(TwitterAccountRepository twitterAccountRepository, TwitterAccountSearchRepository twitterAccountSearchRepository) {
        this.twitterAccountRepository = twitterAccountRepository;
        this.twitterAccountSearchRepository = twitterAccountSearchRepository;
    }

    /**
     * Save a twitterAccount.
     *
     * @param twitterAccount the entity to save
     * @return the persisted entity
     */
    public TwitterAccount save(TwitterAccount twitterAccount) {
        log.debug("Request to save TwitterAccount : {}", twitterAccount);
        TwitterAccount result = twitterAccountRepository.save(twitterAccount);
        twitterAccountSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the twitterAccounts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TwitterAccount> findAll(Pageable pageable) {
        log.debug("Request to get all TwitterAccounts");
        return twitterAccountRepository.findAll(pageable);
    }

    /**
     *  Get one twitterAccount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TwitterAccount findOne(Long id) {
        log.debug("Request to get TwitterAccount : {}", id);
        return twitterAccountRepository.findOne(id);
    }

    /**
     *  Delete the  twitterAccount by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TwitterAccount : {}", id);
        twitterAccountRepository.delete(id);
        twitterAccountSearchRepository.delete(id);
    }

    /**
     * Search for the twitterAccount corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TwitterAccount> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TwitterAccounts for query {}", query);
        Page<TwitterAccount> result = twitterAccountSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
