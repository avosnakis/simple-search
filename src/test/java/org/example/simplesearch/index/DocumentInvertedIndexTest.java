package org.example.simplesearch.index;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DocumentInvertedIndexTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void givenDocHasTestAndExample_whenRetrieved_thenReturns() {
    ObjectNode node = mapper.createObjectNode();
    node.put("test", "EXAMPLE");

    DocumentInvertedIndex index = new DocumentInvertedIndex("test_docs");
    index.addDoc("1", node);

    Set<String> res = index.retrieveHits("test", "EXAMPLE");
    assertEquals(singleton("1"), res);
    assertEquals(Set.of("test"), index.searchableFields());
  }

  @Test
  void givenMultipleDocsHaveTestAndExample_whenRetrieved_thenReturns() {
    DocumentInvertedIndex index = new DocumentInvertedIndex("test_docs");

    ObjectNode firstNode = mapper.createObjectNode();
    firstNode.put("test", "EXAMPLE");

    ObjectNode secondNode = mapper.createObjectNode();
    secondNode.put("test", "EXAMPLE");

    index.addDoc("1", firstNode);
    index.addDoc("2", secondNode);

    Set<String> res = index.retrieveHits("test", "EXAMPLE");
    assertEquals(Set.of("1", "2"), res);
    assertEquals(Set.of("test"), index.searchableFields());
  }

  @Test
  void givenNotMatchingValueForField_whenRetrieved_thenDoesNotReturn() {
    ObjectNode node = mapper.createObjectNode();
    node.put("test", "EXAMPLE");

    DocumentInvertedIndex index = new DocumentInvertedIndex("test_docs");
    index.addDoc("1", node);

    Set<String> res = index.retrieveHits("test", "NOT_THE_VALUE");
    assertEquals(emptySet(), res);
    assertEquals(Set.of("test"), index.searchableFields());
  }

  @Test
  void givenNonexistentField_whenRetrieved_thenDoesNotReturn() {
    ObjectNode node = mapper.createObjectNode();
    node.put("test", "EXAMPLE");

    DocumentInvertedIndex index = new DocumentInvertedIndex("test_docs");
    index.addDoc("1", node);

    Set<String> res = index.retrieveHits("not_a_real_field", "NOT_THE_VALUE");
    assertEquals(emptySet(), res);
    assertEquals(Set.of("test"), index.searchableFields());
  }

  @Test
  void givenFieldIsArray_whenRetrieved_thenCanBeRetrievedFromMultipleQueries() {
    ObjectNode node = mapper.createObjectNode();
    node.set("test", mapper.createArrayNode()
        .add("EXAMPLE_1")
        .add("EXAMPLE_2")
    );

    DocumentInvertedIndex index = new DocumentInvertedIndex("test_docs");
    index.addDoc("1", node);

    Set<String> res = index.retrieveHits("test", "EXAMPLE_1");
    assertEquals(singleton("1"), res);

    Set<String> res2 = index.retrieveHits("test", "EXAMPLE_2");
    assertEquals(singleton("1"), res2);
    assertEquals(Set.of("test"), index.searchableFields());
  }

  @Test
  void givenNestedObject_whenAttemptedToIndex_notIndexed() {
    ObjectNode node = mapper.createObjectNode();
    node.set("test", mapper.createObjectNode()
        .put("nested_field", "nested_value")
    );

    DocumentInvertedIndex index = new DocumentInvertedIndex("test_docs");
    index.addDoc("1", node);

    // This is somewhat awkward to 'query', but an ObjectNode's string representation will look like this.
    Set<String> res = index.retrieveHits("test", "{\"nested_field\":\"nested_value\"}");
    assertEquals(emptySet(), res);
    assertEquals(emptySet(), index.searchableFields());
  }
}