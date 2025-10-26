package edu.ntnu.iir.bidata.Manager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class EncryptionManager {

    // Encrypts a plaintext string using a keyword. Returns Base64(ciphertext).
    public static String encrypt(String plaintext, String keyword) throws Exception {
        byte[] plainBytes = plaintext.getBytes(StandardCharsets.UTF_8);

        // Build keystream from keyword
        byte[] keystream = buildKeystream(keyword, plainBytes.length);

        // XOR plaintext with keystream
        byte[] cipherBytes = xor(plainBytes, keystream);

        // Encode to Base64 for safe storage/printing
        return Base64.getEncoder().encodeToString(cipherBytes);
    }

    // Decrypts a Base64(ciphertext) string using the same keyword.

    public static String decrypt(String encodedCipher, String keyword) throws Exception {
        byte[] cipherBytes = Base64.getDecoder().decode(encodedCipher);

        // Build the same keystream
        byte[] keystream = buildKeystream(keyword, cipherBytes.length);

        // XOR again to get back the original plaintext
        byte[] plainBytes = xor(cipherBytes, keystream);

        return new String(plainBytes, StandardCharsets.UTF_8);
    }

    // Builds a keystream of the given length from the keyword using repeated
    // SHA-256 hashing.
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

    // XOR two byte arrays of equal length.
    private static byte[] xor(byte[] a, byte[] b) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ b[i]);
        }
        return out;
    }

    // Concatenate two byte arrays.
    private static byte[] concat(byte[] a, byte[] b) {
        byte[] out = new byte[a.length + b.length];
        System.arraycopy(a, 0, out, 0, a.length);
        System.arraycopy(b, 0, out, a.length, b.length);
        return out;
    }
}