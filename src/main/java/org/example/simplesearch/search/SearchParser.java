package org.example.simplesearch.search;

import org.example.simplesearch.search.commands.FieldNode;
import org.example.simplesearch.search.commands.IndexNameNode;
import org.example.simplesearch.search.commands.QueryNode;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SearchParser {

  private final SearchLexer lexer;

  public SearchParser(String source) {
    this.lexer = new SearchLexer(source);
  }

  /**
   * Syntax of a search query is:
   * <p>
   * IDENTIFIER DOT IDENTIFIER EQUALS IDENTIFIER
   *
   * @return The full search request.
   * @throws InvalidSearchSyntax In case the syntax is invalid.
   */
  SearchRequest parse() throws InvalidSearchSyntax {
    List<Token> tokens = lexer.readTokens();
    Iterator<Token> tokenIterator = tokens.iterator();

    try {
      Token indexName = tokenIterator.next();
      Token dot = tokenIterator.next();
      Token fieldName = tokenIterator.next();
      Token equals = tokenIterator.next();
      Token query = tokenIterator.next();
      Token end = tokenIterator.next();
      validateSyntax(indexName, dot, fieldName, equals, query, end);

      // The syntax is now confirmed to be valid, we can now build the search request.
      IndexNameNode indexNameNode = new IndexNameNode(indexName.getLiteral());
      FieldNode fieldNode = new FieldNode(fieldName.getLiteral());
      QueryNode queryNode = new QueryNode(query.getLiteral());
      return new SearchRequest(indexNameNode, fieldNode, queryNode);
    } catch (NoSuchElementException e) {
      throw new InvalidSearchSyntax("Failed to parse query. Query must be of form: NAME.FIELD=QUERY");
    }
  }

  private void validateSyntax(Token indexName, Token dot, Token fieldName, Token equals, Token query, Token end)
      throws InvalidSearchSyntax {
    if (indexName.getTokenType() != SearchTokenType.IDENTIFIER) {
      throw new InvalidSearchSyntax("Missing index name.");
    }

    if (dot.getTokenType() != SearchTokenType.DOT) {
      throw new InvalidSearchSyntax("Missing dot in between the index name and the field name.");
    }

    if (fieldName.getTokenType() != SearchTokenType.IDENTIFIER) {
      throw new InvalidSearchSyntax("Missing field name.");
    }

    if (equals.getTokenType() != SearchTokenType.EQUALS) {
      throw new InvalidSearchSyntax("Missing equals between field name and query.");
    }

    if (query.getTokenType() != SearchTokenType.IDENTIFIER) {
      throw new InvalidSearchSyntax("Missing query.");
    }

    if (end.getTokenType() != SearchTokenType.EOF) {
      throw new InvalidSearchSyntax("Unknown trailing tokens found. Query must be of form: NAME.FIELD=QUERY");
    }
  }
}
