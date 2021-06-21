package org.example.simplesearch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleSearchCLITest {

  private static final String JSON_DIR = "src/test/resources/integration";

  private OutputStream outputStream;
  private PrintStream printStream;

  @BeforeEach
  void setUp() {
    this.outputStream = new ByteArrayOutputStream();
    this.printStream = new PrintStream(outputStream);
  }

  @Test
  void givenSubmittedDocsThenConfig_loadsSuccessfully() throws Exception {
    InputStream inputStream = new ByteArrayInputStream(
        "1\norganizations\n_id\n102".getBytes()
    );

    SimpleSearchCLI simpleSearchCLI = new SimpleSearchCLI(printStream, inputStream);
    simpleSearchCLI.execute(integrationFile("organizations.json"), integrationFile("CONFIG.json"));

    assertTrue(containsStringChunks(outputStream.toString(), "\"_id\" : 102", "No related documents."));
  }

  @Test
  void givenSubmittedAllDocsThenConfig_loadsSuccessfully() throws Exception {
    InputStream inputStream = new ByteArrayInputStream(
        "1\norganizations\n_id\n102".getBytes()
    );

    SimpleSearchCLI simpleSearchCLI = new SimpleSearchCLI(printStream, inputStream);
    simpleSearchCLI.execute(integrationFile("organizations.json"),
        integrationFile("tickets.json"),
        integrationFile("users.json"),
        integrationFile("CONFIG.json"));

    String[] chunks = {
        "\"_id\" : 102",
        "\"_id\" : \"25cb699f-a5dd-45d8-9bc1-9c4b7d096946\"",
        "\"_id\" : \"20615fe1-765b-4ff5-b4f6-ea42dcc8cac3\"",
        "\"_id\" : \"3ff0599a-fe0f-4f8f-ac31-e2636843bcea\"",
        "\"_id\" : \"6fed7d01-15dd-4b59-94f9-1093b4bc0995\"",
        "\"_id\" : \"df1a642a-e704-4556-af79-98a63b59401d\"",
        "\"_id\" : \"bb8b1829-25d9-4534-83a2-c4e6086d76d4\"",
        "\"_id\" : \"ea69e0c0-d1b8-462e-a654-b571666e6253\"",
        "\"_id\" : \"a12a5f33-d4a0-4e43-8773-4b22e16fc0c8\"",
        "\"_id\" : 25",
        "\"_id\" : 33",
        "\"_id\" : 69"
    };
    assertTrue(containsStringChunks(outputStream.toString(), chunks));
  }

  @Test
  void givenSubmittedDocs_thenRequestFieldsAndExits_displaysFields() throws Exception {
    InputStream inputStream = new ByteArrayInputStream(
        "2\n3".getBytes()
    );

    SimpleSearchCLI simpleSearchCLI = new SimpleSearchCLI(printStream, inputStream);
    simpleSearchCLI.execute(
        "src/test/resources/factory/single_good.json",
        "src/test/resources/factory/TEST_CONFIG.json"
    );

    assertTrue(containsStringChunks(outputStream.toString(), "_id", "test"));
  }

  private static String integrationFile(String file) {
    return JSON_DIR + File.separator + file;
  }

  /**
   * Checks whether a string contains all of the input chunks. Used instead of an equality as tbe full JSON files used
   * in the integration tests are sizeable, and checking that the unique IDs of each document are present is more robust.
   *
   * @param output The string.
   * @param chunks The chunks to check.
   * @return Whether they are all contained.
   */
  private static boolean containsStringChunks(String output, String... chunks) {
    for (String chunk : chunks) {
      if (!output.contains(chunk)) {
        System.out.println("Missing chunk: " + chunk);
        return false;
      }
    }

    return true;
  }
}