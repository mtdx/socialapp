package com.ninja.socialapp.repository;

import com.ninja.socialapp.domain.TwitterError;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TwitterError entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TwitterErrorRepository extends JpaRepository<TwitterError,Long> {
    
}
