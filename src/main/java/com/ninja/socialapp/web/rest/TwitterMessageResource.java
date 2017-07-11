package com.ninja.socialapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ninja.socialapp.domain.TwitterMessage;
import com.ninja.socialapp.service.TwitterMessageService;
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
 * REST controller for managing TwitterMessage.
 */
@RestController
@RequestMapping("/api")
public class TwitterMessageResource {

    private final Logger log = LoggerFactory.getLogger(TwitterMessageResource.class);

    private static final String ENTITY_NAME = "twitterMessage";

    private final TwitterMessageService twitterMessageService;

    public TwitterMessageResource(TwitterMessageService twitterMessageService) {
        this.twitterMessageService = twitterMessageService;
    }

    /**
     * POST  /twitter-messages : Create a new twitterMessage.
     *
     * @param twitterMessage the twitterMessage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new twitterMessage, or with status 400 (Bad Request) if the twitterMessage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/twitter-messages")
    @Timed
    public ResponseEntity<TwitterMessage> createTwitterMessage(@Valid @RequestBody TwitterMessage twitterMessage) throws URISyntaxException {
        log.debug("REST request to save TwitterMessage : {}", twitterMessage);
        if (twitterMessage.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new twitterMessage cannot already have an ID")).body(null);
        }
        TwitterMessage result = twitterMessageService.save(twitterMessage);
        return ResponseEntity.created(new URI("/api/twitter-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /twitter-messages : Updates an existing twitterMessage.
     *
     * @param twitterMessage the twitterMessage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated twitterMessage,
     * or with status 400 (Bad Request) if the twitterMessage is not valid,
     * or with status 500 (Internal Server Error) if the twitterMessage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/twitter-messages")
    @Timed
    public ResponseEntity<TwitterMessage> updateTwitterMessage(@Valid @RequestBody TwitterMessage twitterMessage) throws URISyntaxException {
        log.debug("REST request to update TwitterMessage : {}", twitterMessage);
        if (twitterMessage.getId() == null) {
            return createTwitterMessage(twitterMessage);
        }
        TwitterMessage result = twitterMessageService.save(twitterMessage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, twitterMessage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /twitter-messages : get all the twitterMessages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of twitterMessages in body
     */
    @GetMapping("/twitter-messages")
    @Timed
    public ResponseEntity<List<TwitterMessage>> getAllTwitterMessages(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TwitterMessages");
        Page<TwitterMessage> page = twitterMessageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/twitter-messages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /twitter-messages/:id : get the "id" twitterMessage.
     *
     * @param id the id of the twitterMessage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the twitterMessage, or with status 404 (Not Found)
     */
    @GetMapping("/twitter-messages/{id}")
    @Timed
    public ResponseEntity<TwitterMessage> getTwitterMessage(@PathVariable Long id) {
        log.debug("REST request to get TwitterMessage : {}", id);
        TwitterMessage twitterMessage = twitterMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(twitterMessage));
    }

    /**
     * DELETE  /twitter-messages/:id : delete the "id" twitterMessage.
     *
     * @param id the id of the twitterMessage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/twitter-messages/{id}")
    @Timed
    public ResponseEntity<Void> deleteTwitterMessage(@PathVariable Long id) {
        log.debug("REST request to delete TwitterMessage : {}", id);
        twitterMessageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/twitter-messages?query=:query : search for the twitterMessage corresponding
     * to the query.
     *
     * @param query the query of the twitterMessage search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/twitter-messages")
    @Timed
    public ResponseEntity<List<TwitterMessage>> searchTwitterMessages(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TwitterMessages for query {}", query);
        Page<TwitterMessage> page = twitterMessageService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/twitter-messages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
