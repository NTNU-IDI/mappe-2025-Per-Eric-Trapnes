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
            animatedPrint("Exiting...");
            System.exit(0);
        }
        return command;
    }

    public static int startScreen() {
        String message = String.join("",
                "\n\n\n\nWelcome to your digital diary!\n",
                "At any point you can type 'exit' ",
                "to close the program.\nYou can also ",
                "go back a page by simply typing 'back'.\n\n");

        animatedPrint(message);
        return 1;
    }

    public static void animatedPrint(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.print(str.charAt(i));
            try {
                Thread.sleep(30);
            } catch (InterruptedException errorMessage) {
                errorMessage.printStackTrace();
            }
        }
    }

    public static int loginScreen(Scanner scanner) {
        animatedPrint("Have I seen you before? (Do you have an account. Yes/No): ");
        String userInput = exitCheck(scanner.nextLine());

        if (userInput.equalsIgnoreCase("back")) {
            return -1;
        }

        // If has account
        if (userInput.equalsIgnoreCase("Yes")) {
            // Checks the String the user gives and compares with file
            animatedPrint("\n\n\nWelcome back!\n");
            try (BufferedReader bufferedReader = new BufferedReader(
                    new FileReader("src\\main\\java\\edu\\ntnu\\idi\\idatt\\Username.txt"))) {
                String line;
                animatedPrint("Username: ");

                String username = exitCheck(scanner.nextLine());
                if (userInput.equalsIgnoreCase("back")) {
                    return -1;
                }

                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains(username)) {
                        animatedPrint("You got a account!\n");
                        return 1;
                    } else {
                        animatedPrint("Sorry, that account does not exist.\n");
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
            animatedPrint("Love meating new people. What's your name?\n");
            animatedPrint("Username: ");

            String username = exitCheck(scanner.nextLine());
            if (username.equalsIgnoreCase("back")) {
                return -1;
            }

            try (BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter("src\\main\\java\\edu\\ntnu\\idi\\idatt\\Username.txt"))) {
                bufferedWriter.write(username);
                animatedPrint("Saved successfully!");
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
                    screenNumber += startScreen();
                    break;
                case 1:
                    screenNumber += loginScreen(scanner);
                    break;
                case 2:
                    animatedPrint("You logged inn!");
                    System.exit(0);
                    break;
                default:
                    break;

            }

        }

    }
}
