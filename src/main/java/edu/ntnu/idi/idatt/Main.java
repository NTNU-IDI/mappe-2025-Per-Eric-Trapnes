package edu.ntnu.idi.idatt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    // Checks if the string is 'exit' and closes the program, or returns the command
    // if its not
    public static String exitCheck(String command) {
        if (command.equalsIgnoreCase("exit")) {
            System.out.println("Exiting...");
            System.exit(0);
        }
        return command;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // --- Start of program ---
            System.out.println("Welcome to your digital diary!");
            System.out.println("At any point you can type 'exit' to close the program");
            System.out.print("Have I seen you before? (Do you have an account. Yes/No): ");
            String userInput = exitCheck(scanner.nextLine());

            // If has account
            if (userInput.equalsIgnoreCase("Yes")) {
                // Checks the String the user gives and compares with file
                System.out.println("Welcome back!\n");
                try (BufferedReader bufferedReader = new BufferedReader(
                        new FileReader("src\\main\\java\\edu\\ntnu\\idi\\idatt\\Username.txt"))) {
                    String line;
                    System.out.print("Username: ");
                    String username = exitCheck(scanner.nextLine());

                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.contains(username)) {
                            System.out.println("You got a account!");
                        } else {
                            System.out.println("Sorry, that account does not exist.");
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

                try (BufferedWriter bufferedWriter = new BufferedWriter(
                        new FileWriter("src\\main\\java\\edu\\ntnu\\idi\\idatt\\Username.txt"))) {
                    bufferedWriter.write(username);
                    System.out.println("Saved successfully!");
                } catch (IOException errorMessage) {
                    System.err.println("An error occurred while writing to the file: " + errorMessage.getMessage());
                }
            }
        }

    }
}
