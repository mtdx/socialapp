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

import java.util.List;
import java.util.Optional;

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
