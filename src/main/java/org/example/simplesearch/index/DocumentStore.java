package org.example.simplesearch.index;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Simple document store, maintaing a mapping of document IDs to documents.
 */
public class DocumentStore {

  private final Map<String, JsonNode> store = new HashMap<>();

  DocumentStore() {
  }

  /**
   * Attempts to retrieve a document based on its ID.
   *
   * @param id The ID of the document to retrieve.
   * @return The document if the ID exists, otherwise nothing.
   */
  Optional<JsonNode> retrieveDocument(String id) {
    return Optional.ofNullable(store.get(id));
  }

  /**
   * Stores a document against the given ID. Will override any existing mapping.
   *
   * @param id The ID of the document to store.
   * @param document The document to store.
   */
  void storeDocument(String id, JsonNode document) {
    store.put(id, document);
  }
}
