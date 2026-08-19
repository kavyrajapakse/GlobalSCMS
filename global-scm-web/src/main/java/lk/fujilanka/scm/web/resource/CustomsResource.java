package lk.fujilanka.scm.web.resource;

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
public class CustomsResource {

    @EJB
    private CustomsFilingServiceLocal customsService;

    @GET
    public Response getAllFilings() {
        List<CustomsFiling> filings = customsService.getAllFilings();
        return Response.ok(filings).build();
    }

    @POST
    public Response createFiling(@QueryParam("shipmentId") Long shipmentId, @QueryParam("details") String details, @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "custom";
        CustomsFiling created = customsService.createFiling(shipmentId, details != null ? details : "Standard Port Customs Declaration", username);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}/status")
    public Response updateStatus(@PathParam("id") Long id, @QueryParam("status") String status, @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "custom";
        CustomsFiling updated = customsService.updateFilingStatus(id, status, username);
        return Response.ok(updated).build();
    }
}
