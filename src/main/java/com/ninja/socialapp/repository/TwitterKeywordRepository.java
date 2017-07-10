package com.ninja.socialapp.repository;

import com.ninja.socialapp.domain.TwitterKeyword;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TwitterKeyword entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TwitterKeywordRepository extends JpaRepository<TwitterKeyword,Long> {
    
}
