package edu.ntnu.iir.bidata.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an author in the digital diary application.
 * Each author is identified by an encrypted ID and maintains
 * a collection of associated diary pages.
 *
 * <p>This class provides methods to access and update the author's
 * identifier and their list of diary pages.
 *
 * @author Per Eric
 */
public class Author {
  private String id;
  private List<DiaryPage> pages;

  /**
   * Creates a new author with the given identifier and
   * an empty list of diary pages.
   *
   * @param id the unique identifier for the author
   */
  public Author(String id) {
    this.id = id;
    this.pages = new ArrayList<>();
  }

  /**
   * Creates a new author with the given identifier and
   * an initial list of diary pages.
   *
   * @param id    the unique identifier for the author
   * @param pages the initial list of diary pages
   */
  public Author(String id, List<DiaryPage> pages) {
    this.id = id;
    this.pages = new ArrayList<>(pages);
  }

  /**
   * Returns the author's identifier.
   *
   * @return the author's ID
   */
  public String getId() {
    return id;
  }

  /**
   * Updates the author's identifier.
   *
   * @param id the new ID to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns the list of diary pages associated with the author.
   *
   * @return the list of diary pages
   */
  public List<DiaryPage> getPages() {
    return pages;
  }

  /**
   * Updates the list of diary pages associated with the author.
   *
   * @param pages the new list of diary pages
   */
  public void setPages(List<DiaryPage> pages) {
    this.pages = new ArrayList<>(pages);
  }

  @Override
  public String toString() {
    return "Author{"
        + "id='" + id + '\''
        + ", pages=" + pages
        + '}';
  }
}
