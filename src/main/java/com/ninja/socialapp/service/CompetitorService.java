package com.ninja.socialapp.service;

import com.ninja.socialapp.domain.Competitor;
import com.ninja.socialapp.domain.enumeration.CompetitorStatus;
import com.ninja.socialapp.repository.CompetitorRepository;
import com.ninja.socialapp.repository.search.CompetitorSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Competitor.
 */
@Service
@Transactional
public class CompetitorService {

    private final Logger log = LoggerFactory.getLogger(CompetitorService.class);

    private final CompetitorRepository competitorRepository;

    private final CompetitorSearchRepository competitorSearchRepository;

    public CompetitorService(CompetitorRepository competitorRepository, CompetitorSearchRepository competitorSearchRepository) {
        this.competitorRepository = competitorRepository;
        this.competitorSearchRepository = competitorSearchRepository;
    }

    /**
     * Save a competitor.
     *
     * @param competitor the entity to save
     * @return the persisted entity
     */
    public Competitor save(Competitor competitor) {
        log.debug("Request to save Competitor : {}", competitor);
        Competitor result = competitorRepository.save(competitor);
        competitorSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the competitors.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Competitor> findAll(Pageable pageable) {
        log.debug("Request to get all Competitors");
        return competitorRepository.findAll(pageable);
    }

    /**
     *  Get one competitor by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Competitor findOne(Long id) {
        log.debug("Request to get Competitor : {}", id);
        return competitorRepository.findOne(id);
    }

    /**
     *  Delete the  competitor by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Competitor : {}", id);
        competitorRepository.delete(id);
        competitorSearchRepository.delete(id);
    }

    /**
     * Search for the competitor corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Competitor> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Competitors for query {}", query);
        Page<Competitor> result = competitorSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    /**
     *  Get Competitor by status.
     *
     *  @param status the of the entities
     *  @return the entities
     */
    @Transactional(readOnly = true)
    public Optional<Competitor> findFirstByStatusOrderByIdAsc(CompetitorStatus status) {
        log.debug("Request to get Competitors by status : {}", status);
        return competitorRepository.findFirstByStatusOrderByIdAsc(status);
    }

    /**
     *  Updates the number of likes.
     *
     *  @param likes the number of likes
     *  @param id the id of the entity
     */
    public void incrementLikes(int likes, Long id) {
        competitorRepository.incrementLikes(likes, id);
    }
}
