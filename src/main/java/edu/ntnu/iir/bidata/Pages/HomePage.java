package edu.ntnu.iir.bidata.Pages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.ntnu.iir.bidata.Manager.PageManager;
import edu.ntnu.iir.bidata.Manager.UIManager;
import edu.ntnu.iir.bidata.Models.Author;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class HomePage {

    public static void home(Scanner scanner, String UID) {
        UIManager.animatedPrint("\n\n\n\n\n\n\nLovely to see you " + UID + "\n\n");

        Author author = new Author(UID, new ArrayList<>());
        String filePath = "src/main/java/edu/ntnu/iir/bidata/Database/Users.JSON";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            List<Author> authors = new ArrayList<>();

            // Load existing data if file has content
            if (file.exists() && file.length() > 0) {
                Reader reader = new FileReader(file);
                Author[] existing = gson.fromJson(reader, Author[].class);
                reader.close();

                if (existing != null) {
                    authors.addAll(Arrays.asList(existing));
                }
            }

            // Check if UID exists
            boolean found = false;
            for (Author a : authors) {
                if (a.getUID().equals(UID)) {
                    author = a;
                    found = true;
                    break;
                }
            }

            if (!found) {
                authors.add(author);
            }

            // Save updated data
            Writer writer = new FileWriter(file);
            gson.toJson(authors, writer);
            writer.close();

            // Menu loop
            boolean running = true;
            while (running) {
                UIManager.animatedPrint("\n==== HOME MENU ====\n");
                UIManager.animatedPrint("1. View pages\n");
                UIManager.animatedPrint("2. Write page\n");
                UIManager.animatedPrint("3. Log out\n");
                UIManager.animatedPrint("Enter your choice: ");

                String choice = UIManager.exitCheck(scanner.nextLine().trim());

                switch (choice) {
                    case "1":
                        PageManager.viewPages(scanner, author);
                        break;
                    case "2":
                        PageManager.writePage(scanner, author);
                        break;
                    case "3":
                        UIManager.animatedPrint("\nAre you sure? (If so type 'yes')\n");
                        choice = UIManager.exitCheck(scanner.nextLine().trim());

                        if (choice.equalsIgnoreCase("yes")) {
                            UIManager.animatedPrint("\nLogging out... Goodbye, " + UID + "\n");
                            running = false;
                        }

                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
