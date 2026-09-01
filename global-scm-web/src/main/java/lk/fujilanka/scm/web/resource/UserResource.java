package lk.fujilanka.scm.web.resource;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lk.fujilanka.scm.core.entity.User;
import lk.fujilanka.scm.ejb.local.EmailNotificationServiceLocal;
import lk.fujilanka.scm.ejb.local.UserServiceLocal;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@DeclareRoles({"ADMIN", "COORDINATOR", "WAREHOUSE_MANAGER", "CUSTOMS_AGENT", "VENDOR_REP"})
public class UserResource {

    @EJB
    private UserServiceLocal userService;

    @EJB
    private EmailNotificationServiceLocal emailService;

    @GET
    @RolesAllowed({"ADMIN"})
    public Response getAllUsers(@Context SecurityContext sc) {
        if (sc != null && !sc.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("error", "Forbidden: Insufficient privileges for this role")).build();
        }
        List<User> users = userService.getAllUsers();
        return Response.ok(users).build();
    }

    @POST
    @RolesAllowed({"ADMIN"})
    public Response registerUser(Map<String, Object> payload, @Context SecurityContext sc) {
        if (sc != null && !sc.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("error", "Forbidden: Insufficient privileges for this role")).build();
        }
        String username = (String) payload.get("username");
        String fullName = (String) payload.get("fullName");
        String email = (String) payload.get("email");
        String phone = (String) payload.get("phone");
        String department = (String) payload.get("department");
        String password = (String) payload.get("password");
        String role = (String) payload.get("role");

        Long vendorId = null;
        if (payload.get("vendorId") != null) {
            try {
                vendorId = Long.valueOf(payload.get("vendorId").toString());
            } catch (Exception ignored) {}
        }

        if (username == null || role == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Username and role are required.").build();
        }

        if (fullName == null || fullName.isBlank()) {
            fullName = username;
        }

        if (email == null || email.isBlank()) {
            email = username + "@gmail.com";
        }

        if (phone == null || phone.isBlank()) {
            phone = "+94 77 123 4567";
        }

        if (department == null || department.isBlank()) {
            department = "Global Supply Chain Operations";
        }

        // If no password provided by admin, generate a secure temporary password
        if (password == null || password.isBlank()) {
            int code = 1000 + new SecureRandom().nextInt(9000);
            password = "Scm#" + code + "!";
        }

        User created = userService.registerUser(username, fullName, email, phone, department, vendorId, password, Set.of(role), true);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}/status")
    @RolesAllowed({"ADMIN"})
    public Response toggleStatus(@PathParam("id") Long id) {
        User updated = userService.toggleUserStatus(id);
        return Response.ok(updated).build();
    }

    @PUT
    @Path("/{id}/role")
    @RolesAllowed({"ADMIN"})
    public Response updateRole(@PathParam("id") Long id, @QueryParam("role") String role) {
        if (role == null || role.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Role name is required.").build();
        }
        User updated = userService.updateUserRole(id, role);
        return Response.ok(updated).build();
    }

    @POST
    @Path("/{id}/send-email")
    @RolesAllowed({"ADMIN"})
    public Response sendOnboardingEmail(@PathParam("id") Long id, @Context SecurityContext sc) {
        try {
            // Automatically generate a fresh temporary password and sync directly with database
            String tempPass = userService.resetUserTemporaryPassword(id);

            List<User> users = userService.getAllUsers();
            User target = null;
            for (User u : users) {
                if (u.getId().equals(id)) {
                    target = u;
                    break;
                }
            }

            if (target == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
            }

            String roleName = "USER";
            if (target.getRoles() != null && !target.getRoles().isEmpty()) {
                roleName = target.getRoles().iterator().next().getName();
            }

            String recipientName = target.getFullName();
            boolean sent = emailService.sendOnboardingEmail(target.getEmail(), recipientName + " (" + target.getUsername() + ")", tempPass, roleName);
            if (sent) {
                return Response.ok(Map.of(
                        "message", "Onboarding email with temporary password dispatched successfully to " + target.getEmail() + " (" + recipientName + ")!",
                        "tempPassword", tempPass
                )).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(Map.of("message", "Failed to deliver email. Check server logs."))
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "Error sending onboarding email: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/change-password")
    @PermitAll
    public Response changePassword(Map<String, String> payload) {
        String username = payload.get("username");
        String currentPassword = payload.get("currentPassword");
        String newPassword = payload.get("newPassword");

        if (username == null || currentPassword == null || newPassword == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "Username, current password, and new password are required."))
                    .build();
        }

        try {
            User updated = userService.changePassword(username, currentPassword, newPassword);
            return Response.ok(Map.of("message", "Password updated successfully for staff member '" + updated.getFullName() + "' (" + username + ").")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", e.getMessage()))
                    .build();
        }
    }
}
