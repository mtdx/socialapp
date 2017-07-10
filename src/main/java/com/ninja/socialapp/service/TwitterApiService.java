package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.Competitor;
import com.ninja.socialapp.domain.TwitterAccount;
import com.ninja.socialapp.domain.TwitterError;
import com.ninja.socialapp.domain.TwitterSettings;
import com.ninja.socialapp.domain.enumeration.TwitterErrorType;
import com.ninja.socialapp.domain.enumeration.TwitterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.ByteArrayInputStream;
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

    private int currentMonth;

    private int currentYear;

    public TwitterApiService(TwitterErrorService twitterErrorService, TwitterAccountService twitterAccountService, CompetitorService competitorService){
        this.twitterErrorService = twitterErrorService;
        this.twitterAccountService = twitterAccountService;
        this.competitorService = competitorService;
    }

    /**
     * Updates a twitter account via API
     */
    public void updateAccount(final TwitterAccount twitterAccount){
        log.debug("Call to update a twitter accounts via TwitterAPI: {}", twitterAccount.getEmail());
        final Twitter twitterClient = getTwitterInstance(twitterAccount);
        try {
            User user = twitterClient.updateProfile(twitterAccount.getName(), twitterAccount.getUrl(),
                twitterAccount.getLocation(), twitterAccount.getDescription());

            if(twitterAccount.getAvatar() != null)
                twitterClient.updateProfileImage(new ByteArrayInputStream(twitterAccount.getAvatar().getImage()));
            if(twitterAccount.getHeader() != null)
                twitterClient.updateProfileBanner(new ByteArrayInputStream(twitterAccount.getHeader().getImage()));

            twitterAccount.setUsername(user.getScreenName());
            twitterAccount.setStatus(TwitterStatus.IDLE);
            twitterAccountService.save(twitterAccount);
        } catch (TwitterException ex) {
            saveEx(ex, twitterAccount.getUsername(), TwitterErrorType.LIKE);
        }
    }

    /**
     * Just get and pass the  followers from the competitor it receives
     */
    public long setupFollowers(final TwitterAccount twitterAccount, Long cursor, final Competitor competitor, final TwitterSettings twitterSettings){
        Twitter twitterClient = getTwitterInstance(twitterAccount);
        try {
            IDs ids = twitterClient.getFollowersIDs(Long.parseLong(competitor.getUserid()), cursor);
            new Thread(() -> likeFollowersTweetsOf(ids.getIDs(), twitterClient, twitterAccount, competitor.getId(), twitterSettings)).start();
            return ids.getNextCursor();
        } catch (TwitterException ex) {
            saveEx(ex, twitterAccount.getUsername(), TwitterErrorType.LIKE);
        }
        return cursor;
    }

    /**
     * Here we do most of the work, we like the followers we get
     */
    private void likeFollowersTweetsOf(long[] followers, Twitter twitterClient, final TwitterAccount twitterAccount,
                                       final long competitorId, final TwitterSettings twitterSettings){
        log.debug("Call to create twitter likes via TwitterAPI: {}", twitterAccount.getEmail());
        Long likes = 0L;
        try {
            if (twitterClient.showUser(twitterAccount.getUsername()).getFavouritesCount() >= twitterSettings.getMaxLikes()) {
                destroyLikes(twitterAccount, twitterClient); // here we try to do some cleanup
            }
        } catch (TwitterException ex) {
            saveEx(ex, twitterAccount.getUsername(), TwitterErrorType.LIKE);
        }
        for (Long ID : followers) {
            threadWait(getRandInt(2, 9));  // 180 per 15 min request limit
            if (isSpamAccount(ID, twitterClient, twitterAccount.getUsername(), twitterSettings))
                continue;  // we try to target real accounts only
            try {
                ResponseList<Status> statuses = twitterClient.getUserTimeline(ID);
                Status tweet = statuses.get(0);
                if (tweet.isFavorited() || tweet.isRetweeted()) continue; // if we already did the tweet

                LocalDate tweetDate = tweet.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int tweetMonth = tweetDate.getMonthValue();
                int tweetYear = tweetDate.getYear();
                int difference = (currentYear - tweetYear) * 12 + currentMonth - tweetMonth;
                if (twitterSettings.getNotLikeTweetsOlderThan() != 0 && difference <= twitterSettings.getNotLikeTweetsOlderThan()) {  // we only favorite tweets newer than x months
                    Long tweetId = tweet.getId();
                    String tweetText = tweet.getText();
                    threadWait(getRandInt(15, 105));

                    if (tweetText.length() >= 70 && getRandInt(1, 100) <= twitterSettings.getRetweetPercent()) {
                        twitterClient.retweetStatus(tweetId); // also we retweet, a % & only long tweets
                    } else {
                        twitterClient.createFavorite(tweetId);
                        likes++;
                    }
                }
            } catch (TwitterException ex) {
                saveEx(ex, twitterAccount.getUsername(), TwitterErrorType.LIKE);
                twitterClient = getTwitterInstance(twitterAccount);
            }
        }
        competitorService.incrementLikes(likes, competitorId);
        twitterAccount.setStatus(TwitterStatus.IDLE); // we reset the account
        twitterAccountService.save(twitterAccount);
    }

    /**
     * Destroy previous likes older than 2 days
     */
    private void destroyLikes(final TwitterAccount twitterAccount, Twitter twitterClient){
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
                threadWait(getRandInt(15, 105));
            } catch (TwitterException ex) {
                saveEx(ex, twitterAccount.getUsername(), TwitterErrorType.LIKE);
                twitterClient = getTwitterInstance(twitterAccount);
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

    private void saveEx(TwitterException ex, String username, TwitterErrorType type) {
        TwitterError twitterError = new TwitterError();
        twitterError.setType(type);
        twitterError.setErrorCode(ex.getErrorCode());
        twitterError.setAccount(username);
        twitterError.setErrorMessage(ex.getErrorMessage());
        twitterError.setMessage(ex.getMessage());
        if(ex.getRateLimitStatus() != null) {
            twitterError.setRateLimitStatus(String.format("%d / %d",
                ex.getRateLimitStatus().getRemaining(), ex.getRateLimitStatus().getLimit()));
        }
        twitterError.setStatusCode(ex.getStatusCode());
        twitterErrorService.save(twitterError);
    }

    private void threadWait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private boolean isSpamAccount(long ID, Twitter twitterClient, String username, final TwitterSettings twitterSettings) {
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
            if (likes <= activityRaw || followers <= activityRaw || following <= activityRaw || statuses <= activityRaw) {
                return true;
            }
            if ((twitterSettings.getFollowingToFollowersRatio() != null && twitterSettings.getFollowingToFollowersRatio() > 0 && (following / followers) >= twitterSettings.getFollowingToFollowersRatio())
                || (twitterSettings.getLikesToTweetsRatio() != null && twitterSettings.getLikesToTweetsRatio() > 0 && (likes / statuses) >= twitterSettings.getLikesToTweetsRatio())) {
                return true;
            }

        } catch (TwitterException ex) {
            saveEx(ex, username, TwitterErrorType.LIKE);
        }

        return false;
    }

    /**
     * Adds competitors by keyword
     */
    public long addCompetitors(final TwitterAccount twitterAccount, Integer page, final Competitor competitor, final TwitterSettings twitterSettings){
        Twitter twitterClient = getTwitterInstance(twitterAccount);
        try {
            ResponseList<User> users = twitterClient.searchUsers("csgo", page);
            for (User user : users){
                String a;
            }
            return ++page;
        } catch (TwitterException ex) {
            saveEx(ex, twitterAccount.getUsername(), TwitterErrorType.SEARCH);
        }

        return page;
    }

    public void refreshDate(){
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        currentMonth = localDate.getMonthValue();
        currentYear = localDate.getYear();
    }

    private static int getRandInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
