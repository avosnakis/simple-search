package org.example.simplesearch.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NumericNode;

import java.io.File;
import java.io.IOException;

/**
 * Factory for creating search indices.
 */
public class IndexFactory {

  /**
   * Creates a search index from an initial file.
   *
   * @param file The file to read documents from.
   * @return The search index.
   */
  public static SearchIndex createSearchIndex(File file, String idFieldName) throws InvalidDocumentFileException {
    JsonNode fileData;
    try {
      fileData = new ObjectMapper().readTree(file);
    } catch (IOException e) {
      throw new InvalidDocumentFileException(e);
    }

    if (!fileData.isArray()) {
      throw new InvalidDocumentFileException("Expected input file to be a valid JSON array.");
    }

    ArrayNode documentArray = (ArrayNode) fileData;

    String name = file.getName();
    DocumentStore store = new DocumentStore(name);
    DocumentInvertedIndex invertedIndex = new DocumentInvertedIndex(name);
    for (JsonNode document : documentArray) {
      JsonNode idField = document.path(idFieldName);

      // As long as there is a valid ID field, we will try to index it.
      if (idField.isNumber()) {
        NumericNode idNum = (NumericNode) idField;
        int id = idNum.asInt();

        store.storeDocument(id, document);
        invertedIndex.addDoc(id, document);
      }
    }

    return new SearchIndex(store, invertedIndex);
  }
}
