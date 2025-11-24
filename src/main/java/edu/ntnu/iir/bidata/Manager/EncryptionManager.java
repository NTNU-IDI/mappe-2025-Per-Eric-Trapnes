package edu.ntnu.iir.bidata.manager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * Provides encryption and decryption utilities for the digital diary application.
 * Implements a simple stream cipher based on XOR with a keystream derived
 * from repeated SHA-256 hashing of a keyword.
 *
 * <p>All ciphertext is encoded in Base64 for safe storage and transmission.
 * This class is used to protect diary metadata and content from being stored
 * in plaintext.
 *
 * @author Per Eric
 */
public class EncryptionManager {

  /**
   * Encrypts a plaintext string using the provided keyword.
   * The plaintext is XORed with a keystream derived from the keyword,
   * and the result is encoded in Base64.
   *
   * @param plaintext the text to encrypt
   * @param keyword   the keyword used to generate the keystream
   * @return the Base64-encoded ciphertext
   * @throws Exception if an error occurs during encryption
   */
  public static String encrypt(String plaintext, String keyword) throws Exception {
    byte[] plainBytes = plaintext.getBytes(StandardCharsets.UTF_8);

    // Build keystream from keyword
    byte[] keystream = buildKeystream(keyword, plainBytes.length);

    // XOR plaintext with keystream
    byte[] cipherBytes = xor(plainBytes, keystream);

    // Encode to Base64 for safe storage/printing
    return Base64.getEncoder().encodeToString(cipherBytes);
  }

  /**
   * Decrypts a Base64-encoded ciphertext string using the provided keyword.
   * The ciphertext is decoded, XORed with the same keystream, and converted
   * back to the original plaintext.
   *
   * @param encodedCipher the Base64-encoded ciphertext
   * @param keyword       the keyword used to generate the keystream
   * @return the decrypted plaintext
   * @throws Exception if an error occurs during decryption
   */
  public static String decrypt(String encodedCipher, String keyword) throws Exception {
    byte[] cipherBytes = Base64.getDecoder().decode(encodedCipher);

    // Build the same keystream
    byte[] keystream = buildKeystream(keyword, cipherBytes.length);

    // XOR again to get back the original plaintext
    byte[] plainBytes = xor(cipherBytes, keystream);

    return new String(plainBytes, StandardCharsets.UTF_8);
  }

  /**
   * Builds a keystream of the given length from the keyword using repeated
   * SHA-256 hashing. The keyword is hashed repeatedly to generate blocks
   * of bytes until the required length is reached.
   *
   * @param keyword the keyword used to generate the keystream
   * @param length  the required length of the keystream
   * @return the generated keystream
   * @throws Exception if an error occurs during hashing
   */
  private static byte[] buildKeystream(String keyword, int length) throws Exception {
    MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

    byte[] seed = keyword.getBytes(StandardCharsets.UTF_8);
    byte[] stream = new byte[length];
    byte[] block = seed;

    int pos = 0;
    while (pos < length) {
      block = sha256.digest(block); // 32 new bytes
      int take = Math.min(block.length, length - pos);
      System.arraycopy(block, 0, stream, pos, take);
      pos += take;

      // Feed the block back in to keep generating more bytes
      block = concat(block, seed);
    }
    return stream;
  }

  /**
   * XORs two byte arrays of equal length.
   *
   * @param a the first byte array
   * @param b the second byte array
   * @return the result of XORing the two arrays
   */
  private static byte[] xor(byte[] a, byte[] b) {
    byte[] out = new byte[a.length];
    for (int i = 0; i < a.length; i++) {
      out[i] = (byte) (a[i] ^ b[i]);
    }
    return out;
  }

  /**
   * Concatenates two byte arrays.
   *
   * @param a the first byte array
   * @param b the second byte array
   * @return the concatenated array
   */
  private static byte[] concat(byte[] a, byte[] b) {
    byte[] out = new byte[a.length + b.length];
    System.arraycopy(a, 0, out, 0, a.length);
    System.arraycopy(b, 0, out, a.length, b.length);
    return out;
  }
}