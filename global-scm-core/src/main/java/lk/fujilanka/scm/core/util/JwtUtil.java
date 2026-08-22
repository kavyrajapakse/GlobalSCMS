package lk.fujilanka.scm.core.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class JwtUtil {
    private static final String SECRET = System.getenv().getOrDefault("SCM_JWT_SECRET", "87878787877SCMSecretKeyForHMAC256SignatureVerification");
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    private static final long EXPIRATION_SECONDS = 3600; // 1 hour token lifetime

    private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM).build();

    public static String generateToken(String username, Set<String> roles) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(username)
                .withClaim("roles", List.copyOf(roles))
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(EXPIRATION_SECONDS)))
                .sign(ALGORITHM);
    }

    public static DecodedJWT parseToken(String token) {
        return VERIFIER.verify(token);
    }

    public static DecodedJWT validateToken(String token) {
        try {
            return parseToken(token);
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public static boolean isValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public static String getUsername(String token) {
        return parseToken(token).getSubject();
    }

    public static String getUsername(DecodedJWT jwt) {
        return jwt != null ? jwt.getSubject() : null;
    }

    public static Set<String> getRoles(String token) {
        List<String> roles = parseToken(token).getClaim("roles").asList(String.class);
        return roles != null ? Set.copyOf(roles) : Set.of();
    }

    public static Set<String> getRoles(DecodedJWT jwt) {
        if (jwt == null) return Set.of();
        List<String> roles = jwt.getClaim("roles").asList(String.class);
        return roles != null ? Set.copyOf(roles) : Set.of();
    }
}
