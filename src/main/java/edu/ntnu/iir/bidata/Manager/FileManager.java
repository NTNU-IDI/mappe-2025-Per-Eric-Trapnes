package edu.ntnu.iir.bidata.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.iir.bidata.models.Author;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides file management utilities for the digital diary application.
 * Handles persistence of {@link Author} objects by reading and writing
 * them to a JSON file.
 *
 * <p>This class uses Gson for serialization and deserialization of
 * author data. It ensures that user accounts and their associated
 * diary pages are stored and retrieved consistently.
 *
 * @author Per Eric
 */
public class FileManager {

  /** JSON file used to store author data. */
  private static final File jsonFile =
      new File("src/main/java/edu/ntnu/iir/bidata/Database/Users.JSON");

  /** Gson instance configured for pretty printing. */
  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  /**
   * Loads all authors from the JSON file.
   * If the file does not exist or is empty, an empty list is returned.
   *
   * @return a list of authors
   * @throws IOException if an error occurs while reading the file
   */
  public static List<Author> loadAuthors() throws IOException {
    if (!jsonFile.exists() || jsonFile.length() == 0) {
      jsonFile.getParentFile().mkdirs();
      jsonFile.createNewFile();
      return new ArrayList<>();
    }
    try (Reader reader = new FileReader(jsonFile)) {
      Author[] array = gson.fromJson(reader, Author[].class);
      return array == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(array));
    }
  }

  /**
   * Finds an author by ID. If the author does not exist,
   * a new author is created, added to the list, and saved.
   *
   * @param id the identifier of the author
   * @return the existing or newly created author
   * @throws IOException if an error occurs while reading or writing the file
   */
  public static Author findAuthor(String id) throws IOException {
    List<Author> authors = loadAuthors();
    for (Author author : authors) {
      if (author.getId().equals(id)) {
        return author;
      }
    }
    Author newAuthor = new Author(id);
    authors.add(newAuthor);
    saveAuthors(authors);
    return newAuthor;
  }

  /**
   * Saves the list of authors to the JSON file.
   *
   * @param authors the list of authors to save
   * @throws IOException if an error occurs while writing the file
   */
  public static void saveAuthors(List<Author> authors) throws IOException {
    try (FileWriter writer = new FileWriter(jsonFile)) {
      gson.toJson(authors, writer);
    }
  }

  /**
   * Saves a single author to the JSON file.
   * If the author already exists, it is updated; otherwise, it is added.
   *
   * @param updatedAuthor the author to save or update
   * @throws IOException if an error occurs while writing the file
   */
  public static void saveAuthor(Author updatedAuthor) throws IOException {
    List<Author> authors = loadAuthors();
    for (int i = 0; i < authors.size(); i++) {
      if (authors.get(i).getId().equals(updatedAuthor.getId())) {
        authors.set(i, updatedAuthor);
        saveAuthors(authors);
        return;
      }
    }
    authors.add(updatedAuthor);
    saveAuthors(authors);
  }
}