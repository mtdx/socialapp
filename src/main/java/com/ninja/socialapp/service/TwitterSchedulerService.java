package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.Competitor;
import com.ninja.socialapp.domain.TwitterAccount;
import com.ninja.socialapp.domain.TwitterSettings;
import com.ninja.socialapp.domain.enumeration.CompetitorStatus;
import com.ninja.socialapp.domain.enumeration.TwitterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class TwitterSchedulerService {

    private final Logger log = LoggerFactory.getLogger(TwitterSchedulerService.class);

    private final TwitterAccountService twitterAccountService;

    private final TwitterApiService twitterApiService;

    private final CompetitorService competitorService;

    private final TwitterErrorService twitterErrorService;

    private final TwitterSettingsService twitterSettingsService;

    public TwitterSchedulerService(TwitterAccountService twitterAccountService, TwitterApiService twitterApiService,
                                   CompetitorService competitorService, TwitterErrorService twitterErrorService,
                                   TwitterSettingsService twitterSettingsService) {
        this.twitterAccountService = twitterAccountService;
        this.twitterApiService = twitterApiService;
        this.competitorService = competitorService;
        this.twitterErrorService = twitterErrorService;
        this.twitterSettingsService = twitterSettingsService;
    }

    /**
     * We check for newly added/updated twitter accounts and we modify them via the twitter API
     * <p>
     * This is scheduled to get fired every 2 minutes.
     * </p>
     */
    @Async
    @Scheduled(cron = "0 */4 * * * *")
    public void updateAccounts() {
        log.debug("Run scheduled update accounts {}");
        List<TwitterAccount> accounts = twitterAccountService.findAllByStatus(TwitterStatus.PENDING_UPDATE);
        for (TwitterAccount account : accounts) {
            new Thread(() -> twitterApiService.updateAccount(account)).start();
        }
    }

    /**
     * We check for competitors and like their followers keeping a cursor
     * <p>
     * This is scheduled to get fired every 1 minute.
     * </p>
     */
    @Async
    @Scheduled(cron = "30 */2 * * * *")
    public void processCompetitors() {
        log.debug("Run scheduled process competitors {}");
        competitorService.findFirstByStatusOrderByIdAsc(CompetitorStatus.IN_PROGRESS).ifPresent((Competitor competitor) -> {
            TwitterSettings twitterSettings = twitterSettingsService.findOne();
            List<TwitterAccount> accounts = twitterAccountService.findAllByStatus(TwitterStatus.IDLE);
            twitterApiService.refreshDate();

            competitor.setStatus(CompetitorStatus.LOCK); // next we update our statuses
            competitorService.save(competitor);
            for (TwitterAccount account : accounts) {
                account.setStatus(TwitterStatus.WORKING);
                twitterAccountService.save(account);
            }

            long cursor = competitor.getCursor() == null ? -1L : competitor.getCursor(); // if cursor -1 update to done
            for (TwitterAccount account : accounts) {
                if (cursor == 0) {
                    if (competitor.getStatus() != CompetitorStatus.DONE) {  // we don't want to save multiple times
                        competitor.setStatus(CompetitorStatus.DONE);
                        competitorService.save(competitor);
                    }
                    account.setStatus(TwitterStatus.IDLE);
                    twitterAccountService.save(account);
                    continue;   // no point moving on as competitor followers are done
                }
                cursor = twitterApiService.setupFollowers(account, cursor, competitor, twitterSettings);
            }

            competitor.setCursor(cursor);  // we save our cursor to keep track and update back our status
            competitor.setStatus(cursor == 0 ? CompetitorStatus.DONE : CompetitorStatus.IN_PROGRESS);
            competitorService.save(competitor);
        });
    }

    /**
     * We delete twitter errors older than 7 days to keep db small. Whe need to delete one by one to delete from search too.
     * <p>
     * This is scheduled to get fired every 1 minute.
     * </p>
     */
    @Async
    @Scheduled(cron = "0 0 23 * * *")
    public void deleteTwitterErrors() {
        log.debug("Run scheduled delete old twitter errors {}");
        final int DAYS = 7; // how much time we keep data
        List<Long> olderThanErrorsIds = twitterErrorService.findOlderThan(Instant.now().minus(Duration.ofDays(DAYS)));
        for (Long olderThanErrorsId : olderThanErrorsIds){
            twitterErrorService.delete(olderThanErrorsId);
        }
    }

    /**
     * We delete twitter errors older than 7 days to keep db small. Whe need to delete one by one to delete from search too.
     * <p>
     * This is scheduled to get fired every 1 minute.
     * </p>
     */
    @Async
    @Scheduled(cron = "0 */5 * * * *")
    public void addTwitterCompetitors() {
        log.debug("Run scheduled add twitter competitors {}");
        final int BATCH = 10;
        if (competitorService.countAllByStatus(CompetitorStatus.IN_PROGRESS) == 0){

        }
    }
}
