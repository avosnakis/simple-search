package org.example.simplesearch.search;

import org.example.simplesearch.search.commands.FieldNode;
import org.example.simplesearch.search.commands.IndexNameNode;
import org.example.simplesearch.search.commands.QueryNode;

import java.util.List;

public class SearchParser {

  SearchRequest parseTokens(List<Token> tokens) {
    return new SearchRequest(new IndexNameNode(), new FieldNode(), new QueryNode());
  }
}
