package lk.fujilanka.scm.web.resource;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.fujilanka.scm.core.dto.AuthResponse;
import lk.fujilanka.scm.core.dto.LoginRequest;
import lk.fujilanka.scm.core.dto.RegisterRequest;
import lk.fujilanka.scm.core.entity.User;
import lk.fujilanka.scm.ejb.local.UserServiceLocal;
import lk.fujilanka.scm.ejb.util.JwtUtil;

import java.util.Set;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @EJB
    private UserServiceLocal userService;

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
            boolean requiresChange = user.isRequiresPasswordChange();
            return Response.ok(new AuthResponse(token, user.getUsername(), roles, requiresChange)).build();
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
                    .entity("Username and password are required.")
                    .build();
        }

        try {
            // Enterprise Security Rule: Public self-registration ALWAYS locks to low-privilege VENDOR_REP role.
            Set<String> roles = Set.of("VENDOR_REP");

            User user = userService.registerUser(request.getUsername(), request.getPassword(), roles);
            String token = JwtUtil.generateToken(user.getUsername(), roles);

            return Response.status(Response.Status.CREATED)
                    .entity(new AuthResponse(token, user.getUsername(), roles, false))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Registration failed: " + e.getMessage())
                    .build();
        }
    }
}