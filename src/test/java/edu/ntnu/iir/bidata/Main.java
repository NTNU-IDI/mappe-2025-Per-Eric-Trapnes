package edu.ntnu.iir.bidata;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to your digital diary!");
            System.out.println("At any point you can type 'exit' to close the program");
            System.out.print("Have I seen you before? (Do you have an account. Y/N): ");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("Y")) {
                System.out.println("\n Welcome back!");
            } else if (userInput.equalsIgnoreCase("exit")) {
                System.out.println("\n Make an account!");
            }
            if (userInput.equalsIgnoreCase("exit")) {
                break;
            }
        }
        scanner.close();
        System.out.println("Exiting...");
    }
}
