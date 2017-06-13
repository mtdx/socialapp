package com.ninja.socialapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ninja.socialapp.domain.Header;
import com.ninja.socialapp.service.HeaderService;
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
 * REST controller for managing Header.
 */
@RestController
@RequestMapping("/api")
public class HeaderResource {

    private final Logger log = LoggerFactory.getLogger(HeaderResource.class);

    private static final String ENTITY_NAME = "header";

    private final HeaderService headerService;

    public HeaderResource(HeaderService headerService) {
        this.headerService = headerService;
    }

    /**
     * POST  /headers : Create a new header.
     *
     * @param header the header to create
     * @return the ResponseEntity with status 201 (Created) and with body the new header, or with status 400 (Bad Request) if the header has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/headers")
    @Timed
    public ResponseEntity<Header> createHeader(@Valid @RequestBody Header header) throws URISyntaxException {
        log.debug("REST request to save Header : {}", header);
        if (header.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new header cannot already have an ID")).body(null);
        }
        Header result = headerService.save(header);
        return ResponseEntity.created(new URI("/api/headers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /headers : Updates an existing header.
     *
     * @param header the header to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated header,
     * or with status 400 (Bad Request) if the header is not valid,
     * or with status 500 (Internal Server Error) if the header couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/headers")
    @Timed
    public ResponseEntity<Header> updateHeader(@Valid @RequestBody Header header) throws URISyntaxException {
        log.debug("REST request to update Header : {}", header);
        if (header.getId() == null) {
            return createHeader(header);
        }
        Header result = headerService.save(header);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, header.getId().toString()))
            .body(result);
    }

    /**
     * GET  /headers : get all the headers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of headers in body
     */
    @GetMapping("/headers")
    @Timed
    public ResponseEntity<List<Header>> getAllHeaders(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Headers");
        Page<Header> page = headerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/headers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /headers/:id : get the "id" header.
     *
     * @param id the id of the header to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the header, or with status 404 (Not Found)
     */
    @GetMapping("/headers/{id}")
    @Timed
    public ResponseEntity<Header> getHeader(@PathVariable Long id) {
        log.debug("REST request to get Header : {}", id);
        Header header = headerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(header));
    }

    /**
     * DELETE  /headers/:id : delete the "id" header.
     *
     * @param id the id of the header to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/headers/{id}")
    @Timed
    public ResponseEntity<Void> deleteHeader(@PathVariable Long id) {
        log.debug("REST request to delete Header : {}", id);
        headerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/headers?query=:query : search for the header corresponding
     * to the query.
     *
     * @param query the query of the header search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/headers")
    @Timed
    public ResponseEntity<List<Header>> searchHeaders(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Headers for query {}", query);
        Page<Header> page = headerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/headers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
