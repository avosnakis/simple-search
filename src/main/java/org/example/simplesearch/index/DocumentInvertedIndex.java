package org.example.simplesearch.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Inverted index which maintains a set of inverted indices for each field on a set of documents.
 */
public class DocumentInvertedIndex {

  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentInvertedIndex.class);

  private final String name;
  private final Map<String, InvertedIndex> indices = new HashMap<>();

  DocumentInvertedIndex(String name) {
    this.name = name;
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
    if (document.isObject()) {
      ObjectNode objectNode = (ObjectNode) document;

      Iterator<String> fieldNames = objectNode.fieldNames();
      while (fieldNames.hasNext()) {
        String field = fieldNames.next();
        indexField(id, field, objectNode.get(field));
      }
    }
  }

  private void indexField(int id, String fieldName, JsonNode field) {
    if (field.isArray()) {
      ArrayNode arrayNode = (ArrayNode) field;
      for (JsonNode value : arrayNode) {
        insertValue(id, fieldName, value);
      }
    } else {
      insertValue(id, fieldName, field);
    }
  }

  /**
   * Attempts to inset a value -> ID mapping for a specified field index.
   * It will not be stored and simply log a warning if the value is not
   *
   * @param id        The ID of the document.
   * @param fieldName The name of the field.
   * @param field     The field value itself.
   */
  private void insertValue(int id, String fieldName, JsonNode field) {
    if (field.isValueNode()) {
      ValueNode value = (ValueNode) field;
      indices.putIfAbsent(fieldName, new InvertedIndex());
      indices.get(fieldName).addDoc(id, value.textValue());
    } else {
      LOGGER.warn("INDEX {}: Failed to index field {} for document {}", this.name, fieldName, id);
    }
  }

  /**
   * Retrieves all document IDs matching a value on a field. Will return an empty result if the field cannot be found.
   *
   * @param field The field to search.
   * @param value The value to search.
   * @return The matching IDs.
   */
  Set<Integer> retrieveHits(String field, String value) {
    InvertedIndex index = indices.get(field);
    if (index == null) {
      return Collections.emptySet();
    }

    return index.retrieveHits(value);
  }
}
