package org.example.simplesearch.search;

import java.util.Objects;

/**
 * Represents a search request.
 */
public class SearchRequest {

  private final String indexName;

  private final String field;

  private final String query;

  public SearchRequest(String indexName, String field, String query) {
    this.indexName = indexName;
    this.field = field;
    this.query = query;
  }

  public String getIndexName() {
    return indexName;
  }

  public String getField() {
    return field;
  }

  public String getQuery() {
    return query;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SearchRequest request = (SearchRequest) o;
    return Objects.equals(indexName, request.indexName) && Objects.equals(field, request.field) && Objects.equals(query, request.query);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indexName, field, query);
  }

  @Override
  public String toString() {
    return "SearchRequest{" +
        "indexName=" + indexName +
        ", field=" + field +
        ", query=" + query +
        '}';
  }
}
