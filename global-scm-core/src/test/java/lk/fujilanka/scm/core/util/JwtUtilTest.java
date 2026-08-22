package lk.fujilanka.scm.core.util;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    @DisplayName("Should generate and validate JWT token with correct claims")
    void testTokenGenerationAndValidation() {
        String username = "test_coordinator";
        Set<String> roles = Set.of("COORDINATOR", "USER");

        String token = JwtUtil.generateToken(username, roles);

        assertNotNull(token);
        assertTrue(JwtUtil.isValid(token));
        assertEquals(username, JwtUtil.getUsername(token));

        Set<String> extractedRoles = JwtUtil.getRoles(token);
        assertTrue(extractedRoles.contains("COORDINATOR"));
        assertTrue(extractedRoles.contains("USER"));
    }

    @Test
    @DisplayName("Should reject malformed or tampered JWT token")
    void testMalformedToken() {
        String malformedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.invalidpayload.invalidsignature";
        assertFalse(JwtUtil.isValid(malformedToken));
        assertNull(JwtUtil.validateToken(malformedToken));
    }

    @Test
    @DisplayName("Should parse valid JWT token and extract subject")
    void testParseToken() {
        String username = "admin_user";
        Set<String> roles = Set.of("ADMIN");

        String token = JwtUtil.generateToken(username, roles);
        DecodedJWT decoded = JwtUtil.parseToken(token);

        assertNotNull(decoded);
        assertEquals(username, decoded.getSubject());
    }
}
