package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.Header;
import com.ninja.socialapp.domain.TwitterAccount;
import com.ninja.socialapp.domain.enumeration.TwitterStatus;
import com.ninja.socialapp.repository.HeaderRepository;
import com.ninja.socialapp.repository.search.HeaderSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Header.
 */
@Service
@Transactional
public class HeaderService {

    private final Logger log = LoggerFactory.getLogger(HeaderService.class);

    private final HeaderRepository headerRepository;

    private final HeaderSearchRepository headerSearchRepository;

    private final TwitterAccountService twitterAccountService;

    public HeaderService(HeaderRepository headerRepository, HeaderSearchRepository headerSearchRepository, TwitterAccountService twitterAccountService) {
        this.headerRepository = headerRepository;
        this.headerSearchRepository = headerSearchRepository;
        this.twitterAccountService = twitterAccountService;
    }

    /**
     * Save a header.
     *
     * @param header the entity to save
     * @return the persisted entity
     */
    public Header save(Header header) {
        log.debug("Request to save Header : {}", header);
        Header result = headerRepository.save(header);
        headerSearchRepository.save(result);
        twitterAccountService.switchToPendingUpdate(twitterAccountService.findAllByHeader(header));
        return result;
    }

    /**
     *  Get all the headers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Header> findAll(Pageable pageable) {
        log.debug("Request to get all Headers");
        return headerRepository.findAll(pageable);
    }

    /**
     *  Get one header by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Header findOne(Long id) {
        log.debug("Request to get Header : {}", id);
        return headerRepository.findOne(id);
    }

    /**
     *  Delete the  header by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Header : {}", id);
        headerRepository.delete(id);
        headerSearchRepository.delete(id);
    }

    /**
     * Search for the header corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Header> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Headers for query {}", query);
        Page<Header> result = headerSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
