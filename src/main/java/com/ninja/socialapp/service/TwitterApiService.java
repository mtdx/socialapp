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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Service Implementation for managing Twitter API.
 */
@Service
public class TwitterApiService {

    private final int currentMonth;

    private final int currentYear;

    private final Logger log = LoggerFactory.getLogger(TwitterApiService.class);

    private final TwitterErrorService twitterErrorService;

    private final TwitterAccountService twitterAccountService;

    public TwitterApiService(TwitterErrorService twitterErrorService, TwitterAccountService twitterAccountService){
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        currentMonth = localDate.getMonthValue();
        currentYear = localDate.getYear();
        this.twitterErrorService = twitterErrorService;
        this.twitterAccountService = twitterAccountService;
    }

    @Async
    public void updateAccount(final TwitterAccount twitterAccount){
        new TwitterAPI(twitterAccount).updateAccount();
    }

    /**
     * Private inner class to deal with multithreading
     */
    private class TwitterAPI{

        private final TwitterAccount twitterAccount;

        TwitterAPI(TwitterAccount twitterAccount){
            this.twitterAccount = twitterAccount;
        }

        /**
         * Updates a twitter account via API
         */
        void updateAccount(){
            log.debug("Request to update a twitter accounts via TwitterAPI: {}", twitterAccount.getEmail());
            final Twitter twitterClient = getTwitterInstance();
            try {
                twitterClient.updateProfile(twitterAccount.getName(), twitterAccount.getUrl(),
                    twitterAccount.getLocation(), twitterAccount.getDescription());
                twitterAccount.setStatus(TwitterStatus.IDLE);
                twitterAccountService.save(twitterAccount);
            } catch (TwitterException ex) {
                TwitterError twitterError = new TwitterError();
                twitterError.setType(TwitterErrorType.UPDATE);
                twitterError.setErrorCode(ex.getErrorCode());
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

        private Twitter getTwitterInstance() {
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
}
