package com.ninja.socialapp.repository;

import com.ninja.socialapp.domain.TwitterError;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.Instant;
import java.util.List;


/**
 * Spring Data JPA repository for the TwitterError entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TwitterErrorRepository extends JpaRepository<TwitterError,Long> {

    @Query("select twitterError.id from TwitterError twitterError where twitterError.created_at <= :instant")
    List<Long> findOlderThan(@Param("instant") final Instant instant);
}
