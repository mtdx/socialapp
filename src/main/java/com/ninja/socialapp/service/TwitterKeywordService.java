package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.TwitterKeyword;
import com.ninja.socialapp.domain.enumeration.KeywordStatus;
import com.ninja.socialapp.repository.TwitterKeywordRepository;
import com.ninja.socialapp.repository.search.TwitterKeywordSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TwitterKeyword.
 */
@Service
@Transactional
public class TwitterKeywordService {

    private final Logger log = LoggerFactory.getLogger(TwitterKeywordService.class);

    private final TwitterKeywordRepository twitterKeywordRepository;

    private final TwitterKeywordSearchRepository twitterKeywordSearchRepository;

    public TwitterKeywordService(TwitterKeywordRepository twitterKeywordRepository, TwitterKeywordSearchRepository twitterKeywordSearchRepository) {
        this.twitterKeywordRepository = twitterKeywordRepository;
        this.twitterKeywordSearchRepository = twitterKeywordSearchRepository;
    }

    /**
     * Save a twitterKeyword.
     *
     * @param twitterKeyword the entity to save
     * @return the persisted entity
     */
    public TwitterKeyword save(TwitterKeyword twitterKeyword) {
        log.debug("Request to save TwitterKeyword : {}", twitterKeyword);
        TwitterKeyword result = twitterKeywordRepository.save(twitterKeyword);
        twitterKeywordSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the twitterKeywords.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TwitterKeyword> findAll(Pageable pageable) {
        log.debug("Request to get all TwitterKeywords");
        return twitterKeywordRepository.findAll(pageable);
    }

    /**
     *  Get one twitterKeyword by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TwitterKeyword findOne(Long id) {
        log.debug("Request to get TwitterKeyword : {}", id);
        return twitterKeywordRepository.findOne(id);
    }

    /**
     *  Delete the  twitterKeyword by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TwitterKeyword : {}", id);
        twitterKeywordRepository.delete(id);
        twitterKeywordSearchRepository.delete(id);
    }

    /**
     * Search for the twitterKeyword corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TwitterKeyword> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TwitterKeywords for query {}", query);
        Page<TwitterKeyword> result = twitterKeywordSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    /**
     *  Get keyword by status.
     *
     *  @param status the of the entities
     *  @return the entities
     */
    @Transactional(readOnly = true)
    public Optional<TwitterKeyword> findFirstByStatusOrderByIdAsc(KeywordStatus status) {
        log.debug("Request to get keyword by status : {}", status);
        return twitterKeywordRepository.findFirstByStatusOrderByIdAsc(status);
    }

    /**
     *  Updates the number of competitors.
     *
     *  @param competitors the number of likes
     *  @param id the id of the entity
     */
    public void incrementCompetitors(Integer competitors, Long id) {
        twitterKeywordRepository.incrementCompetitors(competitors, id);
    }

    /**
     *  Finds a list of entities older than a certain date and a status
     *
     *  @param instant the current time
     *  @param status the keyword status
     *  @return a list of entities older than
     */
    @Transactional(readOnly = true)
    public List<TwitterKeyword> findOlderThanByStatus(Instant instant, KeywordStatus status) {
        log.debug("Call to get older than : {}", instant);
        return twitterKeywordRepository.findOlderThanByStatus(instant, status);
    }

    /**
     *  Resets a keyword
     *
     *  @param twitterKeyword the competitor to be reset
     */
    public void reset(TwitterKeyword twitterKeyword) {
        log.debug("Call to get reset a twitterKeyword : {}", twitterKeyword);
        twitterKeyword.setPage(1);
        twitterKeyword.setCompetitors(0);
        twitterKeyword.setStatus(KeywordStatus.IN_PROGRESS);
        twitterKeyword.setReset(false);
        twitterKeyword.setStop(false);
        twitterKeyword.setCreated(Instant.now());
    }

    /**
     *  Stops a keyword
     *
     *  @param twitterKeyword the competitor to be reset
     */
    public void stop(TwitterKeyword twitterKeyword) {
        log.debug("Call to stop a twitterKeyword : {}", twitterKeyword);
        twitterKeyword.setStatus(KeywordStatus.STOPPED);
        twitterKeyword.setStop(false);
        twitterKeyword.setReset(false);
    }
}
