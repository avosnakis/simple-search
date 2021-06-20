package org.example.simplesearch.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IndexFactoryTest {

  private final ObjectMapper mapper = new ObjectMapper();
  private static final String JSON_DIR = "src/test/resources/factory";

  @Test
  void givenSingleDocInJson_whenCreatingIndex_succeeds() throws InvalidDocumentFileException {
    File file = new File(JSON_DIR, "single_good.json");

    SearchIndex index = IndexFactory.createSearchIndex(file, "_id");
    Set<JsonNode> result = index.findMatchingDocs("test", "TEST");

    assertEquals(
        singleton(mapper.createObjectNode().put("_id", 1).put("test", "TEST")), result);

    Set<JsonNode> secondResult = index.findMatchingDocs("_id", "1");

    assertEquals(
        singleton(mapper.createObjectNode().put("_id", 1).put("test", "TEST")), secondResult);
  }

  @Test
  void givenMultipleDocsInJson_whenCreatingIndex_succeeds() throws InvalidDocumentFileException {
    File file = new File(JSON_DIR, "multiple_good.json");

    SearchIndex index = IndexFactory.createSearchIndex(file, "_id");
    Set<JsonNode> res1 = index.findMatchingDocs("test", "TEST");

    assertEquals(
        Set.of(mapper.createObjectNode().put("_id", 1).put("test", "TEST")), res1);

    Set<JsonNode> res2 = index.findMatchingDocs("another_field", "FIELD_VAL_1");
    assertEquals(Set.of(
        mapper.createObjectNode().put("_id", 2)
            .set("another_field", mapper.createArrayNode()
                .add("FIELD_VAL_1")
                .add("FIELD_VAL_2"))
    ), res2);
  }

  @Test
  void givenFileIsNotArray_whenCreatingIndex_fails() {
    File file = new File(JSON_DIR, "not_array.json");
    assertThrows(InvalidDocumentFileException.class, () -> IndexFactory.createSearchIndex(file, "_id"));
  }

  @Test
  void givenFileHasEmptyArray_whenCreatingIndex_succeeds() throws InvalidDocumentFileException {
    File file = new File(JSON_DIR, "empty.json");

    SearchIndex index = IndexFactory.createSearchIndex(file, "_id");
    Set<JsonNode> res = index.findMatchingDocs("test", "TEST");

    assertEquals((emptySet()), res);
  }

  @Test
  void givenFileHasDocWithNestedObject_whenCreatingIndex_succeeds() throws InvalidDocumentFileException {
    File file = new File(JSON_DIR, "nested_object.json");

    SearchIndex index = IndexFactory.createSearchIndex(file, "_id");
    Set<JsonNode> res = index.findMatchingDocs("test", "TEST");

    assertEquals(singleton(
        mapper.createObjectNode()
            .put("_id", 1)
            .put("test", "TEST")
            .set("nested_field", mapper.createObjectNode()
                .put("test", "TEST")
            )
    ), res);
  }

  @Test
  void givenFileIsNotValidJson_whenCreatingIndex_fails() {
    File file = new File(JSON_DIR, "not_json");
    assertThrows(InvalidDocumentFileException.class, () -> IndexFactory.createSearchIndex(file, "_id"));
  }

  @Test
  void givenDocumentMissingSpecifiedIndexField_whenCreatingIndex_succeedsButDoesNotStoreDoc() throws InvalidDocumentFileException {
    File file = new File(JSON_DIR, "single_missing_id.json");
    SearchIndex index = IndexFactory.createSearchIndex(file, "_id");
    Set<JsonNode> res = index.findMatchingDocs("test", "TEST");

    assertEquals(emptySet(), res);
  }
}