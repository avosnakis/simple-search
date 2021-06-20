package org.example.simplesearch.search;

import java.util.Collections;
import java.util.List;

/**
 * A lexer that will read tokens from a search query.
 */
public class SearchLexer {

  private final String source;

  SearchLexer(String source) {
    this.source = source;
  }

  /**
   * Reads tokens out of the source string.
   *
   * @return A list of all discovered tokens.
   */
  List<Token> readTokens() {
    return Collections.emptyList();
  }
}
