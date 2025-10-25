package edu.ntnu.iir.bidata.Pages;

import edu.ntnu.iir.bidata.Manager.FileManager;
import edu.ntnu.iir.bidata.Manager.PageManager;
import edu.ntnu.iir.bidata.Manager.UIManager;
import edu.ntnu.iir.bidata.Models.Author;

import java.io.IOException;

import java.util.Scanner;

public class HomePage {

    public static void home(Scanner scanner, String UID) {
        UIManager.animatedPrint("\n\n\n\n\n\n\nLovely to see you " + UID + "\n\n");

        try {
            Author author = FileManager.findAuthor(UID);

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
