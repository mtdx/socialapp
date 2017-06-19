package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.TwitterSettings;
import com.ninja.socialapp.repository.TwitterSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * Service Implementation for managing TwitterSettings.
 */
@Service
@Transactional
public class TwitterSettingsService {

    private final Logger log = LoggerFactory.getLogger(TwitterSettingsService.class);

    private final TwitterSettingsRepository twitterSettingsRepository;

    public TwitterSettingsService(TwitterSettingsRepository twitterSettingsRepository) {
        this.twitterSettingsRepository = twitterSettingsRepository;
    }

    /**
     * Save a twitterSettings.
     *
     * @param twitterSettings the entity to save
     * @return the persisted entity
     */
    public TwitterSettings save(TwitterSettings twitterSettings) {
        log.debug("Request to save TwitterSettings : {}", twitterSettings);
        TwitterSettings result = twitterSettingsRepository.save(twitterSettings);
        return result;
    }

    /**
     *  Get one twitterSettings by id.
     *
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TwitterSettings findOne() {
        log.debug("Request to get TwitterSettings : {}");
        Optional<TwitterSettings> twitterSettings = twitterSettingsRepository.findOne();
        return twitterSettings.orElseGet(this::saveDefault);
    }

    /**
     * Save the default twitterSettings.
     *
     * @return the persisted entity
     */
    public TwitterSettings saveDefault() {
        log.debug("Request to save default TwitterSettings : {}");
        TwitterSettings defaultTwitterSettings = new TwitterSettings();
        defaultTwitterSettings.setMaxLikes(5000); // likes until start doing unlikes
        defaultTwitterSettings.setHasDefaultProfileImage(true); // skip if
        defaultTwitterSettings.hasNoDescription(true);  // skip if
        defaultTwitterSettings.accountAgeLessThan(3); // skip if less, (months)
        defaultTwitterSettings.minActivity(35); // minimum likes, tweets, followers, following
        defaultTwitterSettings.followingToFollowersRatio(3); // eg. 3 => 900 following 300 followers
        defaultTwitterSettings.likesToTweetsRatio(3);   // eg. 3 => 900 likes 300 tweets
        defaultTwitterSettings.notLikeTweetsOlderThan(2); // skip if less, (months)
        TwitterSettings result = twitterSettingsRepository.save(defaultTwitterSettings);
        return result;
    }
}