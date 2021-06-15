package org.example.simplesearch.index;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Inverted index which maintains a set of inverted indices for each field on a set of documents.
 */
public class DocumentInvertedIndex {

  private final String field;
  private final Map<String, InvertedIndex> indices = new HashMap<>();

  DocumentInvertedIndex(String name) {
    this.field = name;
  }

  /**
   * Adds a document to the store.
   * For each field on the document, either a new index will be created (if an index for that field does not exist)
   * or an already extant index will be updated.
   *
   * @param id       The ID of the document.
   * @param document The document.
   */
  void addDoc(int id, JsonNode document) {
  }

  /**
   * Retrieves all document IDs matching a value on a field. Will return an empty result if the field cannot be found.
   *
   * @param field The field to search.
   * @param value The value to search.
   * @return The matching IDs.
   */
  Set<Integer> retrieveHits(String field, String value) {
    return Collections.emptySet();
  }
}
