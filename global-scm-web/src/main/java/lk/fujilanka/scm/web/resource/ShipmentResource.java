package lk.fujilanka.scm.web.resource;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lk.fujilanka.scm.core.entity.Shipment;
import lk.fujilanka.scm.core.entity.User;
import lk.fujilanka.scm.ejb.local.ShipmentServiceLocal;
import lk.fujilanka.scm.ejb.local.UserServiceLocal;

import java.util.List;

@Path("/shipments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@DeclareRoles({"ADMIN", "COORDINATOR", "WAREHOUSE_MANAGER", "CUSTOMS_AGENT", "VENDOR_REP"})
public class ShipmentResource {

    @EJB
    private ShipmentServiceLocal shipmentService;

    @EJB
    private UserServiceLocal userService;

    @GET
    @RolesAllowed({"ADMIN", "COORDINATOR", "VENDOR_REP"})
    public Response getAllShipments(@QueryParam("vendorId") Long vendorId, @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : null;

        // B2B Multi-Tenant Isolation: If caller is VENDOR_REP, filter strictly by their associated supplier
        if (sc != null && sc.isUserInRole("VENDOR_REP") && username != null) {
            List<User> users = userService.getAllUsers();
            for (User u : users) {
                if (u.getUsername().equalsIgnoreCase(username) && u.getVendor() != null) {
                    List<Shipment> vendorShipments = shipmentService.getShipmentsByVendor(u.getVendor().getId());
                    return Response.ok(vendorShipments).build();
                }
            }
        }

        // If vendorId is explicitly supplied
        if (vendorId != null) {
            List<Shipment> vendorShipments = shipmentService.getShipmentsByVendor(vendorId);
            return Response.ok(vendorShipments).build();
        }

        // Internal Enterprise Staff (ADMIN, COORDINATOR) see full global manifest
        List<Shipment> shipments = shipmentService.getAllShipments();
        return Response.ok(shipments).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "COORDINATOR", "VENDOR_REP"})
    public Response getShipmentById(@PathParam("id") Long id) {
        Shipment shipment = shipmentService.findById(id);
        if (shipment == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(shipment).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "COORDINATOR"})
    public Response createShipment(Shipment shipment, @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "coordinator";
        try {
            Shipment created = shipmentService.createShipment(shipment, username);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}/status")
    @RolesAllowed({"ADMIN", "COORDINATOR"})
    public Response updateStatus(@PathParam("id") Long id, @QueryParam("status") String status, @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "coordinator";
        Shipment updated = shipmentService.updateShipmentStatus(id, status, username);
        return Response.ok(updated).build();
    }
}