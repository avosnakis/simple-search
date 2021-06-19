package org.example.simplesearch.index;

import org.example.simplesearch.search.SearchResult;

import java.util.Collections;

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
    return new SearchResult(Collections.emptySet());
  }
}