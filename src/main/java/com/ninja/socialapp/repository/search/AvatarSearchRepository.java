package com.ninja.socialapp.repository.search;

import com.ninja.socialapp.domain.Avatar;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Avatar entity.
 */
public interface AvatarSearchRepository extends ElasticsearchRepository<Avatar, Long> {
}
