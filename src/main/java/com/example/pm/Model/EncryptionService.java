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

/**
 * Service class providing encryption and decryption functionality using AES/GCM encryption.
 * Implements secure password-based encryption with salt and iteration-based key derivation.
 */
public class EncryptionService {
    private static final int SALT_LENGTH = 16; // 128 bits
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;

    /**
     * Encrypts the provided data using password-based AES/GCM encryption.
     *
     * The encryption process follows these steps:
     * 1.Generates a random 16-byte salt for key derivation
     * 2.Generates an AES-256 key from the password using PBKDF2WithHmacSHA256
     * 3.Generates a random 12-byte IV for GCM mode
     * 4.Encrypts the data using AES in GCM  with:
     *   - No padding
     *   - Authenticated encryption
     *   - 128-bit authentication tag
     * 5.Combines the encrypted components to : [4 bytes salt length][salt bytes][12 bytes IV][encrypted data + auth tag]
     * 6.Encodes the final byte array using Base64
     *
     * @param data The string data to encrypt
     * @param password The password to use for encryption
     * @return Base64 encoded string containing salt, IV, and encrypted data
     * @throws Exception If encryption fails
     */
    public String encrypt(String data, String password) throws Exception {
        byte[] salt = generateSalt();
        SecretKey key = generateKey(password, salt);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        //Generate IV
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);

        //Create cipher and encrypt data
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        //Combine salt, IV, and encrypted data
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + salt.length + iv.length + encrypted.length);
        byteBuffer.putInt(salt.length);
        byteBuffer.put(salt);
        byteBuffer.put(iv);
        byteBuffer.put(encrypted);

        //return byte array of encrypted data
        return Base64.getEncoder().encodeToString(byteBuffer.array());
    }

    /**
     * Decrypts data encrypted using the encrypt() method
     *
     * The decryption process follows these steps:
     * 1.Converts Base64 string to bytes
     * 2.Reads Salt from first 4 bytes
     * 3.Reads IV from following 12 bytes
     * 4.Reads data from remaining bytes
     * 5.Derives AES key using PBKDF2WithHmacSHA256
     * 6.Initialize cipher
     * 7.Decrypts data and converts bytes to string
     *
     * @param encryptedData data to decrypt
     * @param password used to decrypt data
     * @return string of decrypted data
     * @throws Exception if decryption fails
     */
    public String decrypt(String encryptedData, String password) throws Exception {
        //Decode string and and wrap in ByteBuffer
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);

        //Extract salt
        int saltLength = byteBuffer.getInt();
        byte[] salt = new byte[saltLength];
        byteBuffer.get(salt);

        //Extract IV
        byte[] iv = new byte[12];
        byteBuffer.get(iv);

        //Extract data
        byte[] encrypted = new byte[byteBuffer.remaining()];
        byteBuffer.get(encrypted);

        //Generate key and cipher for decryption
        SecretKey key = generateKey(password, salt);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        //Decrypt and store data in string
        byte[] decrypted = cipher.doFinal(encrypted);

        //Return decrypted data string
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * Generates a secret key from a password and salt using PBKDF2 with HMAC SHA-256.
     *
     * @param password The password to derive the key from
     * @param salt The salt to use in key derivation
     * @return A SecretKey suitable for AES encryption
     * @throws Exception If key generation fails
     */
    private SecretKey generateKey(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return new SecretKeySpec(skf.generateSecret(spec).getEncoded(), "AES");
    }

    /**
     * Generates a random salt for use in key derivation.
     *
     * @return A byte array containing the generated salt
     */
    private byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    /**
     * Generates a deterministic filename based on a username.
     * Uses SHA-256 hashing with a fixed salt to create a consistent filename.
     *
     * @param username The username to generate a filename for
     * @return A string containing the generated filename with .db extension
     * @throws RuntimeException If SHA-256 algorithm is not available
     */
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

    /**
     * Used to change masterPassword
     * Decrypts data and encrypts with newPassword
     */
    public String changePassword(String encryptedData, String currentPassword, String newPassword) throws Exception {
        // First, decrypt the data using the current password
        String decryptedData = decrypt(encryptedData, currentPassword);

        // Then, encrypt the data using the new password
        return encrypt(decryptedData, newPassword);
    }
}