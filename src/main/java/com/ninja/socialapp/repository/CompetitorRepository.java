package com.ninja.socialapp.repository;

import com.ninja.socialapp.domain.Competitor;
import com.ninja.socialapp.domain.enumeration.CompetitorStatus;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data JPA repository for the Competitor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompetitorRepository extends JpaRepository<Competitor,Long> {

    Optional<Competitor> findFirstByStatusOrderByIdAsc(CompetitorStatus status);

    @Modifying
    @Query("update Competitor competitor set competitor.likes = competitor.likes + :likes where competitor.id = :id")
    void incrementLikes(@Param("likes") final Long likes, @Param("id") final Long id);

    List<Competitor> findAllByStatus(CompetitorStatus status);

    Optional<Competitor> findByUserid(String userId);

    Integer countAllByStatus(CompetitorStatus status);

    @Query("select competitor from Competitor competitor where competitor.created <= :instant and competitor.status = :status")
    List<Competitor> findOlderThanByStatus(@Param("instant") final Instant instant, @Param("status") final CompetitorStatus status);
}
