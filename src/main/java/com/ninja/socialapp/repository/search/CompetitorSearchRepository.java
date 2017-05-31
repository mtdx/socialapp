package com.ninja.socialapp.repository.search;

import com.ninja.socialapp.domain.Competitor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Competitor entity.
 */
public interface CompetitorSearchRepository extends ElasticsearchRepository<Competitor, Long> {
}
