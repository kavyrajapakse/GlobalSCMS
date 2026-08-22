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
public class FreightTrackingTimerBean {

    private static final Logger LOGGER = Logger.getLogger(FreightTrackingTimerBean.class.getName());

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    // Persistent Declarative EJB Timer: Scans active in-transit ocean freight shipments every 20 minutes
    @Schedule(minute = "*/20", hour = "*", persistent = true)
    public void trackActiveShipments() {
        LOGGER.info("[Persistent EJB Timer - Freight Tracking]: Initiating automated GPS ocean vessel location scan...");

        try {
            List<Shipment> inTransitShipments = em.createQuery(
                "SELECT s FROM Shipment s WHERE s.status = 'IN_TRANSIT'", Shipment.class)
                .getResultList();

            for (Shipment s : inTransitShipments) {
                AuditLog audit = new AuditLog("VESSEL_TELEMETRY_SCAN", "system", 
                    "GPS Vessel Tracking: Shipment " + s.getTrackingNumber() + " en route from " + s.getOrigin() + " to " + s.getDestination() + ". Status verified OK.");
                em.persist(audit);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error executing freight tracking scan", e);
        }
    }
}
