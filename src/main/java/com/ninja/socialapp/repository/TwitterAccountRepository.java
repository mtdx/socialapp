package com.ninja.socialapp.repository;

import com.ninja.socialapp.domain.*;
import com.ninja.socialapp.domain.enumeration.TwitterStatus;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data JPA repository for the TwitterAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TwitterAccountRepository extends JpaRepository<TwitterAccount,Long> {

    List<TwitterAccount> findAllByStatus(TwitterStatus status);

    List<TwitterAccount> findAllByHeader(Header header);

    List<TwitterAccount> findAllByAvatar(Avatar avatar);

    List<TwitterAccount> findAllByMessage(TwitterMessage message);

    Optional<TwitterAccount> findOneByConsumerKey(String consumerKey);

    Integer countAllByProxy(Proxy proxy);
}
