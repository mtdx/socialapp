package com.ninja.socialapp.repository.search;

import com.ninja.socialapp.domain.TwitterAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TwitterAccount entity.
 */
public interface TwitterAccountSearchRepository extends ElasticsearchRepository<TwitterAccount, Long> {
}
