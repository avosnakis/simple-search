package org.example.simplesearch.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchIndexTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void givenStoredDocument_whenQueried_thenRetrieved() {
    ObjectNode node = mapper.createObjectNode().put("_id", 1)
        .put("TEST_FIELD", "test");
    DocumentStore store = new DocumentStore();
    store.storeDocument("1", node);

    DocumentInvertedIndex invertedIndex = new DocumentInvertedIndex("TEST");
    invertedIndex.addDoc("1", node);

    SearchIndex searchIndex = new SearchIndex("test", store, invertedIndex);

    Set<JsonNode> searchResult = searchIndex.findMatchingDocs("TEST_FIELD", "test");
    assertEquals(Set.of(node), searchResult);

    assertEquals(Set.of("_id", "TEST_FIELD"), searchIndex.searchableFields());
  }

  @Test
  void givenMultipleStoredDocs_whenQueried_allRetrieved() {
    ObjectNode firstNode = mapper.createObjectNode().put("_id", 1)
        .put("TEST_FIELD", "test");
    ObjectNode secondNode = mapper.createObjectNode().put("_id", 2)
        .put("TEST_FIELD", "test");

    DocumentStore store = new DocumentStore();
    store.storeDocument("1", firstNode);
    store.storeDocument("2", secondNode);

    DocumentInvertedIndex invertedIndex = new DocumentInvertedIndex("TEST");
    invertedIndex.addDoc("1", firstNode);
    invertedIndex.addDoc("2", secondNode);

    SearchIndex searchIndex = new SearchIndex("test", store, invertedIndex);

    Set<JsonNode> searchResult = searchIndex.findMatchingDocs("TEST_FIELD", "test");
    assertEquals(Set.of(firstNode, secondNode), searchResult);
    assertEquals(Set.of("_id", "TEST_FIELD"), searchIndex.searchableFields());
  }

  @Test
  void givenMultipleStoredDocs_whenPartiallyQueried_partRetrieved() {

    ObjectNode firstNode = mapper.createObjectNode().put("_id", 1)
        .put("TEST_FIELD", "test");
    ObjectNode secondNode = mapper.createObjectNode().put("_id", 2)
        .put("TEST_FIELD", "not_test");

    DocumentStore store = new DocumentStore();
    store.storeDocument("1", firstNode);
    store.storeDocument("2", secondNode);

    DocumentInvertedIndex invertedIndex = new DocumentInvertedIndex("TEST");
    invertedIndex.addDoc("1", firstNode);
    invertedIndex.addDoc("2", secondNode);

    SearchIndex searchIndex = new SearchIndex("test", store, invertedIndex);

    Set<JsonNode> searchResult = searchIndex.findMatchingDocs("TEST_FIELD", "test");
    assertEquals(Set.of(firstNode), searchResult);
    assertEquals(Set.of("_id", "TEST_FIELD"), searchIndex.searchableFields());
  }
}