package org.example.simplesearch.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchParserTest {

  @Test
  void givenValidQuery_whenParsed_successfullyOutputs() throws InvalidSearchSyntax {
    SearchParser parser = new SearchParser("organisation.test=TEST");
    SearchRequest request = parser.parse();

    assertEquals(
        new SearchRequest("organisation", "test", "TEST"),
        request
    );
  }

  @Test
  void givenQueryMissingIdentifier_whenParsed_throwsException() {
    SearchParser parser = new SearchParser("..test=TEST");

    assertThrows(InvalidSearchSyntax.class, parser::parse);
  }

  @Test
  void givenQueryMissingDot_whenParsed_throwsException() {
    SearchParser parser = new SearchParser("organisation=test=TEST");

    assertThrows(InvalidSearchSyntax.class, parser::parse);
  }

  @Test
  void givenQuerySecondIdentifierDot_whenParsed_throwsException() {
    SearchParser parser = new SearchParser("organisation..=TEST");

    assertThrows(InvalidSearchSyntax.class, parser::parse);
  }

  @Test
  void givenQueryMissingEquals_whenParsed_throwsException() {
    SearchParser parser = new SearchParser("organisation.test.TEST");

    assertThrows(InvalidSearchSyntax.class, parser::parse);
  }

  @Test
  void givenQueryMissingFieldVal_whenParsed_throwsException() {
    SearchParser parser = new SearchParser("organisation.test=.");

    assertThrows(InvalidSearchSyntax.class, parser::parse);
  }

  @Test
  void givenQueryHasUnknownCharacters_whenParsed_throwsException() {
    SearchParser parser = new SearchParser("~.test=TEST");

    assertThrows(InvalidSearchSyntax.class, parser::parse);
  }

  @Test
  void givenQueryHasNotEnoughTokens_whenParsed_throwsException() {
    SearchParser parser = new SearchParser("organisation test=TEST");

    assertThrows(InvalidSearchSyntax.class, parser::parse);
  }

  @Test
  void givenQueryHasExtraTokens_whenParsed_throwsException() {
    SearchParser parser = new SearchParser("organisation.test=TEST.");

    assertThrows(InvalidSearchSyntax.class, parser::parse);
  }

}