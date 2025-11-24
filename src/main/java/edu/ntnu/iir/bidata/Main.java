package edu.ntnu.iir.bidata;

import edu.ntnu.iir.bidata.pages.AuthPage;
import edu.ntnu.iir.bidata.pages.WelcomePage;
import java.util.Scanner;

/**
 * The Main class is the entry point of the digital diary application.
 * It controls the navigation between different pages based on user interaction.
 */
public class Main {

  /**
   * Starts the application and manages page transitions.
   * Initializes the scanner and loops through the available pages.
   */
  public static void main() {
    Scanner scanner = new Scanner(System.in);
    int currentPage = 0;

    while (true) {
      currentPage += switch (currentPage) {
        case 0 -> WelcomePage.page();
        case 1 -> AuthPage.authenticate(scanner);
        default -> 0;
      };
    }
  }
}