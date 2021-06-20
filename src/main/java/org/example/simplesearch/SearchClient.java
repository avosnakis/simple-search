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
import java.util.stream.Collectors;

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
  SearchResult search(SearchRequest request, String idField) {
    SearchIndex searchIndex = indices.get(request.getIndexName());
    if (searchIndex == null) {
      LOGGER.warn("Failed to find index {}.", request.getIndexName());
      return new SearchResult(Collections.emptySet(), Collections.emptySet());
    }

    Set<JsonNode> primaryResults = searchIndex.findMatchingDocs(request.getField(), request.getQuery());
    Set<String> ids = primaryResults.stream()
        .map(jsonNode -> jsonNode.path(idField))
        .map(JsonNode::asText)
        .collect(Collectors.toSet());
    Set<JsonNode> relatedResults = findRelatedDocuments(request.getIndexName(), ids);
    return new SearchResult(primaryResults, relatedResults);
  }

  /**
   * Finds all documents which have foreign keys matching any of the specified IDs.
   *
   * @param originalIndex The index the search was originally performed on (ie. the index whose foreign keys will be used.)
   * @param originalIds   The original list of IDs that was returned.
   * @return Documents with one of the original IDs in one of the original indice's foreign keys.
   */
  private Set<JsonNode> findRelatedDocuments(String originalIndex, Set<String> originalIds) {
    Set<String> foreignKeys = keyMappings.retrieveKeysFor(originalIndex);
    Set<JsonNode> relatedDocuments = new HashSet<>();

    // now, look at each index to see if any of them have documents with foreign keys matching any of the original IDs.
    for (SearchIndex index : indices.values()) {

      // Need to do this for each foreign key for this index.
      for (String foreignKey : foreignKeys) {
        // And for each ID...
        for (String id : originalIds) {
          Set<JsonNode> related = index.findMatchingDocs(foreignKey, id);
          relatedDocuments.addAll(related);
        }
      }
    }

    return relatedDocuments;
  }
}