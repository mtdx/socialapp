package com.ninja.socialapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TwitterSchedulerService {

    private final Logger log = LoggerFactory.getLogger(TwitterSchedulerService.class);


    @Scheduled(fixedRate = 3000)
    public void reportCurrentTime() {
        log.debug("The time is now {}");
    }
}
