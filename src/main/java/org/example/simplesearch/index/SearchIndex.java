package org.example.simplesearch.index;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.simplesearch.search.SearchResult;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Stores the documents and index for a particular set of documents.
 */
public class SearchIndex {

  private final DocumentStore store;
  private final DocumentInvertedIndex invertedIndex;

  SearchIndex(DocumentStore store, DocumentInvertedIndex invertedIndex) {
    this.store = store;
    this.invertedIndex = invertedIndex;
  }

  /**
   * Retrieves all documents where the specified field matches the specified value.
   * Documents that are related will also be retrieved.
   *
   * @param field The field.
   * @param value The value.
   * @return The documents matching, as well as related documents.
   */
  public SearchResult findMatchingDocs(String field, String value) {
    Set<Integer> matchingIds = invertedIndex.retrieveHits(field, value);
    Set<JsonNode> documents = matchingIds.stream()
        .map(store::retrieveDocument)
        .flatMap(Optional::stream)
        .collect(Collectors.toSet());
    return new SearchResult(documents);
  }
}
