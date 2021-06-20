package org.example.simplesearch;

import org.example.simplesearch.index.SearchIndex;
import org.example.simplesearch.search.SearchRequest;
import org.example.simplesearch.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class SearchClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(SearchClient.class);

  private final Map<String, SearchIndex> indices;

  public SearchClient(Map<String, SearchIndex> indices) {
    this.indices = indices;
  }

  SearchResult search(SearchRequest request) {
    SearchIndex searchIndex = indices.get(request.getIndexName());
    if (searchIndex == null) {
      LOGGER.warn("Failed to find index {}.", request.getIndexName());
      return new SearchResult(Collections.emptySet());
    }

    return searchIndex.findMatchingDocs(request.getField(), request.getQuery());
  }
}