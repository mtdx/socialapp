package com.ninja.socialapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ninja.socialapp.domain.RetweetAccount;
import com.ninja.socialapp.service.RetweetAccountService;
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
 * REST controller for managing RetweetAccount.
 */
@RestController
@RequestMapping("/api")
public class RetweetAccountResource {

    private final Logger log = LoggerFactory.getLogger(RetweetAccountResource.class);

    private static final String ENTITY_NAME = "retweetAccount";

    private final RetweetAccountService retweetAccountService;

    public RetweetAccountResource(RetweetAccountService retweetAccountService) {
        this.retweetAccountService = retweetAccountService;
    }

    /**
     * POST  /retweet-accounts : Create a new retweetAccount.
     *
     * @param retweetAccount the retweetAccount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new retweetAccount, or with status 400 (Bad Request) if the retweetAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/retweet-accounts")
    @Timed
    public ResponseEntity<RetweetAccount> createRetweetAccount(@Valid @RequestBody RetweetAccount retweetAccount) throws URISyntaxException {
        log.debug("REST request to save RetweetAccount : {}", retweetAccount);
        if (retweetAccount.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new retweetAccount cannot already have an ID")).body(null);
        }
        RetweetAccount result = retweetAccountService.save(retweetAccount);
        return ResponseEntity.created(new URI("/api/retweet-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /retweet-accounts : Updates an existing retweetAccount.
     *
     * @param retweetAccount the retweetAccount to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated retweetAccount,
     * or with status 400 (Bad Request) if the retweetAccount is not valid,
     * or with status 500 (Internal Server Error) if the retweetAccount couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/retweet-accounts")
    @Timed
    public ResponseEntity<RetweetAccount> updateRetweetAccount(@Valid @RequestBody RetweetAccount retweetAccount) throws URISyntaxException {
        log.debug("REST request to update RetweetAccount : {}", retweetAccount);
        if (retweetAccount.getId() == null) {
            return createRetweetAccount(retweetAccount);
        }
        RetweetAccount result = retweetAccountService.save(retweetAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, retweetAccount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /retweet-accounts : get all the retweetAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of retweetAccounts in body
     */
    @GetMapping("/retweet-accounts")
    @Timed
    public ResponseEntity<List<RetweetAccount>> getAllRetweetAccounts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of RetweetAccounts");
        Page<RetweetAccount> page = retweetAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/retweet-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /retweet-accounts/:id : get the "id" retweetAccount.
     *
     * @param id the id of the retweetAccount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the retweetAccount, or with status 404 (Not Found)
     */
    @GetMapping("/retweet-accounts/{id}")
    @Timed
    public ResponseEntity<RetweetAccount> getRetweetAccount(@PathVariable Long id) {
        log.debug("REST request to get RetweetAccount : {}", id);
        RetweetAccount retweetAccount = retweetAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(retweetAccount));
    }

    /**
     * DELETE  /retweet-accounts/:id : delete the "id" retweetAccount.
     *
     * @param id the id of the retweetAccount to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/retweet-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteRetweetAccount(@PathVariable Long id) {
        log.debug("REST request to delete RetweetAccount : {}", id);
        retweetAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/retweet-accounts?query=:query : search for the retweetAccount corresponding
     * to the query.
     *
     * @param query the query of the retweetAccount search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/retweet-accounts")
    @Timed
    public ResponseEntity<List<RetweetAccount>> searchRetweetAccounts(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of RetweetAccounts for query {}", query);
        Page<RetweetAccount> page = retweetAccountService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/retweet-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
