package org.example.simplesearch.search;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Set;

public class SearchResult {

  private final Set<JsonNode> primaryResults;
  private final Set<JsonNode> relatedResults;

  public SearchResult(Set<JsonNode> primaryResults, Set<JsonNode> relatedResults) {
    this.primaryResults = primaryResults;
    this.relatedResults = relatedResults;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SearchResult that = (SearchResult) o;
    return Objects.equals(primaryResults, that.primaryResults);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primaryResults);
  }

  @Override
  public String toString() {
    return "SearchResult{" +
        "primaryResults=" + primaryResults +
        '}';
  }

  /**
   * Prints the primary and related results.
   *
   * @param printStream The stream to print to.
   */
  public void print(PrintStream printStream) {
    printResults(primaryResults, printStream, "Matching documents:", "No matching documents.");
    printResults(relatedResults, printStream, "Related documents:", "No related documents.");
  }

  private static void printResults(Set<JsonNode> results, PrintStream printStream, String message, String zeroMessage) {
    if (results.size() > 0) {
      printStream.println(message);
      results.stream()
          .map(JsonNode::toPrettyString)
          .forEach(printStream::println);
    } else {
      printStream.println(zeroMessage);
    }
  }
}
