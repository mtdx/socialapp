package com.ninja.socialapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ninja.socialapp.domain.TwitterSettings;
import com.ninja.socialapp.service.TwitterSettingsService;
import com.ninja.socialapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * REST controller for managing TwitterSettings.
 */
@RestController
@RequestMapping("/api")
public class TwitterSettingsResource {

    private final Logger log = LoggerFactory.getLogger(TwitterSettingsResource.class);

    private static final String ENTITY_NAME = "twitterSettings";

    private final TwitterSettingsService twitterSettingsService;

    public TwitterSettingsResource(TwitterSettingsService twitterSettingsService) {
        this.twitterSettingsService = twitterSettingsService;
    }

    /**
     * POST  /twitter-settings : Create a new twitterSettings.
     *
     * @param twitterSettings the twitterSettings to create
     * @return the ResponseEntity with status 201 (Created) and with body the new twitterSettings, or with status 400 (Bad Request) if the twitterSettings has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/twitter-settings")
    @Timed
    public ResponseEntity<TwitterSettings> createTwitterSettings(@Valid @RequestBody TwitterSettings twitterSettings) throws URISyntaxException {
        log.debug("REST request to save TwitterSettings : {}", twitterSettings);
        if (twitterSettings.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new twitterSettings cannot already have an ID")).body(null);
        }
        TwitterSettings result = twitterSettingsService.save(twitterSettings);
        return ResponseEntity.created(new URI("/api/twitter-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /twitter-settings : Updates an existing twitterSettings.
     *
     * @param twitterSettings the twitterSettings to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated twitterSettings,
     * or with status 400 (Bad Request) if the twitterSettings is not valid,
     * or with status 500 (Internal Server Error) if the twitterSettings couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/twitter-settings")
    @Timed
    public ResponseEntity<TwitterSettings> updateTwitterSettings(@Valid @RequestBody TwitterSettings twitterSettings) throws URISyntaxException {
        log.debug("REST request to update TwitterSettings : {}", twitterSettings);
        if (twitterSettings.getId() == null) {
            return createTwitterSettings(twitterSettings);
        }
        TwitterSettings result = twitterSettingsService.save(twitterSettings);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, twitterSettings.getId().toString()))
            .body(result);
    }

    /**
     * GET  /twitter-settings/:id : get the "id" twitterSettings.
     *
     * @param id the id of the twitterSettings to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the twitterSettings, or with status 404 (Not Found)
     */
    @GetMapping("/twitter-settings/{id}")
    @Timed
    public ResponseEntity<TwitterSettings> getTwitterSettings(@PathVariable Long id) {
        log.debug("REST request to get TwitterSettings : {}", id);
        TwitterSettings twitterSettings = twitterSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(twitterSettings));
    }

}
