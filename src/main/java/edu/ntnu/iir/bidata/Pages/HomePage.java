package edu.ntnu.iir.bidata.pages;

import edu.ntnu.iir.bidata.manager.EncryptionManager;
import edu.ntnu.iir.bidata.manager.FileManager;
import edu.ntnu.iir.bidata.manager.InterfaceManager;
import edu.ntnu.iir.bidata.manager.PageManager;
import java.io.IOException;
import java.util.Scanner;

/**
 * Represents the home page of the digital diary application.
 * Provides access to viewing, writing, and deleting diary pages,
 * as well as logging out of the session.
 *
 * <p>This page is shown after successful authentication.
 * It uses the user's encrypted ID to manage page operations.
 * 
 * @author Per Eric
 */
public class HomePage {

  /**
   * Displays the home menu and handles user interaction.
   * Allows the user to view, write, or delete diary pages,
   * or log out of the application.
   *
   * @param scanner the scanner used for reading user input
   * @param username the authenticated username
   * @param password the corresponding password for encryption
   */
  public static void home(Scanner scanner, String username, String password) {
    try {
      String id = EncryptionManager.encrypt(username, username + ":" + password);
      FileManager.findAuthor(id);
      boolean running = true;

      while (running) {
        InterfaceManager.clearScreen(scanner);
        InterfaceManager.animatedPrint(username.toUpperCase());
        InterfaceManager.animatedPrint("\n\n==== HOME MENU ====\n");
        InterfaceManager.animatedPrint("1. View pages\n");
        InterfaceManager.animatedPrint("2. Write page\n");
        InterfaceManager.animatedPrint("3. Log out\n");
        InterfaceManager.animatedPrint("4. Delete page\n");
        InterfaceManager.animatedPrint("Enter your choice: ");

        String choice = InterfaceManager.exitCheck(scanner);
        if (choice != null) {
          switch (choice) {
            case "1":
              InterfaceManager.clearScreen(scanner);
              PageManager.viewPages(scanner, username, id);
              break;
            case "2":
              InterfaceManager.clearScreen(scanner);
              PageManager.writePage(scanner, username, id);
              break;
            case "3":
              InterfaceManager.clearScreen(scanner);
              InterfaceManager.animatedPrint("\n\n\nAre you sure? (If so type 'yes')\n");
              String confirm = scanner.nextLine().trim();
              if (confirm.isEmpty()) {
                InterfaceManager.animatedPrint("No input. Logout cancelled.\n");
                break;
              }
              if (confirm.equalsIgnoreCase("yes")) {
                InterfaceManager.animatedPrint("\n\nLogging out... Goodbye, " + username + "\n");
                running = false;
              } else {
                InterfaceManager.animatedPrint("Logout cancelled.\n");
              }
              break;
            case "4":
              InterfaceManager.clearScreen(scanner);
              PageManager.deletePage(scanner, username, id);
              break;
            default:
              InterfaceManager.animatedPrint("Invalid choice. Please try again.\n");
          }
        }
      }
    } catch (IOException | RuntimeException error) {
      InterfaceManager.errorHandling(error, scanner);
    } catch (Exception error) {
      InterfaceManager.errorHandling(error, scanner);
    }
  }
}