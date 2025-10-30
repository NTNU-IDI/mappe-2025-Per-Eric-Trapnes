package edu.ntnu.iir.bidata.Manager;

public class UIManager {
    // Handels user input, checks for keyword 'exit'
    public static String exitCheck(String command) {
        return exitCheck(command, 32);
    }

    public static String exitCheck(String command, int maxLength) {
        if (command.length() > maxLength) {
            System.out.println("ERROR: String length exceeds the maximum allowed length of " + 32);
            return "";

        }
        if (command.isEmpty()) {
            System.out.println("ERROR: Entered an empty string");
            return "";

        }
        if (command.equalsIgnoreCase("exit")) {
            animatedPrint("Exiting...");
            System.exit(0);
        }
        return command;
    }

    public static void animatedPrint(String string) {

        try {
            for (int i = 0; i < string.length(); i++) {
                System.out.print(string.charAt(i));
                Thread.sleep(20);
            }

        } catch (InterruptedException errorMessage) {
            errorMessage.printStackTrace();
        }

    }
}
