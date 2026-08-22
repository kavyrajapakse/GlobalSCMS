package lk.fujilanka.scm.web.resource;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lk.fujilanka.scm.core.entity.CustomsFiling;
import lk.fujilanka.scm.ejb.local.CustomsFilingServiceLocal;

import java.util.List;

@Path("/customs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@DeclareRoles({"ADMIN", "COORDINATOR", "WAREHOUSE_MANAGER", "CUSTOMS_AGENT", "VENDOR_REP"})
@RolesAllowed({"ADMIN", "CUSTOMS_AGENT"})
public class CustomsResource {

    @EJB
    private CustomsFilingServiceLocal customsService;

    @GET
    public Response getAllFilings() {
        List<CustomsFiling> filings = customsService.getAllFilings();
        return Response.ok(filings).build();
    }

    @POST
    public Response fileCustomsDeclaration(@QueryParam("shipmentId") Long shipmentId,
                                           @QueryParam("details") String details,
                                           @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "customs";
        CustomsFiling filing = customsService.createFiling(shipmentId, details != null ? details : "Standard manifest filing", username);
        return Response.status(Response.Status.CREATED).entity(filing).build();
    }

    @PUT
    @Path("/{id}/status")
    public Response updateFilingStatus(@PathParam("id") Long id,
                                      @QueryParam("status") String status,
                                      @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "customs";
        CustomsFiling updated = customsService.updateFilingStatus(id, status, username);
        return Response.ok(updated).build();
    }
}
