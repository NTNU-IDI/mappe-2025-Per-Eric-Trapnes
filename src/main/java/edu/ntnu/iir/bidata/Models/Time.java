package edu.ntnu.iir.bidata.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for working with time in the digital diary application.
 * Provides a method to return the current time formatted in a human-readable way.
 *
 * <p>The format used is: day of week, full month name, day, year, and time in 24-hour format.
 * Example: "Mon, November 24 2025 22:57"
 *
 * @author Per Eric
 */
public class Time {

  /** Formatter for displaying the current time in a readable format. */
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("EE, MMMM dd yyyy HH:mm");

  /**
   * Returns the current time formatted using the predefined pattern.
   *
   * @return the formatted current time as a string
   */
  public static String now() {
    return LocalDateTime.now().format(FORMATTER);
  }
}