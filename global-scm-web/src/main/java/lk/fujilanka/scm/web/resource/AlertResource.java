package lk.fujilanka.scm.web.resource;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.ejb.local.AuditLogServiceLocal;

import java.util.ArrayList;
import java.util.List;

@Path("/alerts")
@Produces(MediaType.APPLICATION_JSON)
public class AlertResource {

    @EJB
    private AuditLogServiceLocal auditLogService;

    @GET
    public Response getRecentAlerts(@QueryParam("limit") @DefaultValue("10") int limit) {
        try {
            List<AuditLog> logs = auditLogService.getRecentAuditLogs(limit);
            return Response.ok(logs).build();
        } catch (Exception e) {
            System.err.println("AlertResource error: " + e.getMessage());
            return Response.ok(new ArrayList<>()).build();
        }
    }
}
