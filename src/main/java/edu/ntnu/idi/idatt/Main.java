package edu.ntnu.idi.idatt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    // Handels user input, checks for keyword 'exit'
    public static String exitCheck(String command) {
        if (command.equalsIgnoreCase("exit")) {
            System.out.println("Exiting...");
            System.exit(0);
        }
        return command;
    }

    public static int startScreen(Scanner scanner) {
        System.out.println("Welcome to your digital diary!");
        System.out.println("At any point you can type 'exit' to close the program");

        return 1;
    }

    public static int loginScreen(Scanner scanner) {
        System.out.print("Have I seen you before? (Do you have an account. Yes/No): ");
        String userInput = exitCheck(scanner.nextLine());

        if (userInput.equalsIgnoreCase("back")) {
            return -1;
        }

        // If has account
        if (userInput.equalsIgnoreCase("Yes")) {
            // Checks the String the user gives and compares with file
            System.out.println("Welcome back!\n");
            try (BufferedReader bufferedReader = new BufferedReader(
                    new FileReader("src\\main\\java\\edu\\ntnu\\idi\\idatt\\Username.txt"))) {
                String line;
                System.out.print("Username: ");

                String username = exitCheck(scanner.nextLine());
                if (userInput.equalsIgnoreCase("back")) {
                    return -1;
                }

                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains(username)) {
                        System.out.println("You got a account!");
                        return 1;
                    } else {
                        System.out.println("Sorry, that account does not exist.");
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
            System.out.println("Make an account\n");
            System.out.print("Username: ");

            String username = exitCheck(scanner.nextLine());
            if (username.equalsIgnoreCase("back")) {
                return -1;
            }

            try (BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter("src\\main\\java\\edu\\ntnu\\idi\\idatt\\Username.txt"))) {
                bufferedWriter.write(username);
                System.out.println("Saved successfully!");
                return -1;

            } catch (IOException errorMessage) {
                System.err.println("An error occurred while writing to the file: " + errorMessage.getMessage());
            }
        }
        return 0;
    }

    // --- Start of program ---
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int screenNumber = 0;

        while (true) {
            switch (screenNumber) {

                case -1:
                    screenNumber = 0;
                    break;
                case 0:
                    screenNumber += startScreen(scanner);
                    break;
                case 1:
                    screenNumber += loginScreen(scanner);
                    break;
                case 2:
                    System.out.println("You logged inn!");
                    System.exit(0);
                    break;
                default:
                    break;

            }

        }

    }
}
