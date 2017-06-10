package com.ninja.socialapp.repository.search;

import com.ninja.socialapp.domain.TwitterError;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TwitterError entity.
 */
public interface TwitterErrorSearchRepository extends ElasticsearchRepository<TwitterError, Long> {
}
