package lk.fujilanka.scm.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    @DisplayName("Should hash and verify password successfully with salt")
    void testHashAndVerifySuccess() {
        String rawPass = "Scm#4829!Admin";
        String hashed = PasswordUtil.hashPassword(rawPass);

        assertNotNull(hashed);
        assertTrue(hashed.contains("$"));
        assertNotEquals(rawPass, hashed);

        assertTrue(PasswordUtil.verifyPassword(rawPass, hashed));
        assertFalse(PasswordUtil.verifyPassword("WrongPassword123", hashed));
    }

    @Test
    @DisplayName("Should generate different hashes for same password due to random salt")
    void testSaltRandomness() {
        String rawPass = "SecretSupplyChainPass";
        String hash1 = PasswordUtil.hashPassword(rawPass);
        String hash2 = PasswordUtil.hashPassword(rawPass);

        assertNotEquals(hash1, hash2);
        assertTrue(PasswordUtil.verifyPassword(rawPass, hash1));
        assertTrue(PasswordUtil.verifyPassword(rawPass, hash2));
    }

    @Test
    @DisplayName("Should verify legacy plaintext password for backward compatibility")
    void testLegacyPlaintextCompatibility() {
        assertTrue(PasswordUtil.verifyPassword("admin123", "admin123"));
        assertFalse(PasswordUtil.verifyPassword("admin123", "wrongpass"));
    }
}
