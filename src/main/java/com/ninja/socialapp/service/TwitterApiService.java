package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.TwitterAccount;
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

    public TwitterApiService(){
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        currentMonth = localDate.getMonthValue();
        currentYear = localDate.getYear();
    }

    @Async
    public void updateAccount(final TwitterAccount twitterAccount){
        log.debug("Request to update a twitter accounts via API: {}", twitterAccount.getEmail());
        final Twitter twitterClient = getTwitterInstance(twitterAccount);
        try {
             twitterClient.updateProfile(twitterAccount.getName(), twitterAccount.getUrl(),
                twitterAccount.getLocation(), twitterAccount.getDescription());
        } catch (TwitterException ex) {
            log.error("Request to update a twitter account (" + twitterAccount.getId() +  "): {} ", ex);
        }
    }

    private Twitter getTwitterInstance(TwitterAccount twitterAccount) {
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
