package com.ninja.socialapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Twitter API.
 */
@Service
@Transactional
public class TwitterApiService {

    private final Logger log = LoggerFactory.getLogger(TwitterApiService.class);

}
