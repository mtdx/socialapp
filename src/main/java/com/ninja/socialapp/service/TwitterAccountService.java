package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.*;
import com.ninja.socialapp.domain.enumeration.TwitterStatus;
import com.ninja.socialapp.repository.TwitterAccountRepository;
import com.ninja.socialapp.repository.search.TwitterAccountSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TwitterAccount.
 */
@Service
@Transactional
public class TwitterAccountService {

    private final Logger log = LoggerFactory.getLogger(TwitterAccountService.class);

    private final TwitterAccountRepository twitterAccountRepository;

    private final TwitterAccountSearchRepository twitterAccountSearchRepository;

    private final TwitterSettingsService twitterSettingsService;

    public TwitterAccountService(TwitterAccountRepository twitterAccountRepository, TwitterAccountSearchRepository twitterAccountSearchRepository,
                                 TwitterSettingsService twitterSettingsService) {
        this.twitterAccountRepository = twitterAccountRepository;
        this.twitterAccountSearchRepository = twitterAccountSearchRepository;
        this.twitterSettingsService = twitterSettingsService;
    }

    /**
     * Save a twitterAccount.
     *
     * @param twitterAccount the entity to save
     * @return the persisted entity
     */
    public TwitterAccount save(TwitterAccount twitterAccount) {
        log.debug("Request to save TwitterAccount : {}", twitterAccount);
        TwitterAccount result = twitterAccountRepository.save(twitterAccount);
        twitterAccountSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the twitterAccounts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TwitterAccount> findAll(Pageable pageable) {
        log.debug("Request to get all TwitterAccounts");
        return filterExtra(twitterAccountRepository.findAll(pageable));
    }

    /**
     *  Get one twitterAccount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TwitterAccount findOne(Long id) {
        log.debug("Request to get TwitterAccount : {}", id);
        return filterExtraSingle(twitterAccountRepository.findOne(id));
    }

    /**
     *  Delete the  twitterAccount by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TwitterAccount : {}", id);
        twitterAccountRepository.delete(id);
        twitterAccountSearchRepository.delete(id);
    }

    /**
     * Search for the twitterAccount corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TwitterAccount> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TwitterAccounts for query {}", query);
        Page<TwitterAccount> result = twitterAccountSearchRepository.search(queryStringQuery(query), pageable);
        return filterExtra(result);
    }

    /**
     *  Get twitterAccounts by status.
     *
     *  @param status of the entities
     *  @return the entities
     */
    @Transactional(readOnly = true)
    public List<TwitterAccount> findAllByStatus(TwitterStatus status) {
        log.debug("Request to get TwitterAccounts by status : {}", status);
        return twitterAccountRepository.findAllByStatus(status);
    }

    /**
     *  Get twitterAccounts by header.
     *
     *  @param header the entity we search by
     *  @return the entities
     */
    @Transactional(readOnly = true)
    public List<TwitterAccount> findAllByHeader(Header header) {
        log.debug("Request to get TwitterAccounts by header : {}", header);
        return twitterAccountRepository.findAllByHeader(header);
    }

    /**
     *  Get twitterAccounts by avatar.
     *
     *  @param avatar the entity we search by
     *  @return the entities
     */
    @Transactional(readOnly = true)
    public List<TwitterAccount> findAllByAvatar(Avatar avatar) {
        log.debug("Request to get TwitterAccounts by avatar : {}", avatar);
        return twitterAccountRepository.findAllByAvatar(avatar);
    }

    /**
     *  Get the number of proxies assigned.
     *
     *  @param proxy the entity we count by
     *  @return false if exceds
     */
    @Transactional(readOnly = true)
    public boolean countAllByProxy(Proxy proxy) {
        log.debug("Request to count the number of proxies assigned : {}", proxy);
        return twitterAccountRepository.countAllByProxy(proxy) >=
            twitterSettingsService.findOne().getAccountsPerProxy();
    }

    /**
     *  Get twitterAccounts by message.
     *
     *  @param twitterMessage the entity we search by
     *  @return the entities
     */
    @Transactional(readOnly = true)
    public List<TwitterAccount> findAllByMessage(TwitterMessage twitterMessage) {
        log.debug("Request to get TwitterAccounts by twitter message : {}", twitterMessage);
        return twitterAccountRepository.findAllByMessage(twitterMessage);
    }

    /**
     *  Update accounts to PENDING_UPDATE status.
     *
     *  @param twitterAccounts the entities we update
     */
    public void switchToPendingUpdate(List<TwitterAccount> twitterAccounts) {
        for (TwitterAccount account : twitterAccounts) {
            if (account.getStatus() == TwitterStatus.PENDING_UPDATE)
                continue; // no point updating
            account.setPrevStatus(account.getStatus());
            account.setStatus(TwitterStatus.PENDING_UPDATE);
            save(account); // we update related accounts
        }
    }

    /**
     *  Filter some unused info to keep the main page slim.
     *
     *  @param page the entities we need to clean
     */
    private Page<TwitterAccount> filterExtra(Page<TwitterAccount> page){
        for (TwitterAccount twitterAccount : page.getContent()) {
            twitterAccount.setConsumerKey("");
            twitterAccount.consumerSecret("");
            twitterAccount.accessToken("");
            twitterAccount.accessTokenSecret("");
            twitterAccount.getHeader().setImage(new byte[0]);
            twitterAccount.getHeader().setImageContentType("");
            twitterAccount.getAvatar().setImage(new byte[0]);
            twitterAccount.getAvatar().setImageContentType("");
            twitterAccount.getProxy().setUsername("");
            twitterAccount.getProxy().setPassword("");
            twitterAccount.getMessage().setAccountDescription("");
            twitterAccount.getMessage().setAccountLocation("");
            twitterAccount.getMessage().setAccountUrl("");
        }
        return page;
    }

    /**
     *  Filter some unused info to keep the main page slim.
     *
     *  @param twitterAccount the entity we need to clean
     */
    private TwitterAccount filterExtraSingle(TwitterAccount twitterAccount){
            if (twitterAccount == null) return null;
            twitterAccount.getHeader().setImage(new byte[0]);
            twitterAccount.getHeader().setImageContentType("");
            twitterAccount.getAvatar().setImage(new byte[0]);
            twitterAccount.getAvatar().setImageContentType("");
            twitterAccount.getProxy().setUsername("");
            twitterAccount.getProxy().setPassword("");
            twitterAccount.getMessage().setAccountDescription("");
            twitterAccount.getMessage().setAccountLocation("");
            twitterAccount.getMessage().setAccountUrl("");

        return twitterAccount;
    }

    /**
     *  Get one twitterAccount by consumer key.
     *
     *  @param consumerKey the consumer key of the account
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TwitterAccount> findOneByConsumerKey(String consumerKey) {
        log.debug("Request to get TwitterAccount : {}", consumerKey);
        return twitterAccountRepository.findOneByConsumerKey(consumerKey);
    }
}
