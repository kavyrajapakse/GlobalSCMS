package lk.fujilanka.scm.ejb.timer;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.CustomsFiling;

import java.util.List;

@Stateless
public class CustomsClearanceTimerBean {

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    // Declarative EJB Timer: Scans uninspected customs declarations every 15 minutes
    @Schedule(minute = "*/15", hour = "*", persistent = false)
    public void executeCustomsDeadlineScan() {
        System.out.println("[EJB Timer - Customs Clearance]: Monitoring port clearance declarations...");

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
            System.err.println("CustomsClearanceTimerBean error: " + e.getMessage());
        }
    }
}
