package edu.ntnu.iir.bidata.Pages;

import edu.ntnu.iir.bidata.Manager.UIManager;

public class WelcomePage {
    public static int page() {

        String message = String.join("",
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\nWelcome to your digital diary!\n",
                "At any point you can type 'exit' ",
                "to close the program.\nYou can also ",
                "go back a page by simply typing 'back'.\n\n");

        UIManager.animatedPrint(message);
        return 1;
    }

}
