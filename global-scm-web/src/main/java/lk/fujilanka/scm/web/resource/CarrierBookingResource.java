package lk.fujilanka.scm.web.resource;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lk.fujilanka.scm.ejb.local.CarrierBookingCoordinatorLocal;

import java.util.HashMap;
import java.util.Map;

@Path("/carrier-booking")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@DeclareRoles({"ADMIN", "COORDINATOR", "WAREHOUSE_MANAGER", "CUSTOMS_AGENT", "VENDOR_REP"})
@RolesAllowed({"ADMIN", "COORDINATOR"})
public class CarrierBookingResource {

    @EJB
    private CarrierBookingCoordinatorLocal carrierBookingCoordinator;

    @POST
    public Response processBooking(Map<String, Object> payload,
                                   @QueryParam("shipmentId") Long qShipmentId,
                                   @QueryParam("carrierCode") String qCarrierCode,
                                   @QueryParam("costLkr") Double qCostLkr,
                                   @Context SecurityContext sc) {
        return executeBooking(payload, qShipmentId, qCarrierCode, qCostLkr, sc);
    }

    @POST
    @Path("/book")
    public Response processBookingWithPath(Map<String, Object> payload,
                                           @QueryParam("shipmentId") Long qShipmentId,
                                           @QueryParam("carrierCode") String qCarrierCode,
                                           @QueryParam("costLkr") Double qCostLkr,
                                           @Context SecurityContext sc) {
        return executeBooking(payload, qShipmentId, qCarrierCode, qCostLkr, sc);
    }

    private Response executeBooking(Map<String, Object> payload, Long qShipmentId, String qCarrierCode, Double qCostLkr, SecurityContext sc) {
        String username = (sc != null && sc.getUserPrincipal() != null) ? sc.getUserPrincipal().getName() : "coordinator";
        
        Long shipmentId = qShipmentId != null ? qShipmentId : 1L;
        String carrierCode = (qCarrierCode != null && !qCarrierCode.isBlank()) ? qCarrierCode : "MAERSK";
        double costLkr = qCostLkr != null ? qCostLkr : 0.0;

        if (payload != null) {
            if (payload.get("shipmentId") != null) {
                try { shipmentId = Long.valueOf(payload.get("shipmentId").toString()); } catch (Exception ignored) {}
            }
            if (payload.get("carrierCode") != null) {
                carrierCode = payload.get("carrierCode").toString();
            } else if (payload.get("carrierName") != null) {
                carrierCode = payload.get("carrierName").toString();
            }
            if (payload.get("costLkr") != null) {
                try { costLkr = Double.parseDouble(payload.get("costLkr").toString()); } catch (Exception ignored) {}
            } else if (payload.get("bookingCostLkr") != null) {
                try { costLkr = Double.parseDouble(payload.get("bookingCostLkr").toString()); } catch (Exception ignored) {}
            }
        }

        boolean success = carrierBookingCoordinator.processCarrierBooking(shipmentId, carrierCode, costLkr, username);

        if (success) {
            Map<String, Object> res = new HashMap<>();
            res.put("status", "SUCCESS");
            res.put("message", "Container booking processed successfully via JTA BMT Transaction.");
            res.put("shipmentId", shipmentId);
            res.put("carrierCode", carrierCode);
            res.put("costLkr", costLkr);
            return Response.ok(res).build();
        } else {
            Map<String, Object> res = new HashMap<>();
            res.put("error", "Carrier booking rejected by BMT Transaction Manager. Cost exceeds budget threshold (LKR 15,000,000).");
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    }
}