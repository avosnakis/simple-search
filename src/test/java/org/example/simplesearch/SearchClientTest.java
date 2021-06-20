package org.example.simplesearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simplesearch.index.IndexFactory;
import org.example.simplesearch.index.InvalidDocumentFileException;
import org.example.simplesearch.index.SearchIndex;
import org.example.simplesearch.search.SearchRequest;
import org.example.simplesearch.search.SearchResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.*;

class SearchClientTest {

  private final ObjectMapper mapper = new ObjectMapper();

  private static final String JSON_DIR = "src/test/resources/factory";

  @Test
  void givenIndexExists_whenSearching_returnsResults() throws InvalidDocumentFileException {
    Map<String, SearchIndex> indices = new HashMap<>();
    indices.put("organisation", IndexFactory.createSearchIndex(new File(JSON_DIR, "single_good.json"), "_id"));

    SearchClient client = new SearchClient(indices, new KeyMappings("_id", new HashMap<>()));

    SearchResult result = client.search(new SearchRequest("organisation", "_id", "1"));
    assertEquals(new SearchResult(
        singleton(mapper.createObjectNode().put("_id", 1).put("test", "TEST"))
    ), result);
  }

  @Test
  void givenIndexDoesNotExist_whenSearching_returnsNothing() throws InvalidDocumentFileException {
    Map<String, SearchIndex> indices = new HashMap<>();
    indices.put("organisation", IndexFactory.createSearchIndex(new File(JSON_DIR, "empty.json"), "_id"));

    SearchClient client = new SearchClient(indices, new KeyMappings("_id", new HashMap<>()));

    SearchResult result = client.search(new SearchRequest("users", "_id", "1"));
    assertEquals(new SearchResult(emptySet()), result);
  }

}