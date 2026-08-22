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
import lk.fujilanka.scm.ejb.local.ShipmentServiceLocal;

import java.util.List;

@Path("/shipments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@DeclareRoles({"ADMIN", "COORDINATOR", "WAREHOUSE_MANAGER", "CUSTOMS_AGENT", "VENDOR_REP"})
@RolesAllowed({"ADMIN", "COORDINATOR"})
public class ShipmentResource {

    @EJB
    private ShipmentServiceLocal shipmentService;

    @GET
    public Response getAllShipments() {
        List<Shipment> shipments = shipmentService.getAllShipments();
        return Response.ok(shipments).build();
    }

    @GET
    @Path("/{id}")
    public Response getShipmentById(@PathParam("id") Long id) {
        Shipment shipment = shipmentService.findById(id);
        if (shipment == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(shipment).build();
    }

    @POST
    public Response createShipment(Shipment shipment, @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "coordinator";
        Shipment created = shipmentService.createShipment(shipment, username);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}/status")
    public Response updateStatus(@PathParam("id") Long id, @QueryParam("status") String status, @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "coordinator";
        Shipment updated = shipmentService.updateShipmentStatus(id, status, username);
        return Response.ok(updated).build();
    }
}