package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.Competitor;
import com.ninja.socialapp.domain.TwitterAccount;
import com.ninja.socialapp.domain.TwitterKeyword;
import com.ninja.socialapp.domain.TwitterSettings;
import com.ninja.socialapp.domain.enumeration.CompetitorStatus;
import com.ninja.socialapp.domain.enumeration.RetweetAccountStatus;
import com.ninja.socialapp.domain.enumeration.TwitterErrorType;
import com.ninja.socialapp.domain.enumeration.TwitterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service Implementation for managing Twitter API.
 */
@Service
public class TwitterApiService {

    private final Logger log = LoggerFactory.getLogger(TwitterApiService.class);

    private final TwitterErrorService twitterErrorService;

    private final TwitterAccountService twitterAccountService;

    private final CompetitorService competitorService;

    private final TwitterKeywordService twitterKeywordService;

    private int currentMonth;

    private int currentYear;

    public TwitterApiService(TwitterErrorService twitterErrorService, TwitterAccountService twitterAccountService,
                             CompetitorService competitorService, TwitterKeywordService twitterKeywordService) {
        this.twitterErrorService = twitterErrorService;
        this.twitterAccountService = twitterAccountService;
        this.competitorService = competitorService;
        this.twitterKeywordService = twitterKeywordService;
    }

    /**
     * Updates a twitter account via API
     */
    public void updateAccount(final TwitterAccount twitterAccount, final Twitter twitterClient) {
        log.debug("Call to update a twitter accounts via TwitterAPI: {}", twitterAccount.getEmail());
        try {
            User user = twitterClient.updateProfile(twitterAccount.getMessage().getAccountName(),
                twitterAccount.getMessage().getAccountUrl(), twitterAccount.getMessage().getAccountLocation(),
                twitterAccount.getMessage().getAccountDescription());
            if (twitterAccount.getAvatar() != null)
                twitterClient.updateProfileImage(new ByteArrayInputStream(twitterAccount.getAvatar().getImage()));
            if (twitterAccount.getHeader() != null)
                twitterClient.updateProfileBanner(new ByteArrayInputStream(twitterAccount.getHeader().getImage()));
            // we also check for retweet
            retweetAccount(twitterAccount, twitterClient);

            twitterAccount.setUsername(user.getScreenName());
            TwitterStatus status = getRightStatus(twitterAccount);
            twitterAccount.setPrevStatus(status);
            twitterAccount.setStatus(status);
            twitterAccountService.save(twitterAccount);
        } catch (TwitterException ex) {
            twitterErrorService.handleException(ex, twitterAccount, TwitterErrorType.UPDATE);
            twitterAccount.setStatus(TwitterStatus.AUTH_ERROR);
            twitterAccountService.save(twitterAccount);
        } catch (Exception ex){
            log.error(ex.getMessage());
        }
    }

    /**
     * Just get and pass the idle accounts and start a thread
     */
    public void setupUpdateAccounts(final List<TwitterAccount> twitterAccounts) {
        for (TwitterAccount twitterAccount : twitterAccounts) {
            final Twitter twitterClient = getTwitterInstance(twitterAccount);
            new Thread(() -> updateAccount(twitterAccount, twitterClient)).start();
            threadWait(getRandInt(3, 7));
        }
    }

    /**
     * Updates a twitter account via API
     */
    public void retweetAccount(final TwitterAccount twitterAccount, final Twitter twitterClient) {
        log.debug("Call to retweet a twitter account via TwitterAPI: {}", twitterAccount.getEmail());
        try {
            if (twitterAccount.getRetweetAccount() != null
                && twitterAccount.getRetweetAccount().getStatus() != RetweetAccountStatus.STOPPED) {
                twitterClient.retweetStatus(Long.valueOf(twitterAccount.getRetweetAccount().getTweetId()));
            }
        } catch (TwitterException ex) {
            twitterErrorService.handleException(ex, twitterAccount, TwitterErrorType.UPDATE);
        } catch (Exception ex){
            log.error(ex.getMessage());
        }
    }

    /**
     * Just get and pass the  followers from the competitor it receives
     */
    public long setupFollowers(final TwitterAccount twitterAccount, Long cursor, final Competitor competitor, final TwitterSettings twitterSettings) {
        Twitter twitterClient = getTwitterInstance(twitterAccount);
        try {
            IDs ids = twitterClient.getFollowersIDs(Long.parseLong(competitor.getUserid()), cursor);
            new Thread(() -> likeFollowersTweetsOf(ids.getIDs(), twitterClient, twitterAccount, competitor.getId(), twitterSettings)).start();
            return ids.getNextCursor();
        } catch (TwitterException ex) {
            twitterErrorService.handleException(ex, twitterAccount, TwitterErrorType.LIKE);
        } catch (Exception ex){
            log.error(ex.getMessage());
        }
        return cursor;
    }

