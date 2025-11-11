package edu.ntnu.iir.bidata.Pages;

import edu.ntnu.iir.bidata.Manager.EncryptionManager;
import edu.ntnu.iir.bidata.Manager.FileManager;
import edu.ntnu.iir.bidata.Manager.PageManager;
import edu.ntnu.iir.bidata.Manager.UIManager;

import java.io.IOException;
import java.util.Scanner;

public class HomePage {

    public static void home(Scanner scanner, String username, String password) {

        UIManager.animatedPrint("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nLovely to see you " + username + "\n\n");

        try {
            String UID = EncryptionManager.encrypt(username, username + ":" + password);
            FileManager.findAuthor(UID);

            boolean running = true;
            while (running) {
                UIManager.animatedPrint("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n==== HOME MENU ====\n");
                UIManager.animatedPrint("1. View pages\n");
                UIManager.animatedPrint("2. Write page\n");
                UIManager.animatedPrint("3. Log out\n");
                UIManager.animatedPrint("4. Delete page\n");
                UIManager.animatedPrint("Enter your choice: ");

                String choice = UIManager.exitCheck(scanner.nextLine().trim());
                if (choice.isEmpty()) {
                    UIManager.animatedPrint("No input detected. Please enter a number.\n");
                    continue;
                }

                switch (choice) {
                    case "1":
                        PageManager.viewPages(scanner, username, UID);
                        break;

                    case "2":
                        PageManager.writePage(scanner, username, UID);
                        break;

                    case "3":
                        UIManager.animatedPrint("\n\n\nAre you sure? (If so type 'yes')\n");
                        String confirm = scanner.nextLine().trim();
                        if (confirm.isEmpty()) {
                            UIManager.animatedPrint("No input. Logout cancelled.\n");
                            break;
                        }

                        if (confirm.equalsIgnoreCase("yes")) {
                            UIManager.animatedPrint("\n\n\n\n\n\n\n\n\n\n\nLogging out... Goodbye, " + username + "\n");
                            running = false;
                        } else {
                            UIManager.animatedPrint("Logout cancelled.\n");
                        }
                        break;
                    case "4":
                        PageManager.deletePage(scanner, username, UID);
                        break;

                    default:
                        UIManager.animatedPrint("Invalid choice. Please try again.\n");
                }
            }

        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}