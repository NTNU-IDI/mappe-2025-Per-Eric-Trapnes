package edu.ntnu.idi.idatt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

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
            System.out.println("Welcome to your digital diary!");
            System.out.println("At any point you can type 'exit' to close the program");
            System.out.print("Have I seen you before? (Do you have an account. Yes/No): ");
            String userInput = exitCheck(scanner.nextLine());
            ;

            if (userInput.equalsIgnoreCase("Yes")) {
                System.out.println("Welcome back!\n");
                try (BufferedReader br = new BufferedReader(
                        new FileReader("src\\main\\java\\edu\\ntnu\\idi\\idatt\\Username.txt"))) {
                    String line;
                    System.out.print("Username: ");
                    String username = exitCheck(scanner.nextLine());

                    while ((line = br.readLine()) != null) {
                        if (line.contains(username)) {
                            System.out.println("You got a account!");
                        } else {
                            System.out.println("Sorry, that account does not exist.");
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file: " + e.getMessage());
                }

            } else if (userInput.equalsIgnoreCase("No")) {
                System.out.println("\nMake an account");

                String filePath = "src\\main\\java\\edu\\ntnu\\idi\\idatt\\Username.txt";
                System.out.print("Username: ");
                String content = exitCheck(scanner.nextLine());

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                    writer.write(content);
                    System.out.println("Saved successfully!");
                } catch (IOException e) {
                    System.err.println("An error occurred while writing to the file: " + e.getMessage());
                }
            }
        }

    }
}
