package com.ninja.socialapp.repository;

import com.ninja.socialapp.domain.TwitterSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the TwitterSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TwitterSettingsRepository extends JpaRepository<TwitterSettings,Long> {

}