    /**
     * Here we do most of the work, we like the followers we get
     */
    private void likeFollowersTweetsOf(long[] followers, Twitter twitterClient, final TwitterAccount twitterAccount,
                                       final long competitorId, final TwitterSettings twitterSettings) {
        log.debug("Call to create twitter likes via TwitterAPI: {}", twitterAccount.getEmail());
        Long likes = 0L;
        try {
            if (twitterClient.showUser(twitterAccount.getUsername()).getFavouritesCount() >= twitterSettings.getMaxLikes()) {
                destroyLikes(twitterAccount, twitterClient); // here we try to do some cleanup
            }
        } catch (TwitterException ex) {
            twitterErrorService.handleException(ex, twitterAccount, TwitterErrorType.LIKE);
        } catch (Exception ex){
            log.error(ex.getMessage());
        }
        for (Long ID : followers) {
            threadWait(getRandInt(3, 7));  // 180 per 15 min request limit
            if (isSpamAccount(ID, twitterClient, twitterAccount, twitterSettings))
                continue;  // we try to target real accounts only
            try {
                ResponseList<Status> statuses = twitterClient.getUserTimeline(ID);
                if (statuses.isEmpty()) continue;
                Status tweet = statuses.get(0);
                if (tweet.isFavorited() || tweet.isRetweeted()) continue; // if we already did the tweet

                LocalDate tweetDate = tweet.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int tweetMonth = tweetDate.getMonthValue();
                int tweetYear = tweetDate.getYear();
                int difference = (currentYear - tweetYear) * 12 + currentMonth - tweetMonth;
                if (twitterSettings.getNotLikeTweetsOlderThan() != 0 && difference <= twitterSettings.getNotLikeTweetsOlderThan()) {  // we only favorite tweets newer than x months
                    Long tweetId = tweet.getId();
                    String tweetText = tweet.getText();
                    threadWait(getRandInt(30, 90));

                    if (twitterAccount.getRetweetAccount() == null && tweetText.length() >= 70
                        && getRandInt(1, 100) <= twitterSettings.getRetweetPercent()) {
                        twitterClient.retweetStatus(tweetId); // also we retweet, a % & only long tweets
                    } else {
                        twitterClient.createFavorite(tweetId);
                        likes++;
                    }
                }
            } catch (TwitterException ex) {
                twitterErrorService.handleException(ex, twitterAccount, TwitterErrorType.LIKE);
                twitterClient = getTwitterInstance(twitterAccount);
            } catch (Exception ex){
                log.error(ex.getMessage());
            }
        }
        competitorService.incrementLikes(likes, competitorId);
        twitterAccount.setStatus(TwitterStatus.IDLE); // we reset the account
        twitterAccountService.save(twitterAccount);
    }

