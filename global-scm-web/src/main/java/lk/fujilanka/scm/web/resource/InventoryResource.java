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
import lk.fujilanka.scm.core.entity.User;
import lk.fujilanka.scm.ejb.local.InventoryServiceLocal;
import lk.fujilanka.scm.ejb.local.UserServiceLocal;

import java.util.List;
import java.util.stream.Collectors;

@Path("/inventory")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@DeclareRoles({"ADMIN", "COORDINATOR", "WAREHOUSE_MANAGER", "CUSTOMS_AGENT", "VENDOR_REP"})
public class InventoryResource {

    @EJB
    private InventoryServiceLocal inventoryService;

    @EJB
    private UserServiceLocal userService;

    @GET
    @RolesAllowed({"ADMIN", "WAREHOUSE_MANAGER", "COORDINATOR", "VENDOR_REP"})
    public Response getAllItems(@QueryParam("vendorId") Long vendorId, @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : null;
        List<InventoryItem> allItems = inventoryService.getAllItems();

        // B2B Multi-Tenant Isolation for Vendor Representatives
        if (sc != null && sc.isUserInRole("VENDOR_REP") && username != null) {
            List<User> users = userService.getAllUsers();
            for (User u : users) {
                if (u.getUsername().equalsIgnoreCase(username) && u.getVendor() != null) {
                    Long userVendorId = u.getVendor().getId();
                    List<InventoryItem> filtered = allItems.stream()
                            .filter(i -> i.getVendor() != null && i.getVendor().getId().equals(userVendorId))
                            .collect(Collectors.toList());
                    return Response.ok(filtered).build();
                }
            }
        }

        if (vendorId != null) {
            List<InventoryItem> filtered = allItems.stream()
                    .filter(i -> i.getVendor() != null && i.getVendor().getId().equals(vendorId))
                    .collect(Collectors.toList());
            return Response.ok(filtered).build();
        }

        return Response.ok(allItems).build();
    }

    @GET
    @Path("/low-stock")
    @RolesAllowed({"ADMIN", "WAREHOUSE_MANAGER", "COORDINATOR"})
    public Response getLowStockItems() {
        List<InventoryItem> items = inventoryService.getLowStockItems();
        return Response.ok(items).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "WAREHOUSE_MANAGER"})
    public Response createItem(InventoryItem item) {
        InventoryItem created = inventoryService.createItem(item);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}/quantity")
    @RolesAllowed({"ADMIN", "WAREHOUSE_MANAGER"})
    public Response adjustStock(@PathParam("id") Long id, @QueryParam("quantity") int quantityDelta, @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "warehouse";
        InventoryItem updated = inventoryService.adjustStock(id, quantityDelta, username);
        return Response.ok(updated).build();
    }
}