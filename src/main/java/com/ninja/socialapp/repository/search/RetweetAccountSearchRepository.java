package com.ninja.socialapp.repository.search;

import com.ninja.socialapp.domain.RetweetAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RetweetAccount entity.
 */
public interface RetweetAccountSearchRepository extends ElasticsearchRepository<RetweetAccount, Long> {
}
