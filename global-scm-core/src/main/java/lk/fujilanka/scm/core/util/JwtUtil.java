package lk.fujilanka.scm.core.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JwtUtil {
    private static final String SECRET = "GlobalTradeSCMSecretKey2026_EnterpriseGradeEncryption";
    private static final String ISSUER = "lk.fujilanka.scm";
    private static final long EXPIRATION_TIME = 86400000L; // 24 hours

    public static String generateToken(String username, Set<String> roles) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(username)
                .withClaim("roles", List.copyOf(roles))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    public static DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            return verifier.verify(token);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUsername(DecodedJWT jwt) {
        return jwt != null ? jwt.getSubject() : null;
    }

    public static Set<String> getRoles(DecodedJWT jwt) {
        if (jwt == null) return Set.of();
        List<String> rolesList = jwt.getClaim("roles").asList(String.class);
        return rolesList != null ? new HashSet<>(rolesList) : Set.of();
    }

    public static boolean isValid(String token) {
        return validateToken(token) != null;
    }

    public static DecodedJWT parseToken(String token) {
        return validateToken(token);
    }
}
