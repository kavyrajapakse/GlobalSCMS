package lk.fujilanka.scm.web.resource;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lk.fujilanka.scm.core.entity.User;
import lk.fujilanka.scm.ejb.local.EmailNotificationServiceLocal;
import lk.fujilanka.scm.ejb.local.UserServiceLocal;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @EJB
    private UserServiceLocal userService;

    @EJB
    private EmailNotificationServiceLocal emailService;

    @GET
    public Response getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Response.ok(users).build();
    }

    @POST
    public Response registerUser(Map<String, Object> payload) {
        String username = (String) payload.get("username");
        String password = (String) payload.get("password");
        String role = (String) payload.get("role");

        if (username == null || password == null || role == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Username, password, and role are required.").build();
        }

        User created = userService.registerUser(username, password, Set.of(role));
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}/status")
    public Response toggleStatus(@PathParam("id") Long id) {
        User updated = userService.toggleUserStatus(id);
        return Response.ok(updated).build();
    }

    @PUT
    @Path("/{id}/role")
    public Response updateRole(@PathParam("id") Long id, @QueryParam("role") String role) {
        if (role == null || role.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Role name is required.").build();
        }
        User updated = userService.updateUserRole(id, role);
        return Response.ok(updated).build();
    }

    @POST
    @Path("/{id}/send-email")
    public Response sendOnboardingEmail(@PathParam("id") Long id, @Context SecurityContext sc) {
        String adminUsername = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "admin";
        boolean sent = emailService.sendOnboardingEmail(id, adminUsername);
        return Response.ok(Map.of("message", "Onboarding invitation email dispatched successfully to user.")).build();
    }

    @PUT
    @Path("/change-password")
    public Response changePassword(Map<String, String> payload) {
        String username = payload.get("username");
        String currentPassword = payload.get("currentPassword");
        String newPassword = payload.get("newPassword");

        if (username == null || currentPassword == null || newPassword == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("message", "Username, current password, and new password are required.")).build();
        }

        User updated = userService.changePassword(username, currentPassword, newPassword);
        return Response.ok(Map.of("message", "Password updated successfully for account '" + username + "'.")).build();
    }
}
