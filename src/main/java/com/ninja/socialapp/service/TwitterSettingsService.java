package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.TwitterSettings;
import com.ninja.socialapp.repository.TwitterSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TwitterSettings findOne(Long id) {
        log.debug("Request to get TwitterSettings : {}", id);
        return twitterSettingsRepository.findOne(id);
    }
}
