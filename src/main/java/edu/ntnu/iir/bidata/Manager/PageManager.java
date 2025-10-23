package edu.ntnu.iir.bidata.Manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

import edu.ntnu.iir.bidata.Models.Author;

public class PageManager {
    // --- Option 1: View pages ---
    public static void viewPages(Author author) {
        System.out.println("\nPages for " + author.getUID() + ":");
        List<String> pages = author.getPages();

        if (pages == null || pages.isEmpty()) {
            System.out.println("(No pages yet.)");
        } else {
            for (int i = 0; i < pages.size(); i++) {
                System.out.println((i + 1) + ". " + pages.get(i));
            }
        }
    }

    // --- Option 2: Write page ---
    public static void writePage(Scanner scanner, Author author, List<Author> authors, File file, Gson gson) {
        UIManager.animatedPrint("\nEnter new page name: ");
        String newPage = scanner.nextLine().trim();

        if (newPage.isEmpty()) {
            System.out.println("Page name cannot be empty.");
            return;
        }

        author.getPages().add(newPage);
        System.out.println("Added page: " + newPage);

        // Update the stored JSON
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(authors, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
