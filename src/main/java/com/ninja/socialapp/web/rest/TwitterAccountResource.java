package com.ninja.socialapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ninja.socialapp.domain.TwitterAccount;
import com.ninja.socialapp.domain.enumeration.TwitterStatus;
import com.ninja.socialapp.service.TwitterAccountService;
import com.ninja.socialapp.web.rest.util.HeaderUtil;
import com.ninja.socialapp.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TwitterAccount.
 */
@RestController
@RequestMapping("/api")
public class TwitterAccountResource {

    private final Logger log = LoggerFactory.getLogger(TwitterAccountResource.class);

    private static final String ENTITY_NAME = "twitterAccount";

    private final TwitterAccountService twitterAccountService;

    public TwitterAccountResource(TwitterAccountService twitterAccountService) {
        this.twitterAccountService = twitterAccountService;
    }

    /**
     * POST  /twitter-accounts : Create a new twitterAccount.
     *
     * @param twitterAccount the twitterAccount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new twitterAccount, or with status 400 (Bad Request) if the twitterAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/twitter-accounts")
    @Timed
    public ResponseEntity<TwitterAccount> createTwitterAccount(@Valid @RequestBody TwitterAccount twitterAccount) throws URISyntaxException {
        log.debug("REST request to save TwitterAccount : {}", twitterAccount);
        if (twitterAccount.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new twitterAccount cannot already have an ID")).body(null);
        }
        twitterAccount.setPrevStatus(TwitterStatus.PENDING_UPDATE); // default status
        twitterAccount.setStatus(TwitterStatus.PENDING_UPDATE); // default status
        TwitterAccount result = twitterAccountService.save(twitterAccount);
        return ResponseEntity.created(new URI("/api/twitter-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /twitter-accounts : Updates an existing twitterAccount.
     *
     * @param twitterAccount the twitterAccount to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated twitterAccount,
     * or with status 400 (Bad Request) if the twitterAccount is not valid,
     * or with status 500 (Internal Server Error) if the twitterAccount couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/twitter-accounts")
    @Timed
    public ResponseEntity<TwitterAccount> updateTwitterAccount(@Valid @RequestBody TwitterAccount twitterAccount) throws URISyntaxException {
        log.debug("REST request to update TwitterAccount : {}", twitterAccount);
        if (twitterAccount.getId() == null) {
            return createTwitterAccount(twitterAccount);
        }
        twitterAccount.setPrevStatus(twitterAccount.getStatus());
        twitterAccount.setStatus(TwitterStatus.PENDING_UPDATE);
        TwitterAccount result = twitterAccountService.save(twitterAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, twitterAccount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /twitter-accounts : get all the twitterAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of twitterAccounts in body
     */
    @GetMapping("/twitter-accounts")
    @Timed
    public ResponseEntity<List<TwitterAccount>> getAllTwitterAccounts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TwitterAccounts");
        Page<TwitterAccount> page = twitterAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/twitter-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /twitter-accounts/:id : get the "id" twitterAccount.
     *
     * @param id the id of the twitterAccount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the twitterAccount, or with status 404 (Not Found)
     */
    @GetMapping("/twitter-accounts/{id}")
    @Timed
    public ResponseEntity<TwitterAccount> getTwitterAccount(@PathVariable Long id) {
        log.debug("REST request to get TwitterAccount : {}", id);
        TwitterAccount twitterAccount = twitterAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(twitterAccount));
    }

    /**
     * DELETE  /twitter-accounts/:id : delete the "id" twitterAccount.
     *
     * @param id the id of the twitterAccount to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/twitter-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteTwitterAccount(@PathVariable Long id) {
        log.debug("REST request to delete TwitterAccount : {}", id);
        twitterAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/twitter-accounts?query=:query : search for the twitterAccount corresponding
     * to the query.
     *
     * @param query the query of the twitterAccount search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/twitter-accounts")
    @Timed
    public ResponseEntity<List<TwitterAccount>> searchTwitterAccounts(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TwitterAccounts for query {}", query);
        Page<TwitterAccount> page = twitterAccountService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/twitter-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
