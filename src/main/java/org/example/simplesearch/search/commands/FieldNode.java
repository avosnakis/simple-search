package org.example.simplesearch.search.commands;

import org.example.simplesearch.search.SearchVisitor;

import java.util.Objects;

public class FieldNode implements SearchNode {

  private final String field;

  public FieldNode(String field) {
    this.field = field;
  }

  @Override
  public void accept(SearchVisitor visitor) {

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FieldNode fieldNode = (FieldNode) o;
    return Objects.equals(field, fieldNode.field);
  }

  @Override
  public int hashCode() {
    return Objects.hash(field);
  }

  @Override
  public String toString() {
    return "FieldNode{" +
        "field='" + field + '\'' +
        '}';
  }
}
