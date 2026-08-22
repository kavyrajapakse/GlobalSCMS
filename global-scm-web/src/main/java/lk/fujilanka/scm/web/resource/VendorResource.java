package lk.fujilanka.scm.web.resource;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lk.fujilanka.scm.core.entity.Vendor;
import lk.fujilanka.scm.ejb.local.VendorServiceLocal;

import java.util.List;

@Path("/vendors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@DeclareRoles({"ADMIN", "COORDINATOR", "WAREHOUSE_MANAGER", "CUSTOMS_AGENT", "VENDOR_REP"})
@RolesAllowed({"ADMIN", "VENDOR_REP", "COORDINATOR"})
public class VendorResource {

    @EJB
    private VendorServiceLocal vendorService;

    @GET
    public Response getAllVendors() {
        List<Vendor> vendors = vendorService.getAllVendors();
        return Response.ok(vendors).build();
    }

    @GET
    @Path("/{id}")
    public Response getVendorById(@PathParam("id") Long id) {
        Vendor vendor = vendorService.findById(id);
        if (vendor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(vendor).build();
    }

    @POST
    public Response createVendor(Vendor vendor, @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "vendor";
        Vendor created = vendorService.createVendor(vendor, username);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}/rating")
    public Response updateSlaRating(@PathParam("id") Long id, @QueryParam("rating") Double rating, @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "vendor";
        Vendor vendor = vendorService.findById(id);
        if (vendor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (rating != null) {
            vendor.setComplianceRating(rating);
        }
        Vendor updated = vendorService.updateVendor(vendor, username);
        return Response.ok(updated).build();
    }
}
