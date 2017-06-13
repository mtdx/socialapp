package com.ninja.socialapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ninja.socialapp.domain.Proxy;
import com.ninja.socialapp.service.ProxyService;
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
 * REST controller for managing Proxy.
 */
@RestController
@RequestMapping("/api")
public class ProxyResource {

    private final Logger log = LoggerFactory.getLogger(ProxyResource.class);

    private static final String ENTITY_NAME = "proxy";

    private final ProxyService proxyService;

    public ProxyResource(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    /**
     * POST  /proxies : Create a new proxy.
     *
     * @param proxy the proxy to create
     * @return the ResponseEntity with status 201 (Created) and with body the new proxy, or with status 400 (Bad Request) if the proxy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/proxies")
    @Timed
    public ResponseEntity<Proxy> createProxy(@Valid @RequestBody Proxy proxy) throws URISyntaxException {
        log.debug("REST request to save Proxy : {}", proxy);
        if (proxy.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new proxy cannot already have an ID")).body(null);
        }
        Proxy result = proxyService.save(proxy);
        return ResponseEntity.created(new URI("/api/proxies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /proxies : Updates an existing proxy.
     *
     * @param proxy the proxy to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated proxy,
     * or with status 400 (Bad Request) if the proxy is not valid,
     * or with status 500 (Internal Server Error) if the proxy couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/proxies")
    @Timed
    public ResponseEntity<Proxy> updateProxy(@Valid @RequestBody Proxy proxy) throws URISyntaxException {
        log.debug("REST request to update Proxy : {}", proxy);
        if (proxy.getId() == null) {
            return createProxy(proxy);
        }
        Proxy result = proxyService.save(proxy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, proxy.getId().toString()))
            .body(result);
    }

    /**
     * GET  /proxies : get all the proxies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of proxies in body
     */
    @GetMapping("/proxies")
    @Timed
    public ResponseEntity<List<Proxy>> getAllProxies(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Proxies");
        Page<Proxy> page = proxyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/proxies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /proxies/:id : get the "id" proxy.
     *
     * @param id the id of the proxy to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the proxy, or with status 404 (Not Found)
     */
    @GetMapping("/proxies/{id}")
    @Timed
    public ResponseEntity<Proxy> getProxy(@PathVariable Long id) {
        log.debug("REST request to get Proxy : {}", id);
        Proxy proxy = proxyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(proxy));
    }

    /**
     * DELETE  /proxies/:id : delete the "id" proxy.
     *
     * @param id the id of the proxy to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/proxies/{id}")
    @Timed
    public ResponseEntity<Void> deleteProxy(@PathVariable Long id) {
        log.debug("REST request to delete Proxy : {}", id);
        proxyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/proxies?query=:query : search for the proxy corresponding
     * to the query.
     *
     * @param query the query of the proxy search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/proxies")
    @Timed
    public ResponseEntity<List<Proxy>> searchProxies(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Proxies for query {}", query);
        Page<Proxy> page = proxyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/proxies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
