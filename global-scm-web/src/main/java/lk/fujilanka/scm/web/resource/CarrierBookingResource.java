package lk.fujilanka.scm.web.resource;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lk.fujilanka.scm.ejb.local.CarrierBookingCoordinatorLocal;

@Path("/carrier-booking")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CarrierBookingResource {

    @EJB
    private CarrierBookingCoordinatorLocal carrierBookingCoordinator;

    @POST
    public Response processBooking(@QueryParam("shipmentId") Long shipmentId,
                                   @QueryParam("carrierCode") String carrierCode,
                                   @QueryParam("costUSD") double costUSD,
                                   @Context SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "coordinator";
        boolean success = carrierBookingCoordinator.processCarrierBooking(shipmentId, carrierCode, costUSD, username);

        if (success) {
            return Response.ok("Container booking processed successfully via JTA BMT Transaction.").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Carrier booking rejected by BMT Transaction Manager. Cost exceeds budget threshold (LKR 15,000,000 / $50,000).")
                    .build();
        }
    }
}