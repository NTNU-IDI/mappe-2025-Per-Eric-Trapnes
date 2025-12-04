package edu.ntnu.iir.bidata.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InterfaceManagerTest {
  private ByteArrayOutputStream output;
  private PrintStream originalOut;

  @BeforeEach
  void setUp() {
    originalOut = System.out;
    output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));
  }

  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
  }

  @Test
  void testExitCheckValidInput() {
    String input = "hello\n";
    Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

    String result = InterfaceManager.exitCheck(scanner, 32);

    assertEquals("hello", result);
    assertTrue(output.toString().isEmpty()); // ingen feilmelding
  }

  @Test
  void testExitCheckEmptyString() {
    String input = "\n\n"; // første tom streng, så ENTER for å gå videre
    Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

    String result = InterfaceManager.exitCheck(scanner, 32);

    assertNull(result);
    assertTrue(output.toString().contains("ERROR: Entered an empty string"));
  }

  @Test
  void testExitCheckTooLongString() {
    String longInput = "a".repeat(40) + "\n\n"; // lengre enn maxLength
    Scanner scanner = new Scanner(new ByteArrayInputStream(longInput.getBytes()));

    String result = InterfaceManager.exitCheck(scanner, 32);

    assertNull(result);
    assertTrue(output.toString().contains("ERROR: String length exceeds"));
  }

  @Test
  void testAnimatedPrint() {
    InterfaceManager.animatedPrint("Hi");
    String printed = output.toString();
    assertTrue(printed.contains("Hi"));
  }
}