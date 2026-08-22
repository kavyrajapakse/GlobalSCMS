package lk.fujilanka.scm.ejb.timer;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.CustomsFiling;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class CustomsClearanceTimerBean {

    private static final Logger LOGGER = Logger.getLogger(CustomsClearanceTimerBean.class.getName());

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    // Persistent Declarative EJB Timer: Demonstrates persistent timer support for Payara clustered environments
    @Schedule(minute = "*/15", hour = "*", persistent = true)
    public void executeCustomsDeadlineScan() {
        LOGGER.info("[Persistent EJB Timer - Customs Clearance]: Monitoring port clearance declarations...");

        try {
            List<CustomsFiling> pendingFilings = em.createQuery(
                "SELECT f FROM CustomsFiling f WHERE f.status = 'CLEARANCE_REQUESTED'", CustomsFiling.class)
                .getResultList();

            for (CustomsFiling f : pendingFilings) {
                AuditLog audit = new AuditLog("PORT_TIMER_INSPECTION_SCAN", "system", 
                    "Port Inspection Telemetry: Customs filing " + f.getFilingNumber() + " queued for inspection at Hambantota Terminal.");
                em.persist(audit);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "CustomsClearanceTimerBean execution error", e);
        }
    }
}
