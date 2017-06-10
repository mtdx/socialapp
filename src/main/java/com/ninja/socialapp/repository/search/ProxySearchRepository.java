package com.ninja.socialapp.repository.search;

import com.ninja.socialapp.domain.Proxy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Proxy entity.
 */
public interface ProxySearchRepository extends ElasticsearchRepository<Proxy, Long> {
}
