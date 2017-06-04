package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.Competitor;
import com.ninja.socialapp.domain.TwitterAccount;
import com.ninja.socialapp.domain.enumeration.CompetitorStatus;
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

    private final CompetitorService competitorService;

    public TwitterSchedulerService(TwitterAccountService twitterAccountService, TwitterApiService twitterApiService, CompetitorService competitorService) {
        this.twitterAccountService = twitterAccountService;
        this.twitterApiService = twitterApiService;
        this.competitorService = competitorService;
    }

    /**
     * We check for newly added/updated twitter accounts and we modify them via the twitter API
     * <p>
     * This is scheduled to get fired every 2 minutes.
     * </p>
     */
    @Async
    @Scheduled(cron = "0 */2 * * * *" )
    public void updateAccounts() {
        log.debug("Run scheduled update accounts {}");
        List<TwitterAccount> accounts = twitterAccountService.findByStatus(TwitterStatus.PENDING_UPDATE);
        for (TwitterAccount account : accounts){
            twitterApiService.updateAccount(account);
        }
    }

    /**
     * We check for competitors add their followers keeping a cursor
     * <p>
     * This is scheduled to get fired every 1 minute.
     * </p>
     */
    @Async
    @Scheduled(cron = "0 */2 * * * *" )
    public void addFollowers() {
        log.debug("Run scheduled add followers {}");
        Competitor competitor = competitorService.findOneByStatus(CompetitorStatus.IN_PROGRESS);

    }
}
