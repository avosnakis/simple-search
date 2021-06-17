package org.example.simplesearch.index;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Simple document store, maintaing a mapping of document IDs to documents.
 */
public class DocumentStore {

  private final String name;

  private final Map<Integer, JsonNode> store = new HashMap<>();

  DocumentStore(String name) {
    this.name = name;
  }

  /**
   * Attempts to retrieve a document based on its ID.
   *
   * @param id The ID of the document to retrieve.
   * @return The document if the ID exists, otherwise nothing.
   */
  Optional<JsonNode> retrieveDocument(int id) {
    return Optional.ofNullable(store.get(id));
  }

  /**
   * Stores a document against the given ID. Will override any existing mapping.
   *
   * @param id The ID of the document to store.
   * @param document The document to store.
   */
  void storeDocument(int id, JsonNode document) {
    store.put(id, document);
  }
}
