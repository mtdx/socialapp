package com.ninja.socialapp.repository;

import com.ninja.socialapp.domain.Proxy;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Proxy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProxyRepository extends JpaRepository<Proxy,Long> {

    @Query("select proxy from Proxy proxy where proxy.id NOT IN (select twitterAccount.proxy.id from TwitterAccount twitterAccount GROUP BY twitterAccount.proxy.id HAVING count(twitterAccount.proxy.id) > :num)")
    List<Proxy> findAllRestrict(@Param("num") final Long num);

    List<Proxy> findAllByUsernameAndPasswordOrderById(String username, String password);
}
