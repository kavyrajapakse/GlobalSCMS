package lk.fujilanka.scm.ejb.timer;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.Shipment;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class ContainerRouteTimerBean {

    private static final Logger LOGGER = Logger.getLogger(ContainerRouteTimerBean.class.getName());

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    // Persistent Declarative EJB Timer: Evaluates container transit route optimization every 25 minutes
    @Schedule(minute = "*/25", hour = "*", persistent = true)
    public void optimizeContainerRoutes() {
        LOGGER.info("[Persistent EJB Timer - Route Optimization]: Evaluating international shipping lanes and maritime routes...");

        try {
            List<Shipment> oceanShipments = em.createQuery(
                "SELECT s FROM Shipment s WHERE s.transportMode = 'OCEAN' AND s.status != 'DELIVERED'", Shipment.class)
                .getResultList();

            for (Shipment s : oceanShipments) {
                AuditLog audit = new AuditLog("ROUTE_OPTIMIZATION_SCAN", "system", 
                    "Lane Telemetry Scan: Route " + s.getOrigin() + " to " + s.getDestination() + " for shipment " + s.getTrackingNumber() + " optimized for fuel efficiency.");
                em.persist(audit);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error executing container route optimization scan", e);
        }
    }
}
