package edu.ntnu.iir.bidata.Pages;

import java.util.Scanner;

import edu.ntnu.iir.bidata.Manager.UIManager;

public class HomePage {
    public static int home(Scanner scanner) {
        UIManager.animatedPrint("You logged inn!");
        System.exit(0);
        return 0;
    }

}
