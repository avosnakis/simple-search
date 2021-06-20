package org.example.simplesearch.search;

import org.example.simplesearch.search.commands.FieldNode;
import org.example.simplesearch.search.commands.IndexNameNode;
import org.example.simplesearch.search.commands.QueryNode;

public class SearchRequest {

  private final IndexNameNode indexName;

  private final FieldNode field;

  private final QueryNode query;

  public SearchRequest(IndexNameNode indexName, FieldNode field, QueryNode query) {
    this.indexName = indexName;
    this.field = field;
    this.query = query;
  }
}
