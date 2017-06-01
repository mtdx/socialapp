package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.TwitterAccount;
import com.ninja.socialapp.domain.enumeration.TwitterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwitterSchedulerService {

    private final Logger log = LoggerFactory.getLogger(TwitterSchedulerService.class);

    private final TwitterAccountService twitterAccountService;

    private final TwitterApiService twitterApiService;

    public TwitterSchedulerService(TwitterAccountService twitterAccountService, TwitterApiService twitterApiService) {
        this.twitterAccountService = twitterAccountService;
        this.twitterApiService = twitterApiService;
    }

    /**
     * We check for newly added/updated twitter accounts and we modify them via the twitter API
     * <p>
     * This is scheduled to get fired every 59 seconds.
     * </p>
     */
    @Async
    @Scheduled(cron = "*/59 * * * * *" )
    public void updateAccounts() {
        log.debug("Run scheduled update accounts {}");
        List<TwitterAccount> accounts = twitterAccountService.findByStatus(TwitterStatus.PENDING_UPDATE);
        for (TwitterAccount account : accounts){
            twitterApiService.updateAccount(account);
        }
    }
}
