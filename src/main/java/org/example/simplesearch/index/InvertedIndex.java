package org.example.simplesearch.index;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Inverted index, containing word -> document ID mappings.
 */
class InvertedIndex {

  private final Map<String, Set<Integer>> index = new HashMap<>();

  /**
   * Adds a doc to the index, storing its ID against its field values.
   *
   * @param id    The ID of the document.
   * @param value The document.
   */
  void addDoc(int id, String value) {
    index.putIfAbsent(value, new HashSet<>());
    index.get(value).add(id);
  }

  /**
   * Retrieves all document IDs matching a value.
   *
   * @param val The value to search.
   * @return The matching IDs.
   */
  Set<Integer> retrieveHits(String val) {
    return index.getOrDefault(val, Collections.emptySet());
  }
}
