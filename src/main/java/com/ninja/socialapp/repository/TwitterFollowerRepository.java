package com.ninja.socialapp.repository;

import com.ninja.socialapp.domain.TwitterFollower;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TwitterFollower entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TwitterFollowerRepository extends JpaRepository<TwitterFollower,Long> {

}
