package edu.ntnu.iir.bidata.Pages;

import edu.ntnu.iir.bidata.Manager.EncryptionManager;
import edu.ntnu.iir.bidata.Manager.FileManager;
import edu.ntnu.iir.bidata.Manager.UIManager;
import edu.ntnu.iir.bidata.Models.Author;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoginPage {

    private static final String usernameFile = "src/main/java/edu/ntnu/iir/bidata/Database/Username.txt";

    public static int login(Scanner scanner) {
        UIManager.animatedPrint("Have I seen you before? (Do you have an account. Yes/No): ");
        String userInput = normalize(UIManager.exitCheck(scanner.nextLine()));
        if (userInput == null)
            return 0;
        if (userInput.equalsIgnoreCase("back"))
            return -1;

        List<Author> authors;
        try {
            authors = FileManager.loadAuthors();
            List<String> usernames = loadUsernames();

            // --- Existing account ---
            if (userInput.equalsIgnoreCase("Yes")) {
                UIManager.animatedPrint("\n\n\nWelcome back!\n");

                if (usernames.isEmpty()) {
                    UIManager.animatedPrint("No accounts exist yet.\n");
                    return 0;
                }

                UIManager.animatedPrint("Existing accounts:\n");
                for (String name : usernames) {
                    UIManager.animatedPrint("- " + name + "\n");
                }

                UIManager.animatedPrint("Username: ");
                String username = normalize(UIManager.exitCheck(scanner.nextLine()));
                if (username == null)
                    return 0;
                if (username.equalsIgnoreCase("back"))
                    return -1;

                if (!containsIgnoreCase(usernames, username)) {
                    UIManager.animatedPrint("Sorry, that account does not exist.\n");
                    return 0;
                }

                // Ask for password
                UIManager.animatedPrint("Password: ");
                String password = UIManager.exitCheck(scanner.nextLine());
                if (password == null)
                    return 0;

                // Verify against Users.json
                for (Author author : authors) {
                    try {
                        String decrypted = EncryptionManager.decrypt(author.getUID(), username + ":" + password);
                        if (decrypted.equals(username)) {
                            UIManager.animatedPrint("Welcome back, " + username + "!\n");
                            HomePage.home(scanner, username, password);
                            return -1;
                        }
                    } catch (Exception ignored) {
                    }
                }

                UIManager.animatedPrint("Incorrect password.\n");
                return 0;

                // --- New yaccount ---
            } else if (userInput.equalsIgnoreCase("No")) {
                UIManager.animatedPrint("Love meeting new people. What's your name?\n");
                UIManager.animatedPrint("Username: ");
                String username = normalize(UIManager.exitCheck(scanner.nextLine()));
                if (username == null)
                    return 0;
                if (username.equalsIgnoreCase("back"))
                    return -1;

                if (containsIgnoreCase(usernames, username)) {
                    UIManager.animatedPrint("That username is already taken. Please choose another.\n");
                    return 0;
                }

                // Ask for password twice
                UIManager.animatedPrint("Password: ");
                String password1 = UIManager.exitCheck(scanner.nextLine());
                if (password1 == null)
                    return 0;

                UIManager.animatedPrint("Retype password: ");
                String password2 = UIManager.exitCheck(scanner.nextLine());
                if (password2 == null)
                    return 0;

                if (!password1.equals(password2)) {
                    UIManager.animatedPrint("Passwords do not match.\n");
                    return 0;
                }

                try {
                    // Encrypt UID with username+password
                    String encryptedUID = EncryptionManager.encrypt(username, username + ":" + password1);

                    // Save to Users.json
                    Author newAuthor = new Author(encryptedUID);
                    authors.add(newAuthor);
                    FileManager.saveAuthors(authors);

                    // Save plain username to Username.txt
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(usernameFile, true))) {
                        writer.write(username);
                        writer.newLine();
                    }

                    UIManager.animatedPrint("Account created successfully!\n");
                    HomePage.home(scanner, username, password1);
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

    private static List<String> loadUsernames() {
        List<String> usernames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(usernameFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    usernames.add(line.trim());
                }
            }
        } catch (IOException ignored) {
        }
        return usernames;
    }

    private static boolean containsIgnoreCase(List<String> list, String value) {
        for (String s : list) {
            if (s.equalsIgnoreCase(value))
                return true;
        }
        return false;
    }

    private static String normalize(String input) {
        return input == null ? null : input.trim();
    }
}