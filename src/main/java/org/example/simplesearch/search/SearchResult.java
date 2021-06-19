package org.example.simplesearch.search;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;
import java.util.Set;

public class SearchResult {

  private final Set<JsonNode> primaryResults;

  public SearchResult(Set<JsonNode> primaryResults) {
    this.primaryResults = primaryResults;
  }

  public Set<JsonNode> getPrimaryResults() {
    return primaryResults;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SearchResult that = (SearchResult) o;
    return Objects.equals(primaryResults, that.primaryResults);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primaryResults);
  }

  @Override
  public String toString() {
    return "SearchResult{" +
        "primaryResults=" + primaryResults +
        '}';
  }
}
