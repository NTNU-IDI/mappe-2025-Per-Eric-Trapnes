package edu.ntnu.idi.idatt.Pages;

import edu.ntnu.idi.idatt.Manager.UIManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LoginPage {
    public static int login(Scanner scanner) {
        UIManager.animatedPrint("Have I seen you before? (Do you have an account. Yes/No): ");
        String userInput = UIManager.exitCheck(scanner.nextLine());
        if (userInput == null) {
            return 0;
        } else if (userInput.equalsIgnoreCase("back")) {
            return -1;
        }

        // If has account
        if (userInput.equalsIgnoreCase("Yes")) {
            // Checks the String the user gives and compares with file
            UIManager.animatedPrint("\n\n\nWelcome back!\n");
            try (BufferedReader bufferedReader = new BufferedReader(
                    new FileReader("src\\main\\java\\edu\\ntnu\\idi\\idatt\\Username.txt"))) {
                String line;
                UIManager.animatedPrint("Username: ");

                String username = UIManager.exitCheck(scanner.nextLine());
                if (username == null) {
                    return 0;
                } else if (username.equalsIgnoreCase("back")) {
                    return -1;
                }

                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains(username)) {
                        UIManager.animatedPrint("You got a account!\n");
                        return 1;
                    } else {
                        UIManager.animatedPrint("Sorry, that account does not exist.\n");
                        return 0;
                    }
                }
            } catch (IOException errorMessage) {
                System.err.println("Error reading file: " + errorMessage.getMessage());
            }

            // If does not have account
        } else if (userInput.equalsIgnoreCase("No")) {
            // Writes the username given to the 'Username.txt' file.
            // OBS can only store 1 username with currect implimentation
            UIManager.animatedPrint("Love meating new people. What's your name?\n");
            UIManager.animatedPrint("Username: ");

            String username = UIManager.exitCheck(scanner.nextLine());
            if (username == null) {
                return 0;
            } else if (username.equalsIgnoreCase("back")) {
                return -1;
            }

            try (BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter("src\\main\\java\\edu\\ntnu\\idi\\idatt\\Username.txt"))) {
                bufferedWriter.write(username);
                UIManager.animatedPrint("Saved successfully!");
                return -1;

            } catch (IOException errorMessage) {
                System.err.println("An error occurred while writing to the file: " + errorMessage.getMessage());
            }
        }
        return 0;
    }
}