    /**
     * Here we add competitors based on keywords
     */
    private void addCompetitors(List<User> users, final TwitterAccount twitterAccount,
                                final long twitterKeywordId, final Integer minCompetitorFollowers) {
        log.debug("Call to add competitors via TwitterAPI: {}", twitterAccount.getEmail());
        Integer competitors = 0;
        for (User user : users) {
            if (user.getScreenName().length() <= 3) {
                continue;
            }
            try {
                String userId = String.valueOf(user.getId());
                if (!competitorService.findByUserid(userId).isPresent()
                    && user.getFollowersCount() >= minCompetitorFollowers) {
                    Competitor competitor = new Competitor();
                    competitor.setUserid(userId);
                    competitor.setUsername(user.getScreenName());
                    competitor.setStatus(CompetitorStatus.IN_PROGRESS);
                    competitor.setLikes(0L);
                    competitor.setCursor(-1L);
                    competitor.setStop(false);
                    competitor.setReset(false);
                    competitor.setCreated(Instant.now());
                    competitorService.save(competitor);
                    competitors++;
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
        if (competitors > 0) {
            twitterKeywordService.incrementCompetitors(competitors, twitterKeywordId);
        }
        twitterAccount.setStatus(TwitterStatus.IDLE); // we reset the account
        twitterAccountService.save(twitterAccount);
    }

    /**
     * Destroy previous likes older than 2 days
     */
    private void destroyLikes(final TwitterAccount twitterAccount, Twitter twitterClient) {
        log.debug("Call to destroy twitter likes via TwitterAPI: {}", twitterAccount.getEmail());
        Paging paging = new Paging(1);
        List<Status> list = new ArrayList<>();
        do {
            try {
                list = twitterClient.getFavorites(paging);
                for (Status s : list) {
                    twitterClient.destroyFavorite(s.getId());
                }
                paging.setPage(paging.getPage() + 1);
                threadWait(getRandInt(3, 7));
            } catch (TwitterException ex) {
                twitterErrorService.handleException(ex, twitterAccount, TwitterErrorType.LIKE);
                twitterClient = getTwitterInstance(twitterAccount);
                threadWait(30);
            } catch (Exception ex){
                log.error(ex.getMessage());
            }
        } while (list.size() > 0);
    }

    private Twitter getTwitterInstance(final TwitterAccount twitterAccount) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(false)
            .setOAuthConsumerKey(twitterAccount.getConsumerKey())
            .setOAuthConsumerSecret(twitterAccount.getConsumerSecret())
            .setOAuthAccessToken(twitterAccount.getAccessToken())
            .setOAuthAccessTokenSecret(twitterAccount.getAccessTokenSecret());
        cb.setHttpProxyHost(twitterAccount.getProxy().getHost())
            .setHttpProxyPort(twitterAccount.getProxy().getPort())
            .setHttpProxyUser(twitterAccount.getProxy().getUsername())
            .setHttpProxyPassword(twitterAccount.getProxy().getPassword());

        return new TwitterFactory(cb.build()).getInstance();
    }

    private void threadWait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private boolean isSpamAccount(long ID, Twitter twitterClient, final TwitterAccount twitterAccount, final TwitterSettings twitterSettings) {
        try {
            User user = twitterClient.showUser(ID);
            if ((twitterSettings.isHasDefaultProfileImage() && user.isDefaultProfileImage())
                || (twitterSettings.isHasNoDescription() && user.getDescription().length() == 0)) {
                return true;
            }

            LocalDate tweetData = user.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int accountMonth = tweetData.getMonthValue();
            int accountYear = tweetData.getYear();
            int difference = (currentYear - accountYear) * 12 + currentMonth - accountMonth;
            if (twitterSettings.getAccountAgeLessThan() != 0 && difference < twitterSettings.getAccountAgeLessThan()) {
                return true;
            }

            int activityRaw = twitterSettings.getMinActivity(); // a minimum activity metric
            int likes = user.getFavouritesCount(); // likes
            int followers = user.getFollowersCount(); // followers
            int following = user.getFriendsCount(); // following
            int statuses = user.getStatusesCount(); // tweets
            if (likes < activityRaw || followers < activityRaw / 2 || following < activityRaw || statuses < activityRaw) {
                return true;
            }
            if ((twitterSettings.getFollowingToFollowersRatio() != null && twitterSettings.getFollowingToFollowersRatio() > 0 && (following / followers) >= twitterSettings.getFollowingToFollowersRatio())
                || (twitterSettings.getLikesToTweetsRatio() != null && twitterSettings.getLikesToTweetsRatio() > 0 && (likes / statuses) >= twitterSettings.getLikesToTweetsRatio())) {
                return true;
            }

        } catch (TwitterException ex) {
            twitterErrorService.handleException(ex, twitterAccount, TwitterErrorType.LIKE);
        } catch (Exception ex){
            log.error(ex.getMessage());
        }

        return false;
    }

    /**
     * Adds competitors by keyword
     */
    public int setupCompetitors(final TwitterAccount twitterAccount, Integer page, final TwitterKeyword twitterKeyword, final TwitterSettings twitterSettings) {
        Twitter twitterClient = getTwitterInstance(twitterAccount);
        try {
            ResponseList<User> users = twitterClient.searchUsers(twitterKeyword.getKeyword(), page);
            new Thread(() -> addCompetitors(users, twitterAccount, twitterKeyword.getId(), twitterSettings.getMinCompetitorFollowers())).start();
            return ++page;
        } catch (TwitterException ex) {
            twitterErrorService.handleException(ex, twitterAccount, TwitterErrorType.SEARCH);
        } catch (Exception ex){
            log.error(ex.getMessage());
        }

        return page;
    }

    public void refreshDate() {
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        currentMonth = localDate.getMonthValue();
        currentYear = localDate.getYear();
    }

    private static int getRandInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private TwitterStatus getRightStatus(final TwitterAccount twitterAccount) {
        return (twitterAccount.getPrevStatus() != TwitterStatus.PENDING_UPDATE
            && twitterAccount.getPrevStatus() != TwitterStatus.AUTH_ERROR
            && twitterAccount.getPrevStatus() != TwitterStatus.SUSPENDED
            && twitterAccount.getPrevStatus() != TwitterStatus.LOCKED
            && twitterAccount.getPrevStatus() != TwitterStatus.LOCK)
            ? twitterAccount.getPrevStatus() : TwitterStatus.IDLE;
    }
}
