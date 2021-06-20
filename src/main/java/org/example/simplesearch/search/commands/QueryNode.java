package org.example.simplesearch.search.commands;

import org.example.simplesearch.search.SearchVisitor;

import java.util.Objects;

public class QueryNode implements SearchNode {

  private final String query;

  public QueryNode(String query) {
    this.query = query;
  }

  @Override
  public void accept(SearchVisitor visitor) {

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    QueryNode queryNode = (QueryNode) o;
    return Objects.equals(query, queryNode.query);
  }

  @Override
  public int hashCode() {
    return Objects.hash(query);
  }

  @Override
  public String toString() {
    return "QueryNode{" +
        "query='" + query + '\'' +
        '}';
  }
}
