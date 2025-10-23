package edu.ntnu.iir.bidata.Pages;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import edu.ntnu.iir.bidata.Manager.UIManager;

public class LoginPage {
    public static int login(Scanner scanner) {
        String filePath = "src\\main\\java\\edu\\ntnu\\iir\\bidata\\Database\\Username.txt";

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
                    new FileReader(filePath))) {
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
                        System.out.println("");
                        HomePage.home(scanner, username);
                        return -1;
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
            UIManager.animatedPrint("Love meeting new people. What's your name?\n");
            UIManager.animatedPrint("Username: ");

            String username = UIManager.exitCheck(scanner.nextLine());

            if (username == null) {
                return 0;
            } else if (username.equalsIgnoreCase("back")) {
                return -1;
            }

            try (BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter(filePath))) {
                bufferedWriter.write(username);
                UIManager.animatedPrint("Saved successfully!");

                HomePage.home(scanner, username);
                return -1;

            } catch (IOException errorMessage) {
                System.err.println("An error occurred while writing to the file: " + errorMessage.getMessage());
            }
        }
        return 0;
    }
}
