package lk.fujilanka.scm.web.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.fujilanka.scm.core.dto.LoginRequest;
import lk.fujilanka.scm.core.dto.AuthResponse;
import lk.fujilanka.scm.core.util.JwtUtil;
import lk.fujilanka.scm.core.security.SCMCallbackHandler;
import lk.fujilanka.scm.core.security.SCMUserPrincipal;
import lk.fujilanka.scm.core.security.SCMRolePrincipal;

import jakarta.ejb.EJB;
import lk.fujilanka.scm.core.dto.RegisterRequest;
import lk.fujilanka.scm.core.entity.User;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @EJB
    private lk.fujilanka.scm.ejb.local.UserServiceLocal userService;

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username and password are required.")
                    .build();
        }

        User user = userService.authenticate(request.getUsername(), request.getPassword());

        if (user != null) {
            Set<String> roles = userService.getUserRoles(user.getUsername());
            String token = JwtUtil.generateToken(user.getUsername(), roles);
            return Response.ok(new AuthResponse(token, user.getUsername(), roles)).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid username or password.")
                    .build();
        }
    }

    @POST
    @Path("/register")
    public Response register(RegisterRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username, password, and roles are required")
                    .build();
        }

        try {
            Set<String> roles = request.getRoles() != null && !request.getRoles().isEmpty() 
                                ? request.getRoles() 
                                : Set.of("COORDINATOR");

            User user = userService.registerUser(request.getUsername(), request.getPassword(), roles);
            String token = JwtUtil.generateToken(user.getUsername(), roles);

            return Response.status(Response.Status.CREATED)
                    .entity(new AuthResponse(token, user.getUsername(), roles))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Registration failed: " + e.getMessage())
                    .build();
        }
    }
}