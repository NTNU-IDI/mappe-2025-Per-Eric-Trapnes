package edu.ntnu.iir.bidata.manager;

import edu.ntnu.iir.bidata.models.Author;
import edu.ntnu.iir.bidata.models.DiaryPage;
import edu.ntnu.iir.bidata.models.Time;
import java.awt.Desktop;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Manages diary pages in the digital diary application.
 * Provides functionality to view, write, and delete pages,
 * as well as handling draft editing and persistence.
 *
 * <p>This class interacts with {@link Author}, {@link DiaryPage},
 * and {@link FileManager} to manage encrypted page storage and
 * metadata updates. It ensures that diary content is securely
 * stored and retrieved using {@link EncryptionManager}.
 *
 * <p>Pages are stored as encrypted files in a designated directory,
 * while a temporary draft file is used for editing content before
 * saving changes.
 *
 * @author Per Eric
 */
public class PageManager {

  /** Directory where encrypted diary pages are stored. */
  private static File PAGES_DIR = new File("src/main/java/edu/ntnu/iir/bidata/Database/Pages");

  /** File used as a temporary draft for editing diary pages. */
  private static File DRAFT_FILE = new File(PAGES_DIR, "draft.txt");

  /**
   * Displays all pages for the given author and allows the user
   * to open and edit a selected page. The selected page is decrypted,
   * opened in a draft file for editing, and then re-encrypted upon saving.
   *
   * @param scanner  the scanner used for user input
   * @param username the username of the author
   * @param id       the encryption key or identifier
   */
  public static void viewPages(Scanner scanner, String username, String id) {
    try {
      Author author = FileManager.findAuthor(id);
      List<DiaryPage> pages = author.getPages();

      System.out.println("\nPages for " + username + ":");
      if (pages == null || pages.isEmpty()) {
        System.out.println("(No pages yet.)");
        return;
      }

      int nameWidth = 35;
      int dateWidth = 35;

      System.out.printf("%-4s %-" + nameWidth + "s %-" + dateWidth + "s %-" + dateWidth + "s%n",
          "#", "Page Name", "Created", "Edited");

      for (int i = 0; i < pages.size(); i++) {
        DiaryPage page = pages.get(i);

        String name = page.getDiaryId(id, scanner);
        String created = page.getCreatedTime(id, scanner);
        String edited = page.getEditedTime(id, scanner).isEmpty() 
            ? "-" : page.getEditedTime(id, scanner);

        if (name.length() > nameWidth - 3) {
          name = name.substring(0, nameWidth - 6) + "...";
        }

        System.out.printf("%-4d %-" + nameWidth + "s %-" + dateWidth + "s %-" + dateWidth + "s%n",
            (i + 1), name, created, edited);
      }

      System.out.print("Enter the number of the page to open (or 0 to cancel): ");
      String input = InterfaceManager.exitCheck(scanner);

      if (input == null || input.isEmpty()) {
        System.out.println("No input detected. Cancelled.");
        return;
      }

      int choice;
      try {
        choice = Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a number.");
        InterfaceManager.animatedPrint(
            "After reading the message you can move on by pressing (ENTER)");
        scanner.nextLine();
        return;
      }

      if (choice == 0) {
        System.out.println("Cancelled.");
        InterfaceManager.animatedPrint(
            "After reading the message you can move on by pressing (ENTER)");
        scanner.nextLine().trim();
        return;
      }

      if (choice < 1 || choice > pages.size()) {
        System.out.println("Invalid choice.");
        InterfaceManager.animatedPrint(
            "After reading the message you can move on by pressing (ENTER)");
        scanner.nextLine().trim();
        return;
      }

      DiaryPage selectedPage = pages.get(choice - 1);

      String encryptedDiaryId = EncryptionManager.encrypt(selectedPage.getDiaryId(id, scanner), id);
      String safeEncryptedId = Base64.getUrlEncoder().encodeToString(
          encryptedDiaryId.getBytes(StandardCharsets.UTF_8));

      File encryptedFile = new File(PAGES_DIR, safeEncryptedId + ".txt");
      if (!encryptedFile.exists()) {
        System.out.println("File not found: " + encryptedFile.getAbsolutePath());
        InterfaceManager.animatedPrint(
            "After reading the message you can move on by pressing (ENTER)");
        scanner.nextLine().trim();
        return;
      }

      String encryptedContent = Files.readString(encryptedFile.toPath());
      String decryptedContent = EncryptionManager.decrypt(encryptedContent, id);
      Files.writeString(DRAFT_FILE.toPath(), decryptedContent);

      if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().edit(DRAFT_FILE);
      } else {
        System.out.println("Desktop editing not supported on this system.");
        return;
      }

      System.out.println("Draft opened. Press Enter when done editing...");
      scanner.nextLine();

      String newContent = Files.readString(DRAFT_FILE.toPath());
      String reEncrypted = EncryptionManager.encrypt(newContent, id);
      Files.writeString(encryptedFile.toPath(), reEncrypted);

      selectedPage.setEditedTime(id, Time.now(), scanner);
      FileManager.saveAuthor(author);
      Files.writeString(DRAFT_FILE.toPath(), "");
      InterfaceManager.animatedPrint("Changes saved.\n");

    } catch (Exception error) {
      InterfaceManager.errorHandling(error, scanner);
    }
  }

  /**
   * Creates a new diary page for the given author and opens it
   * in draft mode for editing. The page is encrypted and stored
   * in the designated directory, and metadata is updated accordingly.
   *
   * @param scanner  the scanner used for user input
   * @param username the username of the author
   * @param id       the encryption key or identifier
   */
  public static void writePage(Scanner scanner, String username, String id) {
    try {
      Author author = FileManager.findAuthor(id);
      if (author == null) {
        throw new IllegalStateException("No author found for id: " + id);
      }

      InterfaceManager.animatedPrint("Enter a title for your new page: ");
      String title = InterfaceManager.exitCheck(scanner);
      if (title == null || title.equalsIgnoreCase("back")) {
        return;
      }

      String safeTitle = title.replaceAll("[\\\\/:*?\"<>| ]+", "_");
      if (!PAGES_DIR.exists()) {
        PAGES_DIR.mkdirs();
      }

      String diaryId = username + "_" + safeTitle;
      String encryptedDiaryId = EncryptionManager.encrypt(diaryId, id);
      String safeEncryptedId = Base64.getUrlEncoder().encodeToString(
          encryptedDiaryId.getBytes(StandardCharsets.UTF_8));

      File encryptedFile = new File(PAGES_DIR, safeEncryptedId + ".txt");
      if (!encryptedFile.exists()) {
        encryptedFile.createNewFile();
      }

      boolean exists = author.getPages().stream()
          .anyMatch(p -> Objects.equals(p.getDiaryId(id, scanner), diaryId));
      if (!exists) {
        DiaryPage newPage = new DiaryPage(id, encryptedDiaryId, Time.now(), scanner);
        author.getPages().add(newPage);
      }

      if (!DRAFT_FILE.exists()) {
        DRAFT_FILE.createNewFile();
      }

      Files.writeString(DRAFT_FILE.toPath(), "");

      if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().edit(DRAFT_FILE);
      } else {
        System.out.println("Not supported. Please edit " + DRAFT_FILE.getAbsolutePath());
      }
      System.out.println("\nDraft opened. Press Enter in terminal when done...");
      scanner.nextLine();

      String content = Files.readString(DRAFT_FILE.toPath());
      String encryptedContent = EncryptionManager.encrypt(content, id);
      Files.writeString(encryptedFile.toPath(), encryptedContent);

      author.getPages().stream()
          .filter(p -> Objects.equals(p.getDiaryId(id, scanner), diaryId))
          .findFirst()
          .ifPresent(p -> p.setEditedTime(id, Time.now(), scanner));

      FileManager.saveAuthor(author);
      Files.writeString(DRAFT_FILE.toPath(), "");
      InterfaceManager.animatedPrint("Page saved.\n");

    } catch (Exception error) {
      InterfaceManager.errorHandling(error, scanner);
    }
  }

  /**
   * Deletes a diary page selected by the user. Removes both the
   * metadata and the encrypted file if possible. Prompts the user
   * for confirmation before deletion.
   *
   * @param scanner  the scanner used for user input
   * @param username the username of the author
   * @param id       the encryption key or identifier
   */
  public static void deletePage(Scanner scanner, String username, String id) {
    try {
      Author author = FileManager.findAuthor(id);
      List<DiaryPage> pages = author.getPages();

      System.out.println("\nPages for " + username + ":");
      if (pages == null || pages.isEmpty()) {
        System.out.println("(No pages yet.)");
        return;
      }

      for (int i = 0; i < pages.size(); i++) {
        DiaryPage page = pages.get(i);
        System.out.println((i + 1) + ". " + page.getDiaryId(id, scanner)
            + " (Created: "
            + page.getCreatedTime(id, scanner)
            + (page.getEditedTime(id, scanner).isEmpty() ? "" : ", Edited: "
            + page.getEditedTime(id, scanner))
            + ")");
      }

      System.out.print("Enter the number of the page to delete (or 0 to cancel): ");
      String input = InterfaceManager.exitCheck(scanner);

      if (input == null || input.isEmpty()) {
        return;
      }

      int choice;
      try {
        choice = Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a number.");
        InterfaceManager.animatedPrint(
            "After reading the message you can move on by pressing (ENTER)");
        scanner.nextLine();
        return;
      }

      if (choice == 0) {
        System.out.println("Cancelled.");
        return;
      }
      if (choice < 1 || choice > pages.size()) {
        System.out.println("Invalid choice.");
        return;
      }

      DiaryPage selectedPage = pages.get(choice - 1);

      // Build file name exactly like in viewPages()
      String encryptedDiaryId = EncryptionManager.encrypt(selectedPage.getDiaryId(id, scanner), id);
      String safeEncryptedId = Base64.getUrlEncoder().encodeToString(
          encryptedDiaryId.getBytes(StandardCharsets.UTF_8));
      File encryptedFile = new File(PAGES_DIR, safeEncryptedId + ".txt");

      // Confirm deletion
      System.out.print("Are you sure you want to delete '"
          + selectedPage.getDiaryId(id, scanner) + "'? (yes/no): ");
      String confirm = scanner.nextLine().trim();
      if (!confirm.equalsIgnoreCase("yes")) {
        System.out.println("Delete cancelled.");
        return;
      }

      boolean fileDeleted = encryptedFile.exists() && encryptedFile.delete();

      // Remove page from author
      pages.remove(selectedPage);
      FileManager.saveAuthor(author);

      if (fileDeleted) {
        InterfaceManager.animatedPrint("Page deleted successfully.\n");
      } else {
        InterfaceManager.animatedPrint("Page metadata removed, but file could not be deleted.\n");
      }

    } catch (Exception error) {
      InterfaceManager.errorHandling(error, scanner);
    }
  }

  /**
   * Updates the directory used for storing diary pages.
   * Useful for testing or reconfiguration.
   *
   * @param dir the new directory
   */
  public static void setPagesDir(File dir) {
    PAGES_DIR = dir;
  }

  /**
   * Updates the file used for draft editing.
   * Useful for testing or reconfiguration.
   *
   * @param file the new draft file
   */
  public static void setDraftFile(File file) {
    DRAFT_FILE = file;
  }
}
           