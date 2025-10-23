package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.Pages.WelcomePage;
import edu.ntnu.idi.idatt.Pages.LoginPage;
import edu.ntnu.idi.idatt.Pages.HomePage;

import java.util.Scanner;

public class Main {

    // --- Start of program ---
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int currentPage = 0;

        while (true) {
            currentPage += switch (currentPage) {
                case -1 -> currentPage * -1;
                case 0 -> WelcomePage.page();
                case 1 -> LoginPage.login(scanner);
                case 2 -> HomePage.home(scanner);
                default -> 0;

            };

        }

    }
}
