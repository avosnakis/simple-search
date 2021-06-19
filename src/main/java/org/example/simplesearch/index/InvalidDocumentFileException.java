package org.example.simplesearch.index;

public class InvalidDocumentFileException extends Exception {
  public InvalidDocumentFileException(String message) {
    super(message);
  }

  public InvalidDocumentFileException(Throwable cause) {
    super(cause);
  }
}
