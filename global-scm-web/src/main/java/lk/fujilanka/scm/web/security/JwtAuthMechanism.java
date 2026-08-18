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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class JwtAuthMechanism implements HttpAuthenticationMechanism {

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        String path = request.getRequestURI();
        if (path.contains("/api/auth/login")) {
            return httpMessageContext.doNothing();
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();
                List<String> rolesList = jwt.getClaim("roles").asList(String.class);
                Set<String> roles = rolesList != null ? new HashSet<>(rolesList) : Set.of();

                return httpMessageContext.notifyContainerAboutLogin(username, roles);
            }
        }

        if (httpMessageContext.isProtected()) {
            return httpMessageContext.responseUnauthorized();
        }

        return httpMessageContext.doNothing();
    }
}