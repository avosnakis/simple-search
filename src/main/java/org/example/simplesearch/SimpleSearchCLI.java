package org.example.simplesearch;

import org.example.simplesearch.index.IndexFactory;
import org.example.simplesearch.index.InvalidDocumentFileException;
import org.example.simplesearch.index.SearchIndex;
import org.example.simplesearch.search.InvalidSearchSyntax;
import org.example.simplesearch.search.SearchParser;
import org.example.simplesearch.search.SearchRequest;
import org.example.simplesearch.search.SearchResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Command line interface for the search engine.
 */
public class SimpleSearchCLI {

  private final PrintStream printStream;
  private final InputStream inputStream;

  SimpleSearchCLI(PrintStream printStream, InputStream inputStream) {
    this.printStream = printStream;
    this.inputStream = inputStream;
  }

  public static void main(String... args) {
    SimpleSearchCLI simpleSearchCLI = new SimpleSearchCLI(System.out, System.in);

    try {
      simpleSearchCLI.execute(args);
    } catch (InvalidArgumentException e) {
      System.exit(1);
    } catch (IOException e) {
      System.out.println("Encountered error: " + e.getMessage());
      System.exit(1);
    } catch (InvalidConfigurationException e) {
      System.out.println("Malformed configuration file.");
      System.exit(1);
    } catch (InvalidDocumentFileException e) {
      System.out.println("Invalid document file submitted.");
      System.exit(1);
    }
  }

  void execute(String... args)
      throws InvalidArgumentException, IOException, InvalidConfigurationException, InvalidDocumentFileException {
    if (args.length == 0) {
      usage();
      throw new InvalidArgumentException("");
    }

    KeyMappings mappings = fromConfigFile(args[args.length - 1]);
    SearchClient searchClient = createSearchClient(args, mappings);

    Scanner sc = new Scanner(inputStream);
    printStream.print(">>> ");
    while (sc.hasNext()) {
      String input = sc.next();
      if (input.equals("exit")) {
        break;
      }

      printStream.println();
      SearchParser parser = new SearchParser(input);
      try {
        SearchRequest request = parser.parse();
        SearchResult result = searchClient.search(request, mappings.getIdField());
        result.print(printStream);
      } catch (InvalidSearchSyntax e) {
        printStream.println(e.getMessage());
      }

      printStream.print(">>> ");
    }

    printStream.println("Exiting simple search engine."); // This somehow never get printed.
  }

  private KeyMappings fromConfigFile(String configFilePath) throws IOException, InvalidConfigurationException {
    File configFile = new File(configFilePath);
    if (!configFile.exists()) {
      throw new FileNotFoundException("Missing config file " + configFilePath);
    }

    return KeyMappings.fromJsonConfig(configFile);
  }

  private SearchClient createSearchClient(String[] files, KeyMappings keyMappings) throws IOException, InvalidDocumentFileException {
    Map<String, SearchIndex> indices = new HashMap<>();
    for (int i = 0; i < files.length - 1; i++) {
      String filePath = files[i];
      File file = new File(filePath);

      if (!file.exists()) {
        throw new FileNotFoundException("Missing file " + filePath);
      }

      SearchIndex index = IndexFactory.createSearchIndex(file, keyMappings.getIdField());
      indices.put(index.getName(), index);
    }

    return new SearchClient(indices, keyMappings);
  }

  private void usage() {
    printStream.println("Please enter all input files as following:");
    printStream.println("file1.json file2.json config.json");
    printStream.println("There can be as many input files as you want, but the last file must be your configuration file.");
  }
}
