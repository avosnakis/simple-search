package org.example.simplesearch.search.commands;

import org.example.simplesearch.search.SearchVisitor;

/**
 * Interface for commands.
 */
public interface SearchNode {

  /**
   * @param visitor The visitor to accept.
   */
  void accept(SearchVisitor visitor);
}
