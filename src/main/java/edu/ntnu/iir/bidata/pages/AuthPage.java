package edu.ntnu.iir.bidata.pages;

import edu.ntnu.iir.bidata.manager.EncryptionManager;
import edu.ntnu.iir.bidata.manager.FileManager;
import edu.ntnu.iir.bidata.manager.InterfaceManager;
import edu.ntnu.iir.bidata.models.Author;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the authentication page of the digital diary application.
 * Provides functionality for existing users to log in and new users to create accounts.
 *
 * <p>This class manages user input, validates credentials, and interacts with
 * the {@link FileManager}, {@link EncryptionManager}, and {@link HomePage} to
 * handle account persistence and navigation.
 *
 * @author Per Eric
 */
public class AuthPage {

  /** Path to the file storing plain usernames. */
  private static final String NAME_FILE = "src/main/java/edu/ntnu/iir/bidata/Database/Name.txt";

  /**
   * Handles user authentication and account creation.
   * Prompts the user to log in if they have an existing account,
   * or to create a new account if they do not.
   *
   * @param scanner the scanner used for reading user input
   * @return -1 if navigation should proceed to the home page or back,
   *         0 if authentication fails or is cancelled
   */
  public static int authenticate(Scanner scanner) {
    InterfaceManager.animatedPrint("Have I seen you before? (Do you have an account. Yes/No): ");
    String userInput = normalize(InterfaceManager.exitCheck(scanner.nextLine()));
    if (userInput == null) {
      return 0;
    }
    if (userInput.equalsIgnoreCase("back")) {
      return -1;
    }

    List<Author> authors;
    try {
      authors = FileManager.loadAuthors();
      List<String> names = loadUsernames();

      // Existing account flow
      if (userInput.equalsIgnoreCase("Yes")) {
        InterfaceManager.animatedPrint("\n\n\nWelcome back!\n");

        if (names.isEmpty()) {
          InterfaceManager.animatedPrint("No accounts exist yet.\n");
          return 0;
        }

        InterfaceManager.animatedPrint("Existing accounts:\n");
        for (String name : names) {
          InterfaceManager.animatedPrint("- " + name + "\n");
        }

        InterfaceManager.animatedPrint("Username: ");
        String name = normalize(InterfaceManager.exitCheck(scanner.nextLine()));
        if (name == null) {
          return 0;
        }
        if (name.equalsIgnoreCase("back")) {
          return -1;
        }

        if (!containsIgnoreCase(names, name)) {
          InterfaceManager.animatedPrint("Sorry, that account does not exist.\n");
          return 0;
        }

        InterfaceManager.animatedPrint("Password: ");
        String password = InterfaceManager.exitCheck(scanner.nextLine());
        if (password == null) {
          return 0;
        }

        for (Author author : authors) {
          try {
            String decrypted = EncryptionManager.decrypt(author.getId(), name + ":" + password);
            if (decrypted.equals(name)) {
              InterfaceManager.animatedPrint("Welcome back, " + name + "!\n");
              HomePage.home(scanner, name, password);
              return -1;
            }
          } catch (Exception ignored) {
            // Ignore decryption errors
          }
        }

        InterfaceManager.animatedPrint("Incorrect password.\n");
        return 0;

      // New account flow
      } else if (userInput.equalsIgnoreCase("No")) {
        InterfaceManager.animatedPrint("Love meeting new people. What's your name?\n");
        InterfaceManager.animatedPrint("Username: ");
        String name = normalize(InterfaceManager.exitCheck(scanner.nextLine()));
        if (name == null) {
          return 0;
        }
        if (name.equalsIgnoreCase("back")) {
          return -1;
        }

        if (containsIgnoreCase(names, name)) {
          InterfaceManager.animatedPrint("That name is already taken. Please choose another.\n");
          return 0;
        }

        InterfaceManager.animatedPrint("Password: ");
        String password1 = InterfaceManager.exitCheck(scanner.nextLine());
        if (password1 == null) {
          return 0;
        }

        InterfaceManager.animatedPrint("Retype password: ");
        String password2 = InterfaceManager.exitCheck(scanner.nextLine());
        if (password2 == null) {
          return 0;
        }

        if (!password1.equals(password2)) {
          InterfaceManager.animatedPrint("Passwords do not match.\n");
          return 0;
        }

        try {
          String encryptedId = EncryptionManager.encrypt(name, name + ":" + password1);
          Author newAuthor = new Author(encryptedId);
          authors.add(newAuthor);
          FileManager.saveAuthors(authors);

          try (BufferedWriter writer = new BufferedWriter(new FileWriter(NAME_FILE, true))) {
            writer.write(name);
            writer.newLine();
          }

          InterfaceManager.animatedPrint("Account created successfully!\n");
          HomePage.home(scanner, name, password1);
          return -1;
        } catch (Exception e) {
          e.printStackTrace();
          return 0;
        }
      }

      return 0;

    } catch (IOException errorMessage) {
      errorMessage.printStackTrace();
    }
    return 0;
  }

  /**
   * Loads all usernames from the name file.
   *
   * @return a list of usernames
   */
  private static List<String> loadUsernames() {
    List<String> names = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(NAME_FILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (!line.trim().isEmpty()) {
          names.add(line.trim());
        }
      }
    } catch (IOException ignored) {
      // Ignore file read errors
    }
    return names;
  }

  /**
   * Checks if a list contains a given value, ignoring case.
   *
   * @param list  the list of strings
   * @param value the value to check
   * @return true if the list contains the value, false otherwise
   */
  private static boolean containsIgnoreCase(List<String> list, String value) {
    for (String s : list) {
      if (s.equalsIgnoreCase(value)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Normalizes input by trimming whitespace.
   *
   * @param input the input string
   * @return the trimmed string, or null if input is null
   */
  private static String normalize(String input) {
    return input == null ? null : input.trim();
  }
}