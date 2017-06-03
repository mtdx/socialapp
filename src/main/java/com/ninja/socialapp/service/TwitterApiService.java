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
import java.util.Date;

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
            TwitterError twitterError = new TwitterError();
            twitterError.setType(TwitterErrorType.UPDATE);
            twitterError.setErrorCode(ex.getErrorCode());
            twitterError.setAccount(twitterAccount.getUsername());
            twitterError.setErrorMessage(ex.getErrorMessage());
            twitterError.setMessage(ex.getMessage());
            if(ex.getRateLimitStatus() != null) {
                twitterError.setRateLimitStatus(String.format("%d / %d",
                    ex.getRateLimitStatus().getRemaining(), ex.getRateLimitStatus().getLimit()));
            }
            twitterError.setStatusCode(ex.getStatusCode());
            twitterErrorService.save(twitterError);
        }
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
}
