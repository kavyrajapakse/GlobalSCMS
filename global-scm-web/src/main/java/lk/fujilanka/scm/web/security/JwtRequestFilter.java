package lk.fujilanka.scm.web.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import lk.fujilanka.scm.core.util.JwtUtil;

import java.io.IOException;
import java.security.Principal;
import java.util.Set;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        if (path.contains("auth/login") || path.contains("auth/register")) {
            return;
        }

        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                final String username = JwtUtil.getUsername(jwt);
                final Set<String> roles = JwtUtil.getRoles(jwt);

                requestContext.setSecurityContext(new SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        return () -> username;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        if (role == null || roles == null) return false;
                        return roles.stream().anyMatch(r -> r.equalsIgnoreCase(role) || r.equalsIgnoreCase("ROLE_" + role));
                    }

                    @Override
                    public boolean isSecure() {
                        return requestContext.getSecurityContext().isSecure();
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return "BEARER";
                    }
                });
            }
        }
    }
}
