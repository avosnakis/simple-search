package org.example.simplesearch;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.simplesearch.index.SearchIndex;
import org.example.simplesearch.search.SearchRequest;
import org.example.simplesearch.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Interface for conducting searches.
 */
public class SearchClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(SearchClient.class);

  private final Map<String, SearchIndex> indices;
  private final KeyMappings keyMappings;

  public SearchClient(Map<String, SearchIndex> indices, KeyMappings keyMappings) {
    this.indices = indices;
    this.keyMappings = keyMappings;
  }

  /**
   * Searches the specified index.
   *
   * @param request The search request.
   * @return The search result.
   */
  SearchResult search(SearchRequest request) {
    SearchIndex searchIndex = indices.get(request.getIndexName());
    if (searchIndex == null) {
      LOGGER.warn("Failed to find index {}.", request.getIndexName());
      return new SearchResult(Collections.emptySet());
    }

    return searchIndex.findMatchingDocs(request.getField(), request.getQuery());
  }

  /**
   * Finds all documents which have foreign keys matching any of the specified IDs.
   *
   * @param originalIndex The index the search was originally performed on (ie. the index whose foreign keys will be used.)
   * @param originalIds   The original list of IDs that was returned.
   * @return Documents with one of the original IDs in one of the original indice's foreign keys.
   */
  Set<JsonNode> findRelatedDocuments(String originalIndex, Set<Integer> originalIds) {
    Set<String> foreignKeys = keyMappings.retrieveKeysFor(originalIndex);
    Set<JsonNode> relatedDocuments = new HashSet<>();

    // now, look at each index to see if any of them have documents with foreign keys matching any of the original IDs.
    for (SearchIndex index : indices.values()) {

      // need to do this for each foreign key.
      for (String foreignKey : foreignKeys) {
        // and for each ID...
        for (int id : originalIds) {
          SearchResult related = index.findMatchingDocs(foreignKey, Integer.toString(id));
          relatedDocuments.addAll(related.getPrimaryResults());
        }
      }
    }

    return relatedDocuments;
  }
}