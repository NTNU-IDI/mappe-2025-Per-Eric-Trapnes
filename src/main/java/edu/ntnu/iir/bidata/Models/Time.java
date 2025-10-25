package edu.ntnu.iir.bidata.Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Time {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEEE,MMMM dd HH:mm");

    public static String now() {

        return LocalDateTime.now().format(FORMATTER);
    }

}
