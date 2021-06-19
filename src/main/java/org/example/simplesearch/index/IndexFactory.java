package org.example.simplesearch.index;

import java.io.File;

public class IndexFactory {

  /**
   * Creates a search index from an initial file.
   *
   * @param file The file to read documents from.
   * @return The search index.
   */
  static SearchIndex createSearchIndex(File file) throws InvalidDocumentFileException {
    return new SearchIndex(null, null);
  }
}
