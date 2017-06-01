package com.ninja.socialapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ninja.socialapp.domain.TwitterError;
import com.ninja.socialapp.service.TwitterErrorService;
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
 * REST controller for managing TwitterError.
 */
@RestController
@RequestMapping("/api")
public class TwitterErrorResource {

    private final Logger log = LoggerFactory.getLogger(TwitterErrorResource.class);

    private static final String ENTITY_NAME = "twitterError";

    private final TwitterErrorService twitterErrorService;

    public TwitterErrorResource(TwitterErrorService twitterErrorService) {
        this.twitterErrorService = twitterErrorService;
    }

    /**
     * GET  /twitter-errors : get all the twitterErrors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of twitterErrors in body
     */
    @GetMapping("/twitter-errors")
    @Timed
    public ResponseEntity<List<TwitterError>> getAllTwitterErrors(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TwitterErrors");
        Page<TwitterError> page = twitterErrorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/twitter-errors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /twitter-errors/:id : get the "id" twitterError.
     *
     * @param id the id of the twitterError to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the twitterError, or with status 404 (Not Found)
     */
    @GetMapping("/twitter-errors/{id}")
    @Timed
    public ResponseEntity<TwitterError> getTwitterError(@PathVariable Long id) {
        log.debug("REST request to get TwitterError : {}", id);
        TwitterError twitterError = twitterErrorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(twitterError));
    }

    /**
     * SEARCH  /_search/twitter-errors?query=:query : search for the twitterError corresponding
     * to the query.
     *
     * @param query the query of the twitterError search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/twitter-errors")
    @Timed
    public ResponseEntity<List<TwitterError>> searchTwitterErrors(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TwitterErrors for query {}", query);
        Page<TwitterError> page = twitterErrorService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/twitter-errors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
