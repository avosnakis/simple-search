package org.example.simplesearch;

import org.example.simplesearch.index.IndexFactory;
import org.example.simplesearch.index.InvalidDocumentFileException;
import org.example.simplesearch.index.SearchIndex;
import org.example.simplesearch.search.SearchRequest;
import org.example.simplesearch.search.SearchResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

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

    printStream.println("Welcome to the simple search engine.");
    printStream.println("Type 1 to search, 2 to list fields that can be searched, or 3 to exit.");
    printStream.println();
    Scanner scanner = new Scanner(inputStream);
    while (scanner.hasNext()) {
      int res = scanner.nextInt();
      if (res == 1) {
        performSearch(scanner, searchClient, mappings);
      } else if (res == 2) {
        listFields(searchClient);
      } else if (res == 3) {
        printStream.println("Exiting simple search engine.");
        return;
      } else {
        printStream.println("Unknown command.");
      }
    }
  }

  private void listFields(SearchClient searchClient) {
    Map<String, Set<String>> fields = searchClient.searchableFields();
    for (Map.Entry<String, Set<String>> entry: fields.entrySet()) {
      printStream.printf("%s:%n", entry.getKey());
      for (String field : entry.getValue()) {
        printStream.printf("\t%s%n", field);
      }
    }
  }

  private void performSearch(Scanner scanner, SearchClient searchClient, KeyMappings mappings) {
    SearchRequest request = enterRequest(scanner, Collections.emptyList());
    SearchResult result = searchClient.search(request, mappings.getIdField());
    result.print(printStream);
  }

  private SearchRequest enterRequest(Scanner scanner, List<String> files) {
    printStream.println("Which file would you like to query?");
    String file = scanner.next();

    printStream.println("Which field would you like to query?");
    String field = scanner.next();

    printStream.println("What value would you like to search for?");
    String value = scanner.next();

    printStream.printf("Searching %s, where %s is %s%n", field, field, value);
    return new SearchRequest(file, field, value);
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
