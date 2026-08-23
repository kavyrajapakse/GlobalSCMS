package lk.fujilanka.scm.web.resource;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.fujilanka.scm.ejb.local.GlobalSCMConfigLocal;

import java.util.Map;

@Path("/config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@DeclareRoles({"ADMIN", "COORDINATOR", "WAREHOUSE_MANAGER", "CUSTOMS_AGENT", "VENDOR_REP"})
public class ConfigResource {

    @EJB
    private GlobalSCMConfigLocal configBean;

    @GET
    @RolesAllowed({"ADMIN", "COORDINATOR", "WAREHOUSE_MANAGER", "CUSTOMS_AGENT", "VENDOR_REP"})
    public Response getSystemConfiguration() {
        Map<String, Object> config = configBean.getSystemConfigSnapshot();
        return Response.ok(config).build();
    }

    @PUT
    @Path("/threshold")
    @RolesAllowed({"ADMIN"})
    public Response updateBudgetThreshold(@QueryParam("amount") double amount) {
        if (amount <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Threshold amount must be positive.").build();
        }
        configBean.setMaxCarrierBookingThresholdLkr(amount);
        return Response.ok(Map.of("message", "Carrier budget threshold updated successfully", "newThreshold", amount)).build();
    }

    @PUT
    @Path("/port-status")
    @RolesAllowed({"ADMIN", "CUSTOMS_AGENT"})
    public Response updatePortStatus(@QueryParam("port") String portCode, @QueryParam("status") String status) {
        if (portCode == null || status == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Port code and status are required.").build();
        }
        configBean.updatePortStatus(portCode.toUpperCase(), status.toUpperCase());
        return Response.ok(Map.of("message", "Port status updated successfully", "port", portCode, "status", status)).build();
    }
}
