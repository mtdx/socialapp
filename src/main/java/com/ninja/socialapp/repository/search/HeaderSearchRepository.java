package com.ninja.socialapp.repository.search;

import com.ninja.socialapp.domain.Header;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Header entity.
 */
public interface HeaderSearchRepository extends ElasticsearchRepository<Header, Long> {
}
