package com.ninja.socialapp.repository;

import com.ninja.socialapp.domain.TwitterAccount;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TwitterAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TwitterAccountRepository extends JpaRepository<TwitterAccount,Long> {
    
}
