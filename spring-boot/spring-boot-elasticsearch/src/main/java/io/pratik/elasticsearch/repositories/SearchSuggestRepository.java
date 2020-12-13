/**
 * 
 */
package io.pratik.elasticsearch.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import io.pratik.elasticsearch.models.SearchSuggest;

/**
 * @author Pratik Das
 *
 */
@Repository
public interface SearchSuggestRepository extends ElasticsearchRepository<SearchSuggest, String> {
}
