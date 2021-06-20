package org.example.simplesearch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Stores the configured ID field as well as configured foreign keys.
 */
public class KeyMappings {

  private static final String CONFIG_ID_FIELD = "idField";
  private static final String CONFIG_FK_FIELD = "foreignKeys";

  private static final ObjectMapper mapper = new ObjectMapper();

  private final String idField;
  private final Map<String, Set<String>> mappings;

  KeyMappings(String idField, Map<String, Set<String>> mappings) {
    this.idField = idField;
    this.mappings = mappings;
  }

  public String getIdField() {
    return idField;
  }

  public Set<String> retrieveKeysFor(String indexName) {
    return mappings.getOrDefault(indexName, Collections.emptySet());
  }

  /**
   * Builds a KeyMappings object from a configuration JSON file.
   *
   * @param file The file.
   * @return The key mappings, including the configured ID field and the configured foreign key associations.
   * @throws IOException                   In case the file cannot be read or is malformed.
   * @throws InvalidConfigurationException In case the file is of the incorrect format.
   */
  static KeyMappings fromJsonConfig(File file) throws IOException, InvalidConfigurationException {
    JsonNode jsonNode = mapper.readTree(file);
    if (!jsonNode.isObject()) {
      throw new InvalidConfigurationException("Configuration must be a valid JSON, with the field: idField, foreignKeys");
    }

    ObjectNode configObject = (ObjectNode) jsonNode;
    JsonNode idNode = configObject.path(CONFIG_ID_FIELD);
    if (!idNode.isValueNode()) {
      throw new InvalidConfigurationException("id field must be a value.");
    }

    ValueNode idValue = (ValueNode) idNode;
    String idField = idValue.asText();
    Map<String, Set<String>> foreignKeyMappings = buildForeignKeyMappings(configObject);
    return new KeyMappings(idField, foreignKeyMappings);
  }


  private static Map<String, Set<String>> buildForeignKeyMappings(ObjectNode configObject) throws InvalidConfigurationException {
    JsonNode foreignKeys = configObject.path(CONFIG_FK_FIELD);
    if (!foreignKeys.isObject()) {
      throw new InvalidConfigurationException("Foreign keys must be stored in an object.");
    }

    Map<String, Set<String>> mappings = new HashMap<>();
    ObjectNode foreignKeyObj = (ObjectNode) foreignKeys;

    Iterator<String> keys = foreignKeyObj.fieldNames();
    while (keys.hasNext()) {
      String indexName = keys.next();
      JsonNode foreignKeyNode = foreignKeyObj.path(indexName);
      addMappings(mappings, indexName, foreignKeyNode);
    }

    return mappings;
  }

  /**
   * Modify the mappings object to associate one or more foreign keys with an index name.
   *
   * @param mappings       The mappings object.
   * @param indexName      The index to add foreign keys for.
   * @param foreignKeyNode The foreign key configuration for this index.
   * @throws InvalidConfigurationException In case the configuration is malformed.
   */
  private static void addMappings(Map<String, Set<String>> mappings, String indexName, JsonNode foreignKeyNode) throws InvalidConfigurationException {
    if (foreignKeyNode.isValueNode()) {
      // If it's just a value, then there is only one foreign key.
      ValueNode foreignKeyVal = (ValueNode) foreignKeyNode;
      mappings.put(indexName, Set.of(foreignKeyVal.asText()));
    } else if (foreignKeyNode.isArray()) {
      // If there is an array, then this index has multiple foreign keys.
      ArrayNode foreignKeyArray = (ArrayNode) foreignKeyNode;
      Set<String> keys = new HashSet<>();
      for (JsonNode foreignKeyArrayVal : foreignKeyArray) {
        // It must be an array of strings.
        if (!foreignKeyArrayVal.isValueNode()) {
          throw new InvalidConfigurationException("Foreign keys must be either a string or an array of strings.");
        }

        ValueNode foreignKeyVal = (ValueNode) foreignKeyArrayVal;
        keys.add(foreignKeyVal.asText());
      }
      mappings.put(indexName, keys);
    } else {
      throw new InvalidConfigurationException("Foreign keys must be either a string or an array of strings.");
    }
  }
}
