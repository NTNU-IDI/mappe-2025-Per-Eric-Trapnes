package edu.ntnu.iir.bidata.Manager;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import edu.ntnu.iir.bidata.Models.Author;

public class PageManager {

    // --- Option 1: View and open pages ---
    public static void viewPages(Scanner scanner, Author author) {
        System.out.println("\nPages for " + author.getUID() + ":");
        List<String> pages = author.getPages();

        if (pages == null || pages.isEmpty()) {
            System.out.println("(No pages yet.)");
            return;
        }

        for (int i = 0; i < pages.size(); i++) {
            File f = new File(pages.get(i));
            System.out.println((i + 1) + ". " + f.getName());
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

            File pageFile = new File(pages.get(choice - 1));
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

        File pageFile = new File(dir, author.getUID() + "_" + safeTitle + ".txt");

        try {
            if (!pageFile.exists()) {
                pageFile.createNewFile();
            }

            // Add file path to author's page list if not already present
            if (!author.getPages().contains(pageFile.getAbsolutePath())) {
                author.getPages().add(pageFile.getAbsolutePath());
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
            UIManager.animatedPrint("file closed by user\n");

        } catch (IOException e) {
            System.err.println("Error writing page: " + e.getMessage());
        }
    }
}