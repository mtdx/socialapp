package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.TwitterAccount;
import com.ninja.socialapp.domain.TwitterError;
import com.ninja.socialapp.domain.enumeration.TwitterErrorType;
import com.ninja.socialapp.domain.enumeration.TwitterStatus;
import com.ninja.socialapp.repository.TwitterErrorRepository;
import com.ninja.socialapp.repository.search.TwitterErrorSearchRepository;
import com.ninja.socialapp.service.util.TwitterErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.TwitterException;


import java.time.Instant;
import java.util.List;

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

    private final TwitterAccountService twitterAccountService;

    public TwitterErrorService(TwitterErrorRepository twitterErrorRepository, TwitterErrorSearchRepository twitterErrorSearchRepository,
                               TwitterAccountService twitterAccountService) {
        this.twitterErrorRepository = twitterErrorRepository;
        this.twitterErrorSearchRepository = twitterErrorSearchRepository;
        this.twitterAccountService = twitterAccountService;
    }

    /**
     * Save a twitterError.
     *
     * @param twitterError the entity to save
     * @return the persisted entity
     */
    public TwitterError save(TwitterError twitterError) {
        log.debug("Request to save TwitterError : {}", twitterError);
        if(twitterError.getMessage() != null && twitterError.getMessage().length() >= 255){
            twitterError.setMessage(twitterError.getMessage().substring(0, 254));
        }
        if(twitterError.getErrorMessage() != null && twitterError.getErrorMessage().length() >= 255){
            twitterError.setErrorMessage(twitterError.getErrorMessage().substring(0, 254));
        }
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

    /**
     *  Finds a list of entities ids older than a certain date.
     *
     *  @param instant the current time
     *  @return a list of entity ids older than
     */
    @Transactional(readOnly = true)
    public List<Long> findOlderThan(Instant instant) {
        log.debug("Call to get older than : {}", instant);
        return twitterErrorRepository.findOlderThan(instant);
    }


    public void handleException(final TwitterException ex, final TwitterAccount twitterAccount, TwitterErrorType type) {
        TwitterError twitterError = new TwitterError();
        twitterError.setType(type);
        twitterError.setErrorCode(ex.getErrorCode());
        twitterError.setAccount(twitterAccount.getUsername());
        twitterError.setErrorMessage(ex.getErrorMessage());
        twitterError.setMessage(ex.getMessage());
        if(ex.getRateLimitStatus() != null) {
            twitterError.setRateLimitStatus(String.format("%d / %d",
                ex.getRateLimitStatus().getRemaining(), ex.getRateLimitStatus().getLimit()));
        }
        twitterError.setStatusCode(ex.getStatusCode());
        save(twitterError);
        saveAccount(ex.getErrorCode(), twitterAccount);
    }

    private void saveAccount(Integer errorCode, final TwitterAccount twitterAccount) {
        TwitterStatus status = twitterAccount.getStatus();
        if (TwitterErrorCode.authError(errorCode)) {
            twitterAccount.setStatus(TwitterStatus.AUTH_ERROR);
        }
        if (TwitterErrorCode.suspended(errorCode)) {
            twitterAccount.setStatus(TwitterStatus.SUSPENDED);
        }
        if (TwitterErrorCode.locked(errorCode)) {
            twitterAccount.setStatus(TwitterStatus.LOCKED);
        }
        if (twitterAccount.getStatus() != status) { // only save if we changed
            twitterAccountService.save(twitterAccount);
        }

    }
}
