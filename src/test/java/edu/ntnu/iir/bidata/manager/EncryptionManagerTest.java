package edu.ntnu.iir.bidata.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class EncryptionManagerTest {
  @Test
  void testEncryptAndDecryptConsistency() throws Exception {
    String keyword = "secretKey123";
    String original = "Hello World";

    String encrypted = EncryptionManager.encrypt(original, keyword);
    assertNotNull(encrypted);
    assertNotEquals(original, encrypted); // kryptert tekst skal ikke være lik original

    String decrypted = EncryptionManager.decrypt(encrypted, keyword);
    assertEquals(original, decrypted); // dekryptering skal gi tilbake original
  }

  @Test
  void testDecryptWithWrongKeyFails() throws Exception {
    String keyword = "secretKey123";
    String wrongKey = "wrongKey456";
    String original = "Sensitive Data";

    String encrypted = EncryptionManager.encrypt(original, keyword);
    String decrypted = EncryptionManager.decrypt(encrypted, wrongKey);

    // Dekryptering med feil nøkkel skal ikke gi original
    assertNotEquals(original, decrypted);
  }

  @Test
  void testEncryptEmptyString() throws Exception {
    String keyword = "secretKey123";
    String encrypted = EncryptionManager.encrypt("", keyword);
    assertNotNull(encrypted);

    String decrypted = EncryptionManager.decrypt(encrypted, keyword);
    assertEquals("", decrypted);
  }

  @Test
  void testEncryptSpecialCharacters() throws Exception {
    String keyword = "secretKey123";
    String original = "ÆØÅ!@#¤%&/()=?";

    String encrypted = EncryptionManager.encrypt(original, keyword);
    String decrypted = EncryptionManager.decrypt(encrypted, keyword);

    assertEquals(original, decrypted);
  }

  @Test
  void testEncryptLongText() throws Exception {
    String keyword = "longKey987";
    String original = "This is a longer text that should still be"
            + " encrypted and decrypted correctly.";

    String encrypted = EncryptionManager.encrypt(original, keyword);
    String decrypted = EncryptionManager.decrypt(encrypted, keyword);

    assertEquals(original, decrypted);
  }
}