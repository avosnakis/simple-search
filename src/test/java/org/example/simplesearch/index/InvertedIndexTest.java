package org.example.simplesearch.index;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InvertedIndexTest {

  @Test
  void whenAddingDocWithStringProp_canBeRetrieved() {
    InvertedIndex index = new InvertedIndex();
    index.addDoc("1", "EXAMPLE");

    Set<String> res = index.retrieveHits("EXAMPLE");
    assertEquals(singleton("1"), res);
  }

  @Test
  void givenMultipleMatchingDocs_whenAddingDocWithStringProp_canBeRetrieved() {
    InvertedIndex index = new InvertedIndex();
    index.addDoc("1", "EXAMPLE");
    index.addDoc("2", "EXAMPLE");

    Set<String> res = index.retrieveHits("EXAMPLE");
    assertEquals(Set.of("1", "2"), res);
  }

  @Test
  void whenSearchingWithNonMatchingProp_cannotBeRetrieved() {
    InvertedIndex index = new InvertedIndex();
    index.addDoc("1", "EXAMPLE");

    Set<String> res = index.retrieveHits("NOT_MATCHING");
    assertEquals(emptySet(), res);
  }
}