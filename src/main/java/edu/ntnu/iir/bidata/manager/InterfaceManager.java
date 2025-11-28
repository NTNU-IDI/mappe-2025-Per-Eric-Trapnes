package edu.ntnu.iir.bidata.manager;

import java.io.IOException;
import java.util.Scanner;

/**
 * Provides utility methods for user interaction in the digital diary application.
 * Includes functionality for handling exit commands, printing text with animation,
 * and clearing the console screen.
 *
 * <p>This class centralizes common interface operations to improve consistency
 * and reduce duplication across the application.
 *
 * @author Per Eric
 */
public class InterfaceManager {

  /**
   * Checks user input for the keyword "exit" and validates its length.
   * Uses a default maximum length of 32 characters.
   *
   * @param scanner the user input string
   * @return the validated command, or an empty string if invalid
   */
  public static String exitCheck(Scanner scanner) {
    return exitCheck(scanner, 32);
  }

  /**
   * Checks user input for the keyword "exit" and validates its length.
   * If the input exceeds the maximum length or is empty, an error message is printed.
   * If the input is "exit", the program terminates gracefully.
   *
   * @param scanner   the user input string
   * @param maxLength the maximum allowed length for the input
   * @return the validated command, or an empty string if invalid
   */
  public static String exitCheck(Scanner scanner, int maxLength) {
    String command = scanner.nextLine().trim();

    if (command.length() > maxLength) {
      System.out.println("ERROR: String length exceeds the maximum allowed length of " + maxLength);
      InterfaceManager.animatedPrint(
          "After reading the error message you can move on by pressing (ENTER)");
      scanner.nextLine();
      return null;
    }
    if (command.isEmpty()) {
      System.out.println("ERROR: Entered an empty string");
      InterfaceManager.animatedPrint(
          "After reading the error message you can move on by pressing (ENTER)");
      scanner.nextLine();
      return null;
    }
    if (command.equalsIgnoreCase("exit")) {
      animatedPrint("Exiting...");
      System.exit(0);
    }
    return command;
  }

  /**
   * Prints text to the console with a simple animation effect.
   * Each character is printed with a short delay to simulate typing.
   *
   * @param string the text to print
   */
  public static void animatedPrint(String string) {
    try {
      for (int i = 0; i < string.length(); i++) {
        System.out.print(string.charAt(i));
        Thread.sleep(20);
      }
    } catch (InterruptedException e) {
      ;
    }
  }

  /**
   * Clears the console screen.
   * Uses platform-specific commands for Windows and ANSI escape codes for other systems.
   */
  public static void clearScreen(Scanner scanner) {
    try {
      if (System.getProperty("os.name").contains("Windows")) {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        System.out.print("\033[H\033[2J");
        System.out.flush();
      }
    } catch (Exception error) {
      errorHandling(error, scanner);
    }
  }

  /**
   * Handles different types of exceptions by printing the stack trace
   * and showing a tailored message to the user before continuing.
   *
   * @param error   the exception to handle
   * @param scanner the scanner used to wait for user input
   */
  public static void errorHandling(Exception error, Scanner scanner) {
    error.printStackTrace();

    if (error instanceof InterruptedException) {
      InterfaceManager.animatedPrint(
            "The program was interrupted. Press ENTER to continue."
      );
    } else if (error instanceof IOException) {
      InterfaceManager.animatedPrint(
            "An input/output error occurred. Press ENTER to continue."
      );
    } else if (error instanceof RuntimeException) {
      InterfaceManager.animatedPrint(
            "A runtime error occurred. Press ENTER to continue."
      );
    } else {
      InterfaceManager.animatedPrint(
            "An unexpected error occurred. After reading the message, press ENTER to continue."
      );
    }

    scanner.nextLine();
  }
}