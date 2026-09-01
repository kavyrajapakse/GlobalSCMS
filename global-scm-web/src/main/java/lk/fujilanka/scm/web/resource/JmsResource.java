package lk.fujilanka.scm.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lk.fujilanka.scm.ejb.local.CargoEventProducerLocal;

import java.util.HashMap;
import java.util.Map;

@Path("/jms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JmsResource {

    @EJB
    private CargoEventProducerLocal jmsProducer;

    public void setJmsProducer(CargoEventProducerLocal jmsProducer) {
        this.jmsProducer = jmsProducer;
    }

    @POST
    @Path("/dispatch-event")
    @RolesAllowed({"ADMIN", "COORDINATOR", "VENDOR_REP"})
    public Response sendAsyncCargoEvent(Map<String, String> payload, @Context SecurityContext securityContext) {
        if (payload == null || !payload.containsKey("trackingNumber")) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "Invalid payload: 'trackingNumber' is required");
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }

        String trackingNumber = payload.get("trackingNumber");
        String eventType = payload.getOrDefault("eventType", "CARGO_DISPATCH_TELEMETRY");
        String details = payload.getOrDefault("details", "Maritime vessel dispatch event initiated.");
        String username = (securityContext != null && securityContext.getUserPrincipal() != null) ? 
                securityContext.getUserPrincipal().getName() : "COORDINATOR";

        boolean queued = (jmsProducer != null) && jmsProducer.sendCargoEvent(trackingNumber, eventType, details, username);

        Map<String, Object> response = new HashMap<>();
        response.put("status", queued ? "QUEUED_FOR_ASYNC_PROCESSING" : "FAILED_TO_QUEUE");
        response.put("queueDestination", "java:global/jms/CargoEventQueue");
        response.put("trackingNumber", trackingNumber);
        response.put("eventType", eventType);
        response.put("message", "JMS Cargo event received and handed over to Message-Driven Bean (MDB) background worker pool.");

        return Response.status(Response.Status.ACCEPTED).entity(response).build();
    }
}
