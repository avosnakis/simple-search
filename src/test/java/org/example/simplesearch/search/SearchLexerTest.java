package org.example.simplesearch.search;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchLexerTest {

  private static final Token EOF = new Token(
      SearchTokenType.EOF,
      ""
  );

  @Test
  void whenInputtingAlphanumericWord_lexesWord() {
    SearchLexer lexer = new SearchLexer("TEST");
    List<Token> tokens = lexer.readTokens();

    assertEquals(List.of(new Token(SearchTokenType.IDENTIFIER, "TEST"), EOF), tokens);
  }

  @Test
  void whenInputtingEquals_lexesEquals() {
    SearchLexer lexer = new SearchLexer("=");
    List<Token> tokens = lexer.readTokens();

    assertEquals(List.of(new Token(SearchTokenType.EQUALS, "="), EOF), tokens);
  }

  @Test
  void whenInputtingDot_lexesDot() {
    SearchLexer lexer = new SearchLexer(".");
    List<Token> tokens = lexer.readTokens();

    assertEquals(List.of(new Token(SearchTokenType.DOT, "."), EOF), tokens);
  }

  @Test
  void givenSpacesInQuery_whenInputtingQuery_spacesIgnored() {
    SearchLexer lexer = new SearchLexer("test . \"_id\" = 123");
    List<Token> tokens = lexer.readTokens();

    assertEquals(
        List.of(
            new Token(SearchTokenType.IDENTIFIER, "test"),
            new Token(SearchTokenType.DOT, "."),
            new Token(SearchTokenType.IDENTIFIER, "_id"),
            new Token(SearchTokenType.EQUALS, "="),
            new Token(SearchTokenType.IDENTIFIER, "123"),
            EOF),
        tokens);
  }

  @Test
  void whenInputtingInvalidCharacter_lexesUnknown() {
    SearchLexer lexer = new SearchLexer("~");
    List<Token> tokens = lexer.readTokens();

    assertEquals(List.of(new Token(SearchTokenType.UNKNOWN, "~"), EOF), tokens);
  }

  @Test
  void givenDoubleQuoteAtStart_whenReadingAnyCharacter_lexesAsWordUntilNextDoubleQuote() {
    SearchLexer lexer = new SearchLexer("\"_id\"");
    List<Token> tokens = lexer.readTokens();

    assertEquals(List.of(new Token(SearchTokenType.IDENTIFIER, "_id"), EOF), tokens);
  }

  @Test
  void givenDoubleQuoteAtStart_whenReadingEquals_lexesAsWordUntilNextDoubleQuote() {
    SearchLexer lexer = new SearchLexer("\"=\"");
    List<Token> tokens = lexer.readTokens();

    assertEquals(List.of(new Token(SearchTokenType.IDENTIFIER, "="), EOF), tokens);
  }

  @Test
  void givenDoubleQuoteAtStart_whenNoTerminatingDoubleQuote_EmitsUnknown() {
    SearchLexer lexer = new SearchLexer("\"=");
    List<Token> tokens = lexer.readTokens();

    assertEquals(List.of(new Token(SearchTokenType.UNKNOWN, "="), EOF), tokens);
  }

  @Test
  void givenEscapedDoubleQuoteInIdentifier_whenLexed_emitsWithDoubleQuote() {
    SearchLexer lexer = new SearchLexer("\"test\\\"backslash\"");
    List<Token> tokens = lexer.readTokens();

    assertEquals(List.of(new Token(SearchTokenType.IDENTIFIER, "test\"backslash"), EOF), tokens);
  }

  @Test
  void givenTwoEscapedDoubleQuoteInIdentifier_whenLexed_emitsWithDoubleQuote() {
    SearchLexer lexer = new SearchLexer("\"test\\\"\\\"backslash\"");
    List<Token> tokens = lexer.readTokens();

    assertEquals(List.of(new Token(SearchTokenType.IDENTIFIER, "test\"\"backslash"), EOF), tokens);
  }
}