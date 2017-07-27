package com.ninja.socialapp.repository;

import com.ninja.socialapp.domain.RetweetAccount;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RetweetAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RetweetAccountRepository extends JpaRepository<RetweetAccount,Long> {
    
}
