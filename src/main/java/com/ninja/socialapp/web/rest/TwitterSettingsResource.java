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
        TwitterSettings newTwitterSettings = twitterSettingsService.findOne();
        newTwitterSettings.setMaxLikes(twitterSettings.getMaxLikes());
        newTwitterSettings.setHasDefaultProfileImage(twitterSettings.isHasDefaultProfileImage());
        newTwitterSettings.hasNoDescription(twitterSettings.isHasNoDescription());
        newTwitterSettings.accountAgeLessThan(twitterSettings.getAccountAgeLessThan());
        newTwitterSettings.minActivity(twitterSettings.getMinActivity());
        newTwitterSettings.followingToFollowersRatio(twitterSettings.getFollowingToFollowersRatio());
        newTwitterSettings.likesToTweetsRatio(twitterSettings.getLikesToTweetsRatio());
        newTwitterSettings.notLikeTweetsOlderThan(twitterSettings.getNotLikeTweetsOlderThan());
        twitterSettingsService.save(newTwitterSettings);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, twitterSettings.getId().toString()))
            .body(newTwitterSettings);
    }

    /**
     * GET  /twitter-settings/ get the twitterSettings.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the twitterSettings, or with status 404 (Not Found)
     */
    @GetMapping("/twitter-settings")
    @Timed
    public ResponseEntity<TwitterSettings> getTwitterSettings() {
        log.debug("REST request to get TwitterSettings : {}");
        return ResponseEntity.ok(twitterSettingsService.findOne());
    }

}
