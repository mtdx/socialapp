package com.ninja.socialapp.repository;

import com.ninja.socialapp.domain.Proxy;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Proxy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProxyRepository extends JpaRepository<Proxy,Long> {

}
