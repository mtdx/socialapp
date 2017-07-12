package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.Avatar;
import com.ninja.socialapp.domain.TwitterAccount;
import com.ninja.socialapp.domain.enumeration.TwitterStatus;
import com.ninja.socialapp.repository.AvatarRepository;
import com.ninja.socialapp.repository.search.AvatarSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Avatar.
 */
@Service
@Transactional
public class AvatarService {

    private final Logger log = LoggerFactory.getLogger(AvatarService.class);

    private final AvatarRepository avatarRepository;

    private final AvatarSearchRepository avatarSearchRepository;

    private final TwitterAccountService twitterAccountService;

    public AvatarService(AvatarRepository avatarRepository, AvatarSearchRepository avatarSearchRepository, TwitterAccountService twitterAccountService) {
        this.avatarRepository = avatarRepository;
        this.avatarSearchRepository = avatarSearchRepository;
        this.twitterAccountService = twitterAccountService;
    }

    /**
     * Save a avatar.
     *
     * @param avatar the entity to save
     * @return the persisted entity
     */
    public Avatar save(Avatar avatar) {
        log.debug("Request to save Avatar : {}", avatar);
        Avatar result = avatarRepository.save(avatar);
        avatarSearchRepository.save(result);
        twitterAccountService.switchToPendingUpdate(twitterAccountService.findAllByAvatar(avatar));
        return result;
    }

    /**
     *  Get all the avatars.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Avatar> findAll(Pageable pageable) {
        log.debug("Request to get all Avatars");
        return avatarRepository.findAll(pageable);
    }

    /**
     *  Get one avatar by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Avatar findOne(Long id) {
        log.debug("Request to get Avatar : {}", id);
        return avatarRepository.findOne(id);
    }

    /**
     *  Delete the  avatar by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Avatar : {}", id);
        avatarRepository.delete(id);
        avatarSearchRepository.delete(id);
    }

    /**
     * Search for the avatar corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Avatar> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Avatars for query {}", query);
        Page<Avatar> result = avatarSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
