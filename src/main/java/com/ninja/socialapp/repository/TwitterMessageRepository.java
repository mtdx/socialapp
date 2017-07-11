package com.ninja.socialapp.repository;

import com.ninja.socialapp.domain.TwitterMessage;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TwitterMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TwitterMessageRepository extends JpaRepository<TwitterMessage,Long> {
    
}
