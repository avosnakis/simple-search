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
import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchClientTest {

  private static final ObjectMapper mapper = new ObjectMapper();

  private static final String JSON_DIR = "src/test/resources/factory";

  @Test
  void givenIndexExists_whenSearching_returnsResults() throws InvalidDocumentFileException {
    Map<String, SearchIndex> indices = new HashMap<>();
    indices.put("organisation", IndexFactory.createSearchIndex(new File(JSON_DIR, "single_good.json"), "_id"));

    SearchClient client = new SearchClient(indices, new KeyMappings("_id", new HashMap<>()));

    SearchResult result = client.search(new SearchRequest("organisation", "_id", "1"), "_id");
    assertEquals(new SearchResult(
        singleton(mapper.createObjectNode().put("_id", 1).put("test", "TEST")),
        emptySet()
    ), result);
  }

  @Test
  void givenIndexDoesNotExist_whenSearching_returnsNothing() throws InvalidDocumentFileException {
    Map<String, SearchIndex> indices = new HashMap<>();
    indices.put("organisation", IndexFactory.createSearchIndex(new File(JSON_DIR, "empty.json"), "_id"));

    SearchClient client = new SearchClient(indices, new KeyMappings("_id", new HashMap<>()));

    SearchResult result = client.search(new SearchRequest("users", "_id", "1"), "_id");
    assertEquals(new SearchResult(emptySet(), emptySet()), result);
  }

  @Test
  void givenRelatedDocExists_whenSearching_givesNode() throws Exception {
    Map<String, SearchIndex> indices = new HashMap<>();
    indices.put("single_good", IndexFactory.createSearchIndex(new File(JSON_DIR, "single_good.json"), "_id"));
    indices.put("test_fk", IndexFactory.createSearchIndex(new File(JSON_DIR, "test_fk.json"), "_id"));

    KeyMappings keyMappings = KeyMappings.fromJsonConfig(new File(JSON_DIR, "TEST_CONFIG.json"));

    SearchClient client = new SearchClient(indices, keyMappings);

    SearchResult result = client.search(new SearchRequest("single_good","_id", "1"), "_id");
    assertEquals(
        new SearchResult(
            singleton(mapper.createObjectNode().put("_id", 1).put("test", "TEST")),
            singleton(mapper.createObjectNode().put("_id", 1).put("single_good_id", 1))
        ), result);
  }
}