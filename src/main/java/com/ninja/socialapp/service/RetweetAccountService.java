package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.RetweetAccount;
import com.ninja.socialapp.domain.enumeration.RetweetAccountStatus;
import com.ninja.socialapp.repository.RetweetAccountRepository;
import com.ninja.socialapp.repository.search.RetweetAccountSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing RetweetAccount.
 */
@Service
@Transactional
public class RetweetAccountService {

    private final Logger log = LoggerFactory.getLogger(RetweetAccountService.class);

    private final RetweetAccountRepository retweetAccountRepository;

    private final RetweetAccountSearchRepository retweetAccountSearchRepository;

    private final TwitterAccountService twitterAccountService;

    public RetweetAccountService(RetweetAccountRepository retweetAccountRepository, RetweetAccountSearchRepository retweetAccountSearchRepository,
                                 TwitterAccountService twitterAccountService) {
        this.retweetAccountRepository = retweetAccountRepository;
        this.retweetAccountSearchRepository = retweetAccountSearchRepository;
        this.twitterAccountService = twitterAccountService;
    }

    /**
     * Save a retweetAccount.
     *
     * @param retweetAccount the entity to save
     * @return the persisted entity
     */
    public RetweetAccount save(RetweetAccount retweetAccount) {
        log.debug("Request to save RetweetAccount : {}", retweetAccount);
        RetweetAccount result = retweetAccountRepository.save(retweetAccount);
        retweetAccountSearchRepository.save(result);
        if (retweetAccount.getStatus() != RetweetAccountStatus.STOPPED)
            twitterAccountService.switchToPendingUpdate(twitterAccountService.findAllByRetweetAccount(retweetAccount));
        return result;
    }

    /**
     *  Get all the retweetAccounts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RetweetAccount> findAll(Pageable pageable) {
        log.debug("Request to get all RetweetAccounts");
        return retweetAccountRepository.findAll(pageable);
    }

    /**
     *  Get one retweetAccount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public RetweetAccount findOne(Long id) {
        log.debug("Request to get RetweetAccount : {}", id);
        return retweetAccountRepository.findOne(id);
    }

    /**
     *  Delete the  retweetAccount by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RetweetAccount : {}", id);
        retweetAccountRepository.delete(id);
        retweetAccountSearchRepository.delete(id);
    }

    /**
     * Search for the retweetAccount corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RetweetAccount> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RetweetAccounts for query {}", query);
        Page<RetweetAccount> result = retweetAccountSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
