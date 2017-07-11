package com.ninja.socialapp.repository;

import com.ninja.socialapp.domain.TwitterKeyword;
import com.ninja.socialapp.domain.enumeration.KeywordStatus;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;


/**
 * Spring Data JPA repository for the TwitterKeyword entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TwitterKeywordRepository extends JpaRepository<TwitterKeyword,Long> {

    Optional<TwitterKeyword> findFirstByStatusOrderByIdAsc(KeywordStatus status);
}
