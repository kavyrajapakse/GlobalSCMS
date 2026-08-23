package lk.fujilanka.scm.web.resource;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lk.fujilanka.scm.core.dto.DraftCargoItem;
import lk.fujilanka.scm.core.entity.Shipment;
import lk.fujilanka.scm.ejb.local.ShipmentDraftSessionLocal;

import java.util.HashMap;
import java.util.Map;

@Path("/draft-shipment")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@DeclareRoles({"ADMIN", "COORDINATOR", "VENDOR_REP"})
@RolesAllowed({"ADMIN", "COORDINATOR", "VENDOR_REP"})
public class ShipmentDraftResource {

    @EJB
    private ShipmentDraftSessionLocal draftSession;

    @GET
    public Response getDraftStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("originPort", draftSession.getOriginPort());
        status.put("destinationPort", draftSession.getDestinationPort());
        status.put("transportMode", draftSession.getTransportMode());
        status.put("totalItemsCount", draftSession.getTotalItemCount());
        status.put("totalEstimatedWeightKg", draftSession.getTotalEstimatedWeightKg());
        status.put("totalEstimatedCostLkr", draftSession.getTotalEstimatedCostLkr());
        status.put("items", draftSession.getDraftItems());
        return Response.ok(status).build();
    }

    @POST
    @Path("/route")
    public Response setRoute(@QueryParam("origin") String origin, @QueryParam("destination") String destination, @QueryParam("mode") String mode) {
        draftSession.setRouteDetails(origin, destination, mode);
        return Response.ok(Map.of("message", "Route updated in draft session")).build();
    }

    @POST
    @Path("/add-item")
    public Response addDraftItem(DraftCargoItem item) {
        if (item == null || item.getSku() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Valid item SKU is required.").build();
        }
        draftSession.addCargoItem(item);
        return Response.ok(Map.of("message", "Item added to conversational draft session", "itemCount", draftSession.getTotalItemCount())).build();
    }

    @DELETE
    @Path("/remove-item/{sku}")
    public Response removeDraftItem(@PathParam("sku") String sku) {
        draftSession.removeCargoItem(sku);
        return Response.ok(Map.of("message", "Item removed from draft", "sku", sku)).build();
    }

    @POST
    @Path("/clear")
    public Response clearDraft() {
        draftSession.clearDraft();
        return Response.ok(Map.of("message", "Draft cleared")).build();
    }

    @POST
    @Path("/finalize")
    public Response finalizeDraft(@QueryParam("carrier") @DefaultValue("Lanka Freight") String carrier, 
                                  @QueryParam("prefix") @DefaultValue("SCM-TRK") String prefix,
                                  @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "coordinator";
        try {
            Shipment created = draftSession.finalizeAndDispatch(carrier, prefix, username);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
