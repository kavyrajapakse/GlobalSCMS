package lk.fujilanka.scm.web.resource;

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
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "vendor_rep";
        Vendor created = vendorService.createVendor(vendor, username);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateVendor(@PathParam("id") Long id, Vendor vendor, @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "vendor_rep";
        vendor.setId(id);
        Vendor updated = vendorService.updateVendor(vendor, username);
        return Response.ok(updated).build();
    }
}
