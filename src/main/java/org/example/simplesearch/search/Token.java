package org.example.simplesearch.search;

/**
 * A token that has been read from a search query.
 */
public class Token {

  private final SearchTokenType tokenType;

  private final String literal;

  Token(SearchTokenType tokenType, String literal) {
    this.tokenType = tokenType;
    this.literal = literal;
  }
}
