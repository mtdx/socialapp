package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.TwitterAccount;
import com.ninja.socialapp.domain.TwitterError;
import com.ninja.socialapp.domain.enumeration.TwitterErrorType;
import com.ninja.socialapp.domain.enumeration.TwitterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service Implementation for managing Twitter API.
 */
@Service
public class TwitterApiService {

    private final Logger log = LoggerFactory.getLogger(TwitterApiService.class);

    private final TwitterErrorService twitterErrorService;

    private final TwitterAccountService twitterAccountService;

    private final int currentMonth;

    private final int currentYear;

    public TwitterApiService(TwitterErrorService twitterErrorService, TwitterAccountService twitterAccountService){
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        currentMonth = localDate.getMonthValue();
        currentYear = localDate.getYear();
        this.twitterErrorService = twitterErrorService;
        this.twitterAccountService = twitterAccountService;
    }

    /**
     * Updates a twitter account via API
     */
    @Async
    public void updateAccount(final TwitterAccount twitterAccount){
        log.debug("Request to update a twitter accounts via TwitterAPI: {}", twitterAccount.getEmail());
        final Twitter twitterClient = getTwitterInstance(twitterAccount);
        try {
            User user = twitterClient.updateProfile(twitterAccount.getName(), twitterAccount.getUrl(),
                twitterAccount.getLocation(), twitterAccount.getDescription());
            twitterClient.updateProfileImage(new ByteArrayInputStream(twitterAccount.getAvatar().getImage()));
            twitterClient.updateProfileBanner(new ByteArrayInputStream(twitterAccount.getHeader().getImage()));
            twitterAccount.setUsername(user.getScreenName());
            twitterAccount.setStatus(TwitterStatus.IDLE);
            twitterAccountService.save(twitterAccount);
        } catch (TwitterException ex) {
            saveEx(ex, twitterAccount.getUsername());
        }
    }

    /**
     * Just likes the followers it receives
     */
    @Async
    public void likeFollowers(final TwitterAccount twitterAccount){
        log.debug("Request to update a twitter accounts via TwitterAPI: {}", twitterAccount.getEmail());

        // update status account
    }

    /**
     * Just likes the followers it receives
     */
    @Async
    public void destroyLikes(final TwitterAccount twitterAccount){
        log.debug("Request to update a twitter accounts via TwitterAPI: {}", twitterAccount.getEmail());
        Twitter twitterClient = getTwitterInstance(twitterAccount);
        Paging paging = new Paging(1);
        List<Status> list = new ArrayList<>();
        do {
            try {
                list = twitterClient.getFavorites(paging);
                for (Status s : list) {
                    twitterClient.destroyFavorite(s.getId());
                }
                paging.setPage(paging.getPage() + 1);
                threadWait(60);
            } catch (TwitterException ex) {
                saveEx(ex, twitterAccount.getUsername());
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

    private void saveEx(TwitterException ex, String username) {
        TwitterError twitterError = new TwitterError();
        twitterError.setType(TwitterErrorType.UPDATE);
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

    private boolean isSpamAccount(long ID, Twitter twitterClient, String username) {
        try {
            User user = twitterClient.showUser(ID);
            if (user.isDefaultProfileImage() || user.getDescription().length() == 0) {
                return true;
            }

            LocalDate tweetData = user.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int accountMonth = tweetData.getMonthValue();
            int accountYear = tweetData.getYear();
            if (accountYear == currentYear && (currentMonth - accountMonth) <= 3) {
                return true;
            }

            byte activityRaw = 35; // a minimum activity metric
            int likes = user.getFavouritesCount(); // likes
            int followers = user.getFollowersCount(); // followers
            int following = user.getFriendsCount(); // following
            int statuses = user.getStatusesCount(); // tweets
            if (likes <= activityRaw || followers <= activityRaw || following <= activityRaw || statuses <= activityRaw) {
                return true;
            }
            if ((following / followers) >= 3 || (likes / statuses) >= 3) {
                return true;
            }

        } catch (TwitterException ex) {
            saveEx(ex, username);
        }

        return false;
    }
}
