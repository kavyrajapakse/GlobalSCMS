package lk.fujilanka.scm.web.resource;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
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
@DeclareRoles({"ADMIN", "COORDINATOR", "WAREHOUSE_MANAGER", "CUSTOMS_AGENT", "VENDOR_REP"})
@RolesAllowed({"ADMIN", "WAREHOUSE_MANAGER"})
public class InventoryResource {

    @EJB
    private InventoryServiceLocal inventoryService;

    @GET
    public Response getAllItems() {
        List<InventoryItem> items = inventoryService.getAllItems();
        return Response.ok(items).build();
    }

    @GET
    @Path("/low-stock")
    public Response getLowStockItems() {
        List<InventoryItem> items = inventoryService.getLowStockItems();
        return Response.ok(items).build();
    }

    @POST
    public Response createItem(InventoryItem item) {
        InventoryItem created = inventoryService.createItem(item);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}/quantity")
    public Response adjustStock(@PathParam("id") Long id, @QueryParam("quantity") int quantityDelta, @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "warehouse";
        InventoryItem updated = inventoryService.adjustStock(id, quantityDelta, username);
        return Response.ok(updated).build();
    }
}