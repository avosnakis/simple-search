package org.example.simplesearch.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A lexer that will read tokens from a search query.
 */
public class SearchLexer {

  private static final Logger LOGGER = LoggerFactory.getLogger(SearchLexer.class);

  private final String source;

  /**
   * The index the lexer is currently pointed at.
   */
  private int position = 0;

  /**
   * The index of the character to start on for the current token.
   */
  private int start = 0;

  private final List<Token> tokens = new ArrayList<>();

  SearchLexer(String source) {
    this.source = source;
  }

  /**
   * Reads tokens out of the source string.
   *
   * @return A list of all discovered tokens.
   */
  List<Token> readTokens() {
    LOGGER.trace("Reading tokens from query {}", this.source);

    while (!finished()) {
      readToken();
      this.start = this.position;
    }

    tokens.add(new Token(SearchTokenType.EOF, ""));
    return tokens;
  }

  private void readToken() {
    LOGGER.trace("Reading tokens, starting at {}", start);

    char curr = advance();
    switch (curr) {
      case '.':
        tokens.add(new Token(SearchTokenType.DOT, "."));
        break;
      case '=':
        tokens.add(new Token(SearchTokenType.EQUALS, "="));
        break;
      case '\"':
        tokens.add(readRawIdentifier());
        break;
      case ' ':
      case '\n':
      case '\r':
      case '\t':
        // Whitespace can be ignored.
        break;
      default:
        if (Character.isLetterOrDigit(curr)) {
          tokens.add(readAlphaNumericIdentifier());
        } else {
          // It is an unknown character, we will emit it as an unknown token to report the error.
          tokens.add(new Token(SearchTokenType.UNKNOWN, Character.toString(curr)));
        }
        break;
    }
  }

  private Token readRawIdentifier() {
    while (peek() != '\"' && peek() != '\0') {
      advance();
    }

    // Extract the literal, taking off the starting double quote.
    String literal = source.substring(start + 1, position);
    if (peek() != '\"') {
      // If the the raw identifier has not terminated with another double quote, emit an unknown.
      return new Token(SearchTokenType.UNKNOWN, literal);
    } else {
      // The next character is a double quote, so consume it and emit the identifier.
      advance();
      return new Token(SearchTokenType.IDENTIFIER, literal);
    }
  }

  private Token readAlphaNumericIdentifier() {
    while (Character.isLetterOrDigit(peek())) {
      advance();
    }

    String literal = source.substring(start, position);
    return new Token(SearchTokenType.IDENTIFIER, literal);
  }

  /**
   * @return The next char, without updating the position. Returns a null byte if at the end.
   */
  private char peek() {
    if (finished()) {
      return '\0';
    } else {
      return source.charAt(position);
    }
  }

  /**
   * Advances the lexer's position by 1 and returns the character it is now pointing at.
   *
   * @return the next character.
   */
  private char advance() {
    char character = source.charAt(position);
    position++;
    return character;
  }

  /**
   * @return Whether the lexer's current position is beyond the end of the source string.
   */
  private boolean finished() {
    return position >= source.length();
  }
}
