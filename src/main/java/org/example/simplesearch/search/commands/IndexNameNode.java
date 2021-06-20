package org.example.simplesearch.search.commands;

import org.example.simplesearch.search.SearchVisitor;

import java.util.Objects;

public class IndexNameNode implements SearchNode {

  private final String indexName;

  public IndexNameNode(String indexName) {
    this.indexName = indexName;
  }

  @Override
  public void accept(SearchVisitor visitor) {

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    IndexNameNode that = (IndexNameNode) o;
    return Objects.equals(indexName, that.indexName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indexName);
  }
}
