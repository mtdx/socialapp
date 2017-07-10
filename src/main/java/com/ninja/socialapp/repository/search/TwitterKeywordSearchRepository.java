package com.ninja.socialapp.repository.search;

import com.ninja.socialapp.domain.TwitterKeyword;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TwitterKeyword entity.
 */
public interface TwitterKeywordSearchRepository extends ElasticsearchRepository<TwitterKeyword, Long> {
}
