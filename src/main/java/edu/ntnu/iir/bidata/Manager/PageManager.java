package edu.ntnu.iir.bidata.Manager;

import java.awt.Desktop;
import java.io.File;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import edu.ntnu.iir.bidata.Models.Author;
import edu.ntnu.iir.bidata.Models.DiaryPage;
import edu.ntnu.iir.bidata.Models.Time;

public class PageManager {

    private static File PAGES_DIR = new File("src/main/java/edu/ntnu/iir/bidata/Database/Pages");
    private static File DRAFT_FILE = new File(PAGES_DIR, "draft.txt");

    // --- Option 1: View and open pages ---
    public static void viewPages(Scanner scanner, String username, String UID) {
        try {
            Author author = FileManager.findAuthor(UID);
            List<DiaryPage> pages = author.getPages();

            System.out.println("\nPages for " + username + ":");
            if (pages == null || pages.isEmpty()) {
                System.out.println("(No pages yet.)");
                return;
            }

            for (int i = 0; i < pages.size(); i++) {
                DiaryPage page = pages.get(i);
                System.out.println((i + 1) + ". " + page.getDiaryID(UID) +
                        " (Created: " + page.getCreatedTime(UID) +
                        (page.getEditedTime(UID).isEmpty() ? "" : ", Edited: " + page.getEditedTime(UID)) + ")");
            }

            System.out.print("Enter the number of the page to open (or 0 to cancel): ");
            String input = UIManager.exitCheck(scanner.nextLine().trim());

            int choice = Integer.parseInt(input);
            if (choice == 0) {
                System.out.println("Cancelled.");
                return;
            }
            if (choice < 1 || choice > pages.size()) {
                System.out.println("Invalid choice.");
                return;
            }

            DiaryPage selectedPage = pages.get(choice - 1);

            // Use the same Base64-safe scheme as writePage
            String encryptedDiaryID = EncryptionManager.encrypt(selectedPage.getDiaryID(UID), UID);
            String safeEncryptedID = Base64.getUrlEncoder().encodeToString(
                    encryptedDiaryID.getBytes(StandardCharsets.UTF_8));

            File encryptedFile = new File(PAGES_DIR, safeEncryptedID + ".txt");
            if (!encryptedFile.exists()) {
                System.out.println("File not found: " + encryptedFile.getAbsolutePath());
                return;
            }

            String encryptedContent = Files.readString(encryptedFile.toPath());
            String decryptedContent = EncryptionManager.decrypt(encryptedContent, UID);
            Files.writeString(DRAFT_FILE.toPath(), decryptedContent);

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().edit(DRAFT_FILE);
            } else {
                System.out.println("Desktop editing not supported on this system.");
                return;
            }

            System.out.println("Draft opened. Press Enter in terminal when done editing...");
            scanner.nextLine();

            String newContent = Files.readString(DRAFT_FILE.toPath());
            String reEncrypted = EncryptionManager.encrypt(newContent, UID);
            Files.writeString(encryptedFile.toPath(), reEncrypted);

            selectedPage.setEditedTime(UID, Time.now());
            FileManager.saveAuthor(author);
            Files.writeString(DRAFT_FILE.toPath(), "");
            UIManager.animatedPrint("Changes saved.\n");

        } catch (Exception errorMessage) {
            errorMessage.printStackTrace();
        }
    }

    // --- Option 2: Write a new page ---
    public static void writePage(Scanner scanner, String username, String UID, boolean skipEditor) {
        try {
            Author author = FileManager.findAuthor(UID);
            if (author == null) {
                throw new IllegalStateException("No author found for UID: " + UID);
            }

            UIManager.animatedPrint("Enter a title for your new page: ");
            String title = UIManager.exitCheck(scanner.nextLine().trim());
            if (title == null || title.equalsIgnoreCase("back"))
                return;

            String safeTitle = title.replaceAll("[\\\\/:*?\"<>| ]+", "_");
            if (!PAGES_DIR.exists())
                PAGES_DIR.mkdirs();

            String diaryID = username + "_" + safeTitle;
            String encryptedDiaryID = EncryptionManager.encrypt(diaryID, UID);
            String safeEncryptedID = Base64.getUrlEncoder().encodeToString(
                    encryptedDiaryID.getBytes(StandardCharsets.UTF_8));

            File encryptedFile = new File(PAGES_DIR, safeEncryptedID + ".txt");
            if (!encryptedFile.exists())
                encryptedFile.createNewFile();

            boolean exists = author.getPages().stream()
                    .anyMatch(p -> Objects.equals(p.getDiaryID(UID), diaryID));
            if (!exists) {
                DiaryPage newPage = new DiaryPage(UID, encryptedDiaryID, Time.now());
                author.getPages().add(newPage);
            }

            if (!DRAFT_FILE.exists())
                DRAFT_FILE.createNewFile();
            Files.writeString(DRAFT_FILE.toPath(), "");

            if (skipEditor) {
                // For testing: inject dummy content
                Files.writeString(DRAFT_FILE.toPath(), "This is test content.");
            } else {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().edit(DRAFT_FILE);
                } else {
                    System.out.println("Desktop editing not supported. Please edit " + DRAFT_FILE.getAbsolutePath());
                }
                System.out.println("\nDraft opened. Press Enter in terminal when done...");
                scanner.nextLine();
            }

            String content = Files.readString(DRAFT_FILE.toPath());
            String encryptedContent = EncryptionManager.encrypt(content, UID);
            Files.writeString(encryptedFile.toPath(), encryptedContent);

            author.getPages().stream()
                    .filter(p -> Objects.equals(p.getDiaryID(UID), diaryID))
                    .findFirst()
                    .ifPresent(p -> p.setEditedTime(UID, Time.now()));

            FileManager.saveAuthor(author);
            Files.writeString(DRAFT_FILE.toPath(), "");
            UIManager.animatedPrint("Page saved.\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setPagesDir(File dir) {
        PAGES_DIR = dir;
    }

    public static void setDraftFile(File file) {
        DRAFT_FILE = file;
    }

}