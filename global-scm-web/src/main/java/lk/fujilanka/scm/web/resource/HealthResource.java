package lk.fujilanka.scm.web.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.fujilanka.scm.ejb.local.*;

import java.util.HashMap;
import java.util.Map;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class HealthResource {

    @EJB
    private HealthServiceLocal healthService;

    @EJB
    private ShipmentServiceLocal shipmentService;

    @EJB
    private InventoryServiceLocal inventoryService;

    @EJB
    private CustomsFilingServiceLocal customsService;

    @EJB
    private VendorServiceLocal vendorService;

    @EJB
    private UserServiceLocal userService;

    @GET
    @Path("/health")
    public Response checkHealth() {
        Map<String, Object> health = healthService.getHealthStatus();
        return Response.ok(health).build();
    }

    @GET
    @Path("/metrics")
    public Response getSystemMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        try {
            int activeShipments = shipmentService.getAllShipments().size();
            int inventorySkus = inventoryService.getAllItems().size();
            int customsFilings = customsService.getAllFilings().size();
            int registeredVendors = vendorService.getAllVendors().size();
            int registeredUsers = userService.getAllUsers().size();

            metrics.put("activeShipments", activeShipments);
            metrics.put("inventorySkus", inventorySkus);
            metrics.put("customsFilings", customsFilings);
            metrics.put("registeredVendors", registeredVendors);
            metrics.put("registeredUsers", registeredUsers);

            long totalMemory = Runtime.getRuntime().totalMemory();
            long freeMemory = Runtime.getRuntime().freeMemory();
            metrics.put("memoryUsedMb", (totalMemory - freeMemory) / (1024 * 1024));
            metrics.put("memoryMaxMb", Runtime.getRuntime().maxMemory() / (1024 * 1024));

            metrics.put("configuredEjbTimers", 5);
            metrics.put("persistentTimersActive", 5);
            metrics.put("timerBeans", "InventoryTimerBean, FreightTrackingTimerBean, CustomsClearanceTimerBean, VendorPerformanceTimerBean, ContainerRouteTimerBean");
        } catch (Exception e) {
            metrics.put("metricsError", e.getMessage());
        }

        return Response.ok(metrics).build();
    }
}
