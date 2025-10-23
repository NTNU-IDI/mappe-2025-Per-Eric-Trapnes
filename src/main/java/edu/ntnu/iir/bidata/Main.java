package edu.ntnu.iir.bidata;

import java.util.Scanner;

import edu.ntnu.iir.bidata.Pages.LoginPage;
import edu.ntnu.iir.bidata.Pages.WelcomePage;

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
                default -> 0;

            };

        }

    }
}
