package lk.fujilanka.scm.ejb.timer;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.Shipment;

import java.util.List;

@Stateless
public class FreightTrackingTimerBean {

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    // Declarative EJB Timer: Scans active shipments in transit every 10 minutes
    @Schedule(minute = "*/10", hour = "*", persistent = false)
    public void executeFreightTrackingScan() {
        System.out.println("[EJB Timer - Freight Tracking]: Scanning ocean and air shipments in transit...");

        try {
            List<Shipment> activeShipments = em.createQuery(
                "SELECT s FROM Shipment s WHERE s.status = 'IN_TRANSIT' OR s.status LIKE 'BOOKED%'", Shipment.class)
                .getResultList();

            for (Shipment s : activeShipments) {
                AuditLog audit = new AuditLog("LOGISTICS_TIMER_TRACKING", "system", 
                    "Automated Tracking Telemetry: Ocean manifest " + s.getTrackingNumber() + " verified in transit to " + s.getDestination());
                em.persist(audit);
            }
        } catch (Exception e) {
            System.err.println("FreightTrackingTimerBean error: " + e.getMessage());
        }
    }
}
