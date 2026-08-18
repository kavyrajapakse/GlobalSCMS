package lk.fujilanka.scm.web.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.fujilanka.scm.core.util.JwtUtil;

import java.util.Set;

@ApplicationScoped
public class JwtAuthMechanism implements HttpAuthenticationMechanism {

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        String path = request.getRequestURI();
        if (path.contains("/api/auth/login") || path.contains("/api/auth/register")) {
            return httpMessageContext.doNothing();
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();
                Set<String> roles = JwtUtil.getRoles(jwt);

                if (roles.isEmpty() && username != null) {
                    String u = username.toLowerCase();
                    if (u.contains("admin")) roles = Set.of("ADMIN", "COORDINATOR");
                    else if (u.contains("coordinator")) roles = Set.of("COORDINATOR");
                    else if (u.contains("custom")) roles = Set.of("CUSTOMS_AGENT");
                    else if (u.contains("warehouse")) roles = Set.of("WAREHOUSE_MANAGER");
                    else if (u.contains("vendor")) roles = Set.of("VENDOR_REP");
                }

                return httpMessageContext.notifyContainerAboutLogin(username, roles);
            }
        }

        return httpMessageContext.doNothing();
    }
}