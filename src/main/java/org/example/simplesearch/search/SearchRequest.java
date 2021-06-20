package org.example.simplesearch.search;

import org.example.simplesearch.search.commands.FieldNode;
import org.example.simplesearch.search.commands.IndexNameNode;
import org.example.simplesearch.search.commands.QueryNode;

import java.util.Objects;

public class SearchRequest {

  private final IndexNameNode indexName;

  private final FieldNode field;

  private final QueryNode query;

  public SearchRequest(IndexNameNode indexName, FieldNode field, QueryNode query) {
    this.indexName = indexName;
    this.field = field;
    this.query = query;
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
