package com.ninja.socialapp.config;

import com.ninja.socialapp.domain.Competitor;
import com.ninja.socialapp.domain.TwitterAccount;
import com.ninja.socialapp.domain.enumeration.CompetitorStatus;
import com.ninja.socialapp.domain.enumeration.TwitterStatus;
import com.ninja.socialapp.service.CompetitorService;
import com.ninja.socialapp.service.TwitterAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class StartupReset {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    private final TwitterAccountService twitterAccountService;

    private final CompetitorService competitorService;

    public StartupReset(TwitterAccountService twitterAccountService, CompetitorService competitorService){
            this.twitterAccountService = twitterAccountService;
            this.competitorService = competitorService;
    }


    /**
     * Resets the status when we push a new version so entities don't get stuck.
     */
    @PostConstruct
    public void init(){
        List<TwitterAccount> twitterAccounts = twitterAccountService.findAllByStatus(TwitterStatus.WORKING);
        List<Competitor> competitors = competitorService.findAllByStatus(CompetitorStatus.LOCK);

        for (TwitterAccount account : twitterAccounts) {
            account.setStatus(TwitterStatus.IDLE);
            twitterAccountService.save(account);
        }
        for (Competitor competitor : competitors) {
            competitor.setStatus(CompetitorStatus.IN_PROGRESS);
            competitorService.save(competitor);
        }
    }
}
