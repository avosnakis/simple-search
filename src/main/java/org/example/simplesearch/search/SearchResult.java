package org.example.simplesearch.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.PrintStream;
import java.util.Iterator;
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
    printResults(primaryResults, printStream, "matching");
    printResults(relatedResults, printStream, "related");
  }

  private static void printResults(Set<JsonNode> results, PrintStream printStream, String desc) {
    if (results.size() > 0) {
      String message = desc + " document" + (results.size() == 1 ? "" : "s") + ".";
      printStream.printf("%d %s%n", results.size(), message);
      results.forEach(node -> {
        printDoc(node, printStream);
        printStream.println();
      });
    } else {
      printStream.printf("No %s documents.%n", desc);
    }
  }

  private static void printDoc(JsonNode doc, PrintStream printStream) {
    Iterator<String> fields = doc.fieldNames();
    while (fields.hasNext()) {
      String curr = fields.next();
      printStream.printf("%-30.30s  %-40.40s%n", curr, doc.path(curr).asText());
    }
  }
}
