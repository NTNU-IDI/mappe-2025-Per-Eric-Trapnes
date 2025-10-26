package edu.ntnu.iir.bidata.Manager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionManager {

    // Encrypt: returns Base64(salt || ciphertext)
    public static String encrypt(String plaintext, String keyword) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        byte[] plain = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] keystream = keystream(keyword, salt, plain.length);
        byte[] cipher = xor(plain, keystream);

        // Combine salt + cipher and encode for printing/storage
        byte[] combined = new byte[salt.length + cipher.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(cipher, 0, combined, salt.length, cipher.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    // Decrypt: expects Base64(salt || ciphertext)
    public static String decrypt(String encoded, String keyword) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encoded);

        byte[] salt = new byte[16];
        System.arraycopy(combined, 0, salt, 0, salt.length);

        byte[] cipher = new byte[combined.length - salt.length];
        System.arraycopy(combined, salt.length, cipher, 0, cipher.length);

        byte[] keystream = keystream(keyword, salt, cipher.length);
        byte[] plain = xor(cipher, keystream);

        return new String(plain, StandardCharsets.UTF_8);
    }

    // Build a keystream of 'length' bytes from keyword+salt using repeated SHA-256
    private static byte[] keystream(String keyword, byte[] salt, int length) throws Exception {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

        // Seed: keyword bytes + salt
        byte[] seed = concat(keyword.getBytes(StandardCharsets.UTF_8), salt);

        byte[] stream = new byte[length];
        byte[] block = seed;
        int pos = 0;

        while (pos < length) {
            block = sha256.digest(block); // next 32 bytes
            int take = Math.min(block.length, length - pos);
            System.arraycopy(block, 0, stream, pos, take);
            pos += take;

            // Mix in salt again to avoid repeating the same block sequence if seed is short
            block = concat(block, salt);
        }
        return stream;
    }

    private static byte[] xor(byte[] a, byte[] b) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++)
            out[i] = (byte) (a[i] ^ b[i]);
        return out;
    }

    private static byte[] concat(byte[] a, byte[] b) {
        byte[] out = new byte[a.length + b.length];
        System.arraycopy(a, 0, out, 0, a.length);
        System.arraycopy(b, 0, out, a.length, b.length);
        return out;
    }

    // Demo
    public static void main(String[] args) throws Exception {
        String keyword = "TrondheimRocks!";
        String message = "Engineering diaries are fun.";

        String enc = encrypt(message, keyword);
        System.out.println("Encrypted: " + enc);

        String dec = decrypt(enc, keyword);
        System.out.println("Decrypted: " + dec);
    }
}
