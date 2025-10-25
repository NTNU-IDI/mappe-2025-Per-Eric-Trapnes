package edu.ntnu.iir.bidata.Manager;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import edu.ntnu.iir.bidata.Models.Author;
import edu.ntnu.iir.bidata.Models.DiaryPage;

public class PageManager {

    // --- Option 1: View and open pages ---
    public static void viewPages(Scanner scanner, Author author) {
        System.out.println("\nPages for " + author.getUID() + ":");
        List<DiaryPage> pages = author.getPages();

        if (pages == null || pages.isEmpty()) {
            System.out.println("(No pages yet.)");
            return;
        }

        for (int i = 0; i < pages.size(); i++) {
            DiaryPage page = pages.get(i);
            System.out.println((i + 1) + ". " + page.getDiaryID() +
                    " (Created: " + page.getCreatedTime() +
                    (page.getEditedTime().isEmpty() ? "" : ", Edited: " + page.getEditedTime()) + ")");
        }

        System.out.print("Enter the number of the page to open (or 0 to cancel): ");
        String input = scanner.nextLine().trim();

        try {
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
            File pageFile = new File("src/main/java/edu/ntnu/iir/bidata/Database/Pages",
                    selectedPage.getDiaryID() + ".txt");

            if (!pageFile.exists()) {
                System.out.println("File not found: " + pageFile.getAbsolutePath());
                return;
            }

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.edit(pageFile);
            } else {
                System.out.println("Desktop editing not supported on this system.");
                return;
            }

            System.out.println("File opened in editor. Close the editor when done, then press Enter here...");
            scanner.nextLine(); // wait for Enter

            // Update edited time
            selectedPage.setEditedTime(LocalDateTime.now().toString());
            UIManager.animatedPrint("file closed by user\n");

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        } catch (IOException e) {
            System.err.println("Error opening page: " + e.getMessage());
        }
    }

    // --- Option 2: Write a new page ---
    public static void writePage(Scanner scanner, Author author) {
        UIManager.animatedPrint("Enter a title for your new page: ");
        String title = UIManager.exitCheck(scanner.nextLine().trim());

        if (title == null || title.equalsIgnoreCase("back")) {
            UIManager.animatedPrint("Cancelled creating page.\n");
            return;
        }

        // Sanitize filename
        String safeTitle = title.replaceAll("[\\\\/:*?\"<>|]", "_");

        // Create file path inside Database/Pages
        File dir = new File("src/main/java/edu/ntnu/iir/bidata/Database/Pages");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String diaryID = author.getUID() + "_" + safeTitle;
        File pageFile = new File(dir, diaryID + ".txt");

        try {
            if (!pageFile.exists()) {
                pageFile.createNewFile();
            }

            // Add DiaryPage object to author's list if not already present
            boolean exists = author.getPages().stream()
                    .anyMatch(p -> p.getDiaryID().equals(diaryID));
            if (!exists) {
                DiaryPage newPage = new DiaryPage(diaryID, LocalDateTime.now().toString());
                author.getPages().add(newPage);
            }

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.edit(pageFile);
            } else {
                System.out.println("Desktop editing not supported on this system.");
                return;
            }

            System.out.println("File opened in editor. Close the editor when done, then press Enter here...");
            scanner.nextLine(); // wait for Enter

            // Update edited time
            author.getPages().stream()
                    .filter(p -> p.getDiaryID().equals(diaryID))
                    .findFirst()
                    .ifPresent(p -> p.setEditedTime(LocalDateTime.now().toString()));

            UIManager.animatedPrint("file closed by user\n");

        } catch (IOException e) {
            System.err.println("Error writing page: " + e.getMessage());
        }
    }
}