package org.example.simplesearch.search;

import org.example.simplesearch.search.commands.FieldNode;
import org.example.simplesearch.search.commands.IndexNameNode;
import org.example.simplesearch.search.commands.QueryNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchParserTest {

  @Test
  void givenValidQuery_whenParsed_successfullyOutputs() throws InvalidSearchSyntax {
    SearchParser parser = new SearchParser("organisation.test=TEST");
    SearchRequest request = parser.parse();

    assertEquals(
        new SearchRequest(new IndexNameNode("organisation"), new FieldNode("test"), new QueryNode("TEST")),
        request
    );
  }

  @Test
  void givenQueryMissingIdentifier_whenParsed_throwsException() {
    SearchParser parser = new SearchParser(".test=TEST");

    assertThrows(InvalidSearchSyntax.class, parser::parse);
  }

  @Test
  void givenQueryMissingDot_whenParsed_throwsException() {
    SearchParser parser = new SearchParser("organisation test=TEST");

    assertThrows(InvalidSearchSyntax.class, parser::parse);
  }

  @Test
  void givenQueryMissingEquals_whenParsed_throwsException() {
    SearchParser parser = new SearchParser("organisation.test TEST");

    assertThrows(InvalidSearchSyntax.class, parser::parse);
  }

  @Test
  void givenQueryMissingFieldVal_whenParsed_throwsException() {
    SearchParser parser = new SearchParser("organisation.test=");

    assertThrows(InvalidSearchSyntax.class, parser::parse);
  }

  @Test
  void givenQueryHasUnknownCharacters_whenParsed_throwsException() {
    SearchParser parser = new SearchParser("~.test=TEST");

    assertThrows(InvalidSearchSyntax.class, parser::parse);
  }
}