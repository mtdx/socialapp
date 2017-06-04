package com.ninja.socialapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ninja.socialapp.domain.Competitor;
import com.ninja.socialapp.domain.enumeration.CompetitorStatus;
import com.ninja.socialapp.service.CompetitorService;
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
 * REST controller for managing Competitor.
 */
@RestController
@RequestMapping("/api")
public class CompetitorResource {

    private final Logger log = LoggerFactory.getLogger(CompetitorResource.class);

    private static final String ENTITY_NAME = "competitor";

    private final CompetitorService competitorService;

    public CompetitorResource(CompetitorService competitorService) {
        this.competitorService = competitorService;
    }

    /**
     * POST  /competitors : Create a new competitor.
     *
     * @param competitor the competitor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new competitor, or with status 400 (Bad Request) if the competitor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/competitors")
    @Timed
    public ResponseEntity<Competitor> createCompetitor(@Valid @RequestBody Competitor competitor) throws URISyntaxException {
        log.debug("REST request to save Competitor : {}", competitor);
        if (competitor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new competitor cannot already have an ID")).body(null);
        }
        competitor.setCursor(-1L); // default cursor
        competitor.setStatus(CompetitorStatus.IN_PROGRESS); // default status
        Competitor result = competitorService.save(competitor);
        return ResponseEntity.created(new URI("/api/competitors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /competitors : Updates an existing competitor.
     *
     * @param competitor the competitor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated competitor,
     * or with status 400 (Bad Request) if the competitor is not valid,
     * or with status 500 (Internal Server Error) if the competitor couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/competitors")
    @Timed
    public ResponseEntity<Competitor> updateCompetitor(@Valid @RequestBody Competitor competitor) throws URISyntaxException {
        log.debug("REST request to update Competitor : {}", competitor);
        if (competitor.getId() == null) {
            return createCompetitor(competitor);
        }
        Competitor result = competitorService.save(competitor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, competitor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /competitors : get all the competitors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of competitors in body
     */
    @GetMapping("/competitors")
    @Timed
    public ResponseEntity<List<Competitor>> getAllCompetitors(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Competitors");
        Page<Competitor> page = competitorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/competitors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /competitors/:id : get the "id" competitor.
     *
     * @param id the id of the competitor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the competitor, or with status 404 (Not Found)
     */
    @GetMapping("/competitors/{id}")
    @Timed
    public ResponseEntity<Competitor> getCompetitor(@PathVariable Long id) {
        log.debug("REST request to get Competitor : {}", id);
        Competitor competitor = competitorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(competitor));
    }

    /**
     * DELETE  /competitors/:id : delete the "id" competitor.
     *
     * @param id the id of the competitor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/competitors/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompetitor(@PathVariable Long id) {
        log.debug("REST request to delete Competitor : {}", id);
        competitorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/competitors?query=:query : search for the competitor corresponding
     * to the query.
     *
     * @param query the query of the competitor search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/competitors")
    @Timed
    public ResponseEntity<List<Competitor>> searchCompetitors(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Competitors for query {}", query);
        Page<Competitor> page = competitorService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/competitors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
