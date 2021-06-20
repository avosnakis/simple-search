package org.example.simplesearch.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simplesearch.index.DocumentStore;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DocumentStoreTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void whenStoringDocument_thenCanBeRetrieved() {
    JsonNode jsonNode = mapper.createObjectNode()
        .put("_id", 1)
        .put("test", "test");

    DocumentStore documentStore = new DocumentStore("TEST");
    documentStore.storeDocument("1", jsonNode);

    assertEquals(Optional.of(jsonNode), documentStore.retrieveDocument("1"));
    assertEquals(Optional.empty(), documentStore.retrieveDocument("2"));
  }

  @Test
  void givenIdExists_whenStoringDocument_thenOverwritten() {
    JsonNode originalDoc = mapper.createObjectNode()
        .put("_id", 1)
        .put("test", "test");

    DocumentStore documentStore = new DocumentStore("TEST");
    documentStore.storeDocument("1", originalDoc);

    JsonNode newDoc = mapper.createObjectNode()
        .put("_id", 1)
        .put("test", "new_est");

    documentStore.storeDocument("1", newDoc);

    assertEquals(Optional.of(newDoc), documentStore.retrieveDocument("1"));
  }
}