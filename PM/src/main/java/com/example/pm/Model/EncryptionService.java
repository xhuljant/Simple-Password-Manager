package com.example.pm.Model;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Locale;

public class EncryptionService {
    private static final int SALT_LENGTH = 16; // 128 bits
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public String encrypt(String data, String password) throws Exception {
        byte[] salt = generateSalt();
        SecretKey key = generateKey(password, salt);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Combine salt, IV, and encrypted data
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + salt.length + iv.length + encrypted.length);
        byteBuffer.putInt(salt.length);
        byteBuffer.put(salt);
        byteBuffer.put(iv);
        byteBuffer.put(encrypted);

        return Base64.getEncoder().encodeToString(byteBuffer.array());
    }

    public String decrypt(String encryptedData, String password) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);

        int saltLength = byteBuffer.getInt();
        byte[] salt = new byte[saltLength];
        byteBuffer.get(salt);

        byte[] iv = new byte[12];
        byteBuffer.get(iv);

        byte[] encrypted = new byte[byteBuffer.remaining()];
        byteBuffer.get(encrypted);

        SecretKey key = generateKey(password, salt);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private SecretKey generateKey(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return new SecretKeySpec(skf.generateSecret(spec).getEncoded(), "AES");
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public String generateFileName(String username) {
        username=username.toLowerCase(Locale.ROOT);
        try {
            // Use a fixed salt for consistency
            String fixedSalt = "FixedSaltForFileNames";  // You can change this to any fixed string

            // Combine username and fixed salt
            String combinedInput = username + fixedSalt;

            // Hash the combined input
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(combinedInput.getBytes(StandardCharsets.UTF_8));

            // Encode the hash using Base64 and remove any non-alphanumeric characters
            String encodedHash = Base64.getUrlEncoder().withoutPadding().encodeToString(hash)
                    .replaceAll("[^a-zA-Z0-9]", "");

            // Truncate to a reasonable length and add file extension
            return encodedHash.substring(0, Math.min(encodedHash.length(), 32)) + ".db";
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }


    public String changePassword(String encryptedData, String currentPassword, String newPassword) throws Exception {
        // First, decrypt the data using the current password
        String decryptedData = decrypt(encryptedData, currentPassword);

        // Then, encrypt the data using the new password
        return encrypt(decryptedData, newPassword);
    }
}