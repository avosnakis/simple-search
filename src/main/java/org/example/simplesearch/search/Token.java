package org.example.simplesearch.search;

import java.util.Objects;

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

  public SearchTokenType getTokenType() {
    return tokenType;
  }

  public String getLiteral() {
    return literal;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Token token = (Token) o;
    return tokenType == token.tokenType && Objects.equals(literal, token.literal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tokenType, literal);
  }

  @Override
  public String toString() {
    return "Token{" +
        "tokenType=" + tokenType +
        ", literal='" + literal + '\'' +
        '}';
  }
}
