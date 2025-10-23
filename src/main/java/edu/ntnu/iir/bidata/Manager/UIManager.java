package edu.ntnu.iir.bidata.Manager;

public class UIManager {
    // Handels user input, checks for keyword 'exit'
    public static String exitCheck(String command) {
        return exitCheck(command, 32);
    }

    public static String exitCheck(String command, int maxLength) {
        if (command.length() > maxLength) {
            System.out.println("ERROR: String length exceeds the maximum allowed length of " + 32);
            return null;

        }
        if (command.isEmpty()) {
            System.out.println("ERROR: Entered an empty string");
            return null;

        }
        if (command.equalsIgnoreCase("exit")) {
            animatedPrint("Exiting...");
            System.exit(0);
        }
        return command;
    }

    public static void animatedPrint(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.print(str.charAt(i));
            try {
                Thread.sleep(30);
            } catch (InterruptedException errorMessage) {
                errorMessage.printStackTrace();
            }
        }
    }
}
