package edu.ntnu.idi.idatt.Pages;

import java.util.Scanner;

import edu.ntnu.idi.idatt.Manager.UIManager;

public class HomePage {
    public static int home(Scanner scanner) {
        UIManager.animatedPrint("You logged inn!");
        System.exit(0);
        return 0;
    }

}
