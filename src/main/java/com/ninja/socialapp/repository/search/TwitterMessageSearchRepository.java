package com.ninja.socialapp.repository.search;

import com.ninja.socialapp.domain.TwitterMessage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TwitterMessage entity.
 */
public interface TwitterMessageSearchRepository extends ElasticsearchRepository<TwitterMessage, Long> {
}
