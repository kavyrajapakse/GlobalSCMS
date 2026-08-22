package lk.fujilanka.scm.core.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Enterprise Cryptographic Password Utility.
 * Implements salted SHA-256 password hashing and constant-time verification.
 */
public class PasswordUtil {

    private static final int SALT_BYTE_LENGTH = 16;
    private static final String HASH_DELIMITER = "$";

    /**
     * Hashes a raw plaintext password using a cryptographically random salt and SHA-256.
     * Output format: Base64(salt) + "$" + Base64(sha256(salt + rawPassword))
     */
    public static String hashPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }

        byte[] salt = new byte[SALT_BYTE_LENGTH];
        new SecureRandom().nextBytes(salt);

        byte[] hash = computeHash(salt, rawPassword);

        return Base64.getEncoder().encodeToString(salt) + HASH_DELIMITER + Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Verifies a raw plaintext password against a stored hash string.
     * Supports both modern salted hashes (salt$hash) and legacy plaintext strings for backward compatibility.
     */
    public static boolean verifyPassword(String rawPassword, String storedHash) {
        if (rawPassword == null || storedHash == null) {
            return false;
        }

        // Check if storedHash uses the salted format: salt$hash
        int delimiterIdx = storedHash.indexOf(HASH_DELIMITER);
        if (delimiterIdx > 0) {
            try {
                String saltStr = storedHash.substring(0, delimiterIdx);
                String expectedHashStr = storedHash.substring(delimiterIdx + 1);

                byte[] salt = Base64.getDecoder().decode(saltStr);
                byte[] expectedHash = Base64.getDecoder().decode(expectedHashStr);
                byte[] actualHash = computeHash(salt, rawPassword);

                return MessageDigest.isEqual(expectedHash, actualHash);
            } catch (Exception e) {
                // If decoding fails, fallback to direct comparison
                return storedHash.equals(rawPassword);
            }
        }

        // Backward compatibility for legacy demo/unmigrated plaintext passwords
        return storedHash.equals(rawPassword);
    }

    private static byte[] computeHash(byte[] salt, String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            return digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available in current JVM runtime.", e);
        }
    }
}
