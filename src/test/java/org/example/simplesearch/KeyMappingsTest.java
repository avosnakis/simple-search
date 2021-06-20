package org.example.simplesearch;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class KeyMappingsTest {

  private static final String TEST_CONF_DIR = "src/test/resources/keys";

  @Test
  void givenValidJson_andCorrectConfig_whenBuilt_thenSucceeds() throws Exception {
    KeyMappings keyMappings = KeyMappings.fromJsonConfig(new File(TEST_CONF_DIR, "good_keys.json"));

    assertEquals("_id", keyMappings.getIdField());
    assertEquals(Collections.singleton("organization_id"), keyMappings.retrieveKeysFor("organizations"));
  }

  @Test
  void givenValidJson_andCorrectConfigWithMultipleKeys_whenBuilt_thenSucceeds() throws Exception {
    KeyMappings keyMappings = KeyMappings.fromJsonConfig(new File(TEST_CONF_DIR, "good_keys_with_array.json"));

    assertEquals("_id", keyMappings.getIdField());
    assertEquals(Set.of("assignee_id", "submitter_id"), keyMappings.retrieveKeysFor("users"));
  }

  @Test
  void givenInvalidJson_whenBuilt_throwsException() {
    assertThrows(IOException.class, () -> KeyMappings.fromJsonConfig(new File(TEST_CONF_DIR, "notjson")));
  }

  @Test
  void givenKeysObjectHasNestedObject_whenBuilt_throwsException() {
    assertThrows(InvalidConfigurationException.class,
        () -> KeyMappings.fromJsonConfig(new File(TEST_CONF_DIR, "keys_has_object.json")));
  }

  @Test
  void givenKeysObjectArrayHasNestedObject_whenBuilt_throwsException() {
    assertThrows(InvalidConfigurationException.class,
        () -> KeyMappings.fromJsonConfig(new File(TEST_CONF_DIR, "keys_with_array_has_object.json")));
  }

  @Test
  void givenMissingForeignKeysField_whenBuilt_throwsException() {
    assertThrows(InvalidConfigurationException.class,
        () -> KeyMappings.fromJsonConfig(new File(TEST_CONF_DIR, "missing_foreign_keys_field.json")));
  }

  @Test
  void givenMissingIdField_whenBuilt_throwsException() {
    assertThrows(InvalidConfigurationException.class,
        () -> KeyMappings.fromJsonConfig(new File(TEST_CONF_DIR, "missing_id_field.json")));
  }
}