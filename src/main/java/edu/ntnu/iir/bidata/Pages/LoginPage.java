package edu.ntnu.iir.bidata.Pages;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.ntnu.iir.bidata.Manager.UIManager;

public class LoginPage {

    private static final String filePath = "src\\main\\java\\edu\\ntnu\\iir\\bidata\\Database\\Username.txt";

    public static int login(Scanner scanner) {
        UIManager.animatedPrint("Have I seen you before? (Do you have an account. Yes/No): ");
        String userInput = normalize(UIManager.exitCheck(scanner.nextLine()));
        if (userInput == null)
            return 0;
        if (userInput.equalsIgnoreCase("back"))
            return -1;

        // Load all usernames from file
        List<String> usernames = loadUsernames();

        // If has account
        if (userInput.equalsIgnoreCase("Yes")) {
            UIManager.animatedPrint("\n\n\nWelcome back!\n");

            if (usernames.isEmpty()) {
                UIManager.animatedPrint("No accounts exist yet.\n");
                return 0;
            } else {
                UIManager.animatedPrint("Existing accounts:\n");
                for (String name : usernames) {
                    UIManager.animatedPrint("- " + name + "\n");
                }
            }

            UIManager.animatedPrint("Username: ");
            String username = normalize(UIManager.exitCheck(scanner.nextLine()));
            if (username == null)
                return 0;
            if (username.equalsIgnoreCase("back"))
                return -1;

            if (containsIgnoreCase(usernames, username)) {
                UIManager.animatedPrint("Welcome back, " + username + "!\n");
                HomePage.home(scanner, username);
                return -1;
            } else {
                UIManager.animatedPrint("Sorry, that account does not exist.\n");
                return 0;
            }

            // If does not have account
        } else if (userInput.equalsIgnoreCase("No")) {
            UIManager.animatedPrint("Love meeting new people. What's your name?\n");
            UIManager.animatedPrint("Username: ");

            String username = normalize(UIManager.exitCheck(scanner.nextLine()));
            if (username == null)
                return 0;
            if (username.equalsIgnoreCase("back"))
                return -1;

            // Check if username already exists
            if (containsIgnoreCase(usernames, username)) {
                UIManager.animatedPrint("That username is already taken. Please choose another.\n");
                return 0;
            }

            // Append new username to file
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath, true))) {
                bufferedWriter.write(username);
                bufferedWriter.newLine(); // ensure separation
                UIManager.animatedPrint("Saved successfully!\n");
            } catch (IOException errorMessage) {
                System.err.println("An error occurred while writing to the file: " + errorMessage.getMessage());
                return 0;
            }
            HomePage.home(scanner, username);
            return -1;
        }

        return 0;
    }

    // Helper method to load usernames into a list
    private static List<String> loadUsernames() {
        List<String> usernames = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    usernames.add(line.trim());
                }
            }
        } catch (IOException e) {
            // If file doesn't exist yet, just return empty list
        }
        return usernames;
    }

    // Normalize input (trim whitespace)
    private static String normalize(String input) {
        return input == null ? null : input.trim();
    }

    // Case-insensitive contains check
    private static boolean containsIgnoreCase(List<String> list, String value) {
        for (String s : list) {
            if (s.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}