package com.ninja.socialapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ninja.socialapp.domain.TwitterFollower;
import com.ninja.socialapp.service.TwitterFollowerService;
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
 * REST controller for managing TwitterFollower.
 */
@RestController
@RequestMapping("/api")
public class TwitterFollowerResource {

    private final Logger log = LoggerFactory.getLogger(TwitterFollowerResource.class);

    private static final String ENTITY_NAME = "twitterFollower";

    private final TwitterFollowerService twitterFollowerService;

    public TwitterFollowerResource(TwitterFollowerService twitterFollowerService) {
        this.twitterFollowerService = twitterFollowerService;
    }

    /**
     * POST  /twitter-followers : Create a new twitterFollower.
     *
     * @param twitterFollower the twitterFollower to create
     * @return the ResponseEntity with status 201 (Created) and with body the new twitterFollower, or with status 400 (Bad Request) if the twitterFollower has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/twitter-followers")
    @Timed
    public ResponseEntity<TwitterFollower> createTwitterFollower(@Valid @RequestBody TwitterFollower twitterFollower) throws URISyntaxException {
        log.debug("REST request to save TwitterFollower : {}", twitterFollower);
        if (twitterFollower.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new twitterFollower cannot already have an ID")).body(null);
        }
        TwitterFollower result = twitterFollowerService.save(twitterFollower);
        return ResponseEntity.created(new URI("/api/twitter-followers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /twitter-followers : Updates an existing twitterFollower.
     *
     * @param twitterFollower the twitterFollower to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated twitterFollower,
     * or with status 400 (Bad Request) if the twitterFollower is not valid,
     * or with status 500 (Internal Server Error) if the twitterFollower couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/twitter-followers")
    @Timed
    public ResponseEntity<TwitterFollower> updateTwitterFollower(@Valid @RequestBody TwitterFollower twitterFollower) throws URISyntaxException {
        log.debug("REST request to update TwitterFollower : {}", twitterFollower);
        if (twitterFollower.getId() == null) {
            return createTwitterFollower(twitterFollower);
        }
        TwitterFollower result = twitterFollowerService.save(twitterFollower);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, twitterFollower.getId().toString()))
            .body(result);
    }

    /**
     * GET  /twitter-followers : get all the twitterFollowers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of twitterFollowers in body
     */
    @GetMapping("/twitter-followers")
    @Timed
    public ResponseEntity<List<TwitterFollower>> getAllTwitterFollowers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TwitterFollowers");
        Page<TwitterFollower> page = twitterFollowerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/twitter-followers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /twitter-followers/:id : get the "id" twitterFollower.
     *
     * @param id the id of the twitterFollower to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the twitterFollower, or with status 404 (Not Found)
     */
    @GetMapping("/twitter-followers/{id}")
    @Timed
    public ResponseEntity<TwitterFollower> getTwitterFollower(@PathVariable Long id) {
        log.debug("REST request to get TwitterFollower : {}", id);
        TwitterFollower twitterFollower = twitterFollowerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(twitterFollower));
    }

    /**
     * DELETE  /twitter-followers/:id : delete the "id" twitterFollower.
     *
     * @param id the id of the twitterFollower to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/twitter-followers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTwitterFollower(@PathVariable Long id) {
        log.debug("REST request to delete TwitterFollower : {}", id);
        twitterFollowerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/twitter-followers?query=:query : search for the twitterFollower corresponding
     * to the query.
     *
     * @param query the query of the twitterFollower search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/twitter-followers")
    @Timed
    public ResponseEntity<List<TwitterFollower>> searchTwitterFollowers(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TwitterFollowers for query {}", query);
        Page<TwitterFollower> page = twitterFollowerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/twitter-followers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
