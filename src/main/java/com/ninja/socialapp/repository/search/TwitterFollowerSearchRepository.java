package com.ninja.socialapp.repository.search;

import com.ninja.socialapp.domain.TwitterFollower;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TwitterFollower entity.
 */
public interface TwitterFollowerSearchRepository extends ElasticsearchRepository<TwitterFollower, Long> {
}
