package lk.fujilanka.scm.web.resource;

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
public class ShipmentResource {

    @EJB
    private ShipmentServiceLocal shipmentService;

    @GET
    public Response getAllShipments() {
        List<Shipment> shipments = shipmentService.getAllShipments();
        return Response.ok(shipments).build();
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