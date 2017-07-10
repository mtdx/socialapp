package com.ninja.socialapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ninja.socialapp.domain.TwitterKeyword;
import com.ninja.socialapp.domain.enumeration.KeywordStatus;
import com.ninja.socialapp.service.TwitterKeywordService;
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

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TwitterKeyword.
 */
@RestController
@RequestMapping("/api")
public class TwitterKeywordResource {

    private final Logger log = LoggerFactory.getLogger(TwitterKeywordResource.class);

    private static final String ENTITY_NAME = "twitterKeyword";

    private final TwitterKeywordService twitterKeywordService;

    public TwitterKeywordResource(TwitterKeywordService twitterKeywordService) {
        this.twitterKeywordService = twitterKeywordService;
    }

    /**
     * POST  /twitter-keywords : Create a new twitterKeyword.
     *
     * @param twitterKeyword the twitterKeyword to create
     * @return the ResponseEntity with status 201 (Created) and with body the new twitterKeyword, or with status 400 (Bad Request) if the twitterKeyword has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/twitter-keywords")
    @Timed
    public ResponseEntity<TwitterKeyword> createTwitterKeyword(@Valid @RequestBody TwitterKeyword twitterKeyword) throws URISyntaxException {
        log.debug("REST request to save TwitterKeyword : {}", twitterKeyword);
        if (twitterKeyword.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new twitterKeyword cannot already have an ID")).body(null);
        }
        twitterKeyword.setStatus(KeywordStatus.IN_PROGRESS);
        twitterKeyword.setCompetitors(0);
        twitterKeyword.setPage(0);
        twitterKeyword.setCreated(Instant.now());
        twitterKeyword.setKeyword(twitterKeyword.getKeyword().toLowerCase().trim());
        TwitterKeyword result = twitterKeywordService.save(twitterKeyword);
        return ResponseEntity.created(new URI("/api/twitter-keywords/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /twitter-keywords : Updates an existing twitterKeyword.
     *
     * @param twitterKeyword the twitterKeyword to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated twitterKeyword,
     * or with status 400 (Bad Request) if the twitterKeyword is not valid,
     * or with status 500 (Internal Server Error) if the twitterKeyword couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/twitter-keywords")
    @Timed
    public ResponseEntity<TwitterKeyword> updateTwitterKeyword(@Valid @RequestBody TwitterKeyword twitterKeyword) throws URISyntaxException {
        log.debug("REST request to update TwitterKeyword : {}", twitterKeyword);
        if (twitterKeyword.getId() == null) {
            return createTwitterKeyword(twitterKeyword);
        }
        if(twitterKeyword.isStop()) {
            twitterKeyword.setStatus(KeywordStatus.STOPPED);
            twitterKeyword.setStop(false);
        }
        if(twitterKeyword.isReset()) {
            twitterKeyword.setPage(0);
            twitterKeyword.setCompetitors(0);
            twitterKeyword.setStatus(KeywordStatus.IN_PROGRESS);
            twitterKeyword.setReset(false);
        }
        twitterKeyword.setKeyword(twitterKeyword.getKeyword().toLowerCase().trim());
        TwitterKeyword result = twitterKeywordService.save(twitterKeyword);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, twitterKeyword.getId().toString()))
            .body(result);
    }

    /**
     * GET  /twitter-keywords : get all the twitterKeywords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of twitterKeywords in body
     */
    @GetMapping("/twitter-keywords")
    @Timed
    public ResponseEntity<List<TwitterKeyword>> getAllTwitterKeywords(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TwitterKeywords");
        Page<TwitterKeyword> page = twitterKeywordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/twitter-keywords");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /twitter-keywords/:id : get the "id" twitterKeyword.
     *
     * @param id the id of the twitterKeyword to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the twitterKeyword, or with status 404 (Not Found)
     */
    @GetMapping("/twitter-keywords/{id}")
    @Timed
    public ResponseEntity<TwitterKeyword> getTwitterKeyword(@PathVariable Long id) {
        log.debug("REST request to get TwitterKeyword : {}", id);
        TwitterKeyword twitterKeyword = twitterKeywordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(twitterKeyword));
    }

    /**
     * DELETE  /twitter-keywords/:id : delete the "id" twitterKeyword.
     *
     * @param id the id of the twitterKeyword to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/twitter-keywords/{id}")
    @Timed
    public ResponseEntity<Void> deleteTwitterKeyword(@PathVariable Long id) {
        log.debug("REST request to delete TwitterKeyword : {}", id);
        twitterKeywordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/twitter-keywords?query=:query : search for the twitterKeyword corresponding
     * to the query.
     *
     * @param query the query of the twitterKeyword search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/twitter-keywords")
    @Timed
    public ResponseEntity<List<TwitterKeyword>> searchTwitterKeywords(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TwitterKeywords for query {}", query);
        Page<TwitterKeyword> page = twitterKeywordService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/twitter-keywords");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
