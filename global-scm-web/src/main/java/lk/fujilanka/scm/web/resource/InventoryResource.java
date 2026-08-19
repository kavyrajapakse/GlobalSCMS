package lk.fujilanka.scm.web.resource;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lk.fujilanka.scm.core.entity.InventoryItem;
import lk.fujilanka.scm.ejb.local.InventoryServiceLocal;

import java.util.List;

@Path("/inventory")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InventoryResource {

    @EJB
    private InventoryServiceLocal inventoryService;

    @GET
    public Response getAllItems() {
        List<InventoryItem> items = inventoryService.getAllItems();
        return Response.ok(items).build();
    }

    @POST
    public Response createItem(InventoryItem item, @Context SecurityContext sc) {
        if (item.getQuantity() == null) item.setQuantity(100);
        if (item.getReorderThreshold() == null) item.setReorderThreshold(30);
        InventoryItem created = inventoryService.createItem(item);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @POST
    @Path("/{id}/adjust")
    public Response adjustStock(@PathParam("id") Long id, @QueryParam("delta") int delta, @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "warehouse";
        InventoryItem updated = inventoryService.adjustStock(id, delta, username);
        return Response.ok(updated).build();
    }
}