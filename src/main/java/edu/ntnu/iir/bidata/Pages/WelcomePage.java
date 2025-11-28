package edu.ntnu.iir.bidata.pages;

import edu.ntnu.iir.bidata.manager.InterfaceManager;
import java.util.Scanner;

/**
 * Displays the welcome page of the digital diary application.
 * Introduces the user to basic navigation and usage instructions.
 *
 * @author Per Eric
 */
public class WelcomePage {

  /**
   * Renders the welcome message with animated output.
   * Clears the screen, prints introductory instructions, and returns a navigation code.
   *
   * @param scanner  the scanner used for user input
   *
   * @return 1 if the page was displayed successfully and should proceed to the next page
   */
  public static int page(Scanner scanner) {
    InterfaceManager.clearScreen(scanner);
    String message = String.join("",
        "\n\nWelcome to your digital diary!\n",
        "At any point you can type 'exit' ",
        "to close the program.\nYou can also ",
        "go back a page by simply typing 'back'.\n\n");

    InterfaceManager.animatedPrint(message);
    return 1;
  }
}