package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.TwitterAccount;
import com.ninja.socialapp.domain.TwitterMessage;
import com.ninja.socialapp.domain.enumeration.TwitterStatus;
import com.ninja.socialapp.repository.TwitterMessageRepository;
import com.ninja.socialapp.repository.search.TwitterMessageSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TwitterMessage.
 */
@Service
@Transactional
public class TwitterMessageService {

    private final Logger log = LoggerFactory.getLogger(TwitterMessageService.class);

    private final TwitterMessageRepository twitterMessageRepository;

    private final TwitterMessageSearchRepository twitterMessageSearchRepository;

    private final TwitterAccountService twitterAccountService;

    public TwitterMessageService(TwitterMessageRepository twitterMessageRepository, TwitterMessageSearchRepository twitterMessageSearchRepository,
                                 TwitterAccountService twitterAccountService) {
        this.twitterMessageRepository = twitterMessageRepository;
        this.twitterMessageSearchRepository = twitterMessageSearchRepository;
        this.twitterAccountService = twitterAccountService;
    }

    /**
     * Save a twitterMessage.
     *
     * @param twitterMessage the entity to save
     * @return the persisted entity
     */
    public TwitterMessage save(TwitterMessage twitterMessage) {
        log.debug("Request to save TwitterMessage : {}", twitterMessage);
        TwitterMessage result = twitterMessageRepository.save(twitterMessage);
        twitterMessageSearchRepository.save(result);
        twitterAccountService.switchToPendingUpdate(twitterAccountService.findAllByMessage(twitterMessage));
        return result;
    }

    /**
     *  Get all the twitterMessages.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TwitterMessage> findAll(Pageable pageable) {
        log.debug("Request to get all TwitterMessages");
        return twitterMessageRepository.findAll(pageable);
    }

    /**
     *  Get one twitterMessage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TwitterMessage findOne(Long id) {
        log.debug("Request to get TwitterMessage : {}", id);
        return twitterMessageRepository.findOne(id);
    }

    /**
     *  Delete the  twitterMessage by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TwitterMessage : {}", id);
        twitterMessageRepository.delete(id);
        twitterMessageSearchRepository.delete(id);
    }

    /**
     * Search for the twitterMessage corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TwitterMessage> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TwitterMessages for query {}", query);
        Page<TwitterMessage> result = twitterMessageSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
