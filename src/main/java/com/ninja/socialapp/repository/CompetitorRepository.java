package com.ninja.socialapp.repository;

import com.ninja.socialapp.domain.Competitor;
import com.ninja.socialapp.domain.enumeration.CompetitorStatus;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;


/**
 * Spring Data JPA repository for the Competitor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompetitorRepository extends JpaRepository<Competitor,Long> {

    Optional<Competitor> findFirstByStatusOrderByIdAsc(CompetitorStatus status);
}
