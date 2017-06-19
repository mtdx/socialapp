package com.ninja.socialapp.repository;

import com.ninja.socialapp.domain.TwitterSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data JPA repository for the TwitterSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TwitterSettingsRepository extends JpaRepository<TwitterSettings,Long> {

    @Query("select twitterSettings from TwitterSettings twitterSettings order by twitterSettings.id desc")
    Optional<TwitterSettings> findOne();
}
