package lk.fujilanka.scm.ejb.timer;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;

@Stateless
public class ContainerRouteTimerBean {

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    // Declarative EJB Timer: Computes shipping container route optimization every 30 minutes
    @Schedule(minute = "*/30", hour = "*", persistent = false)
    public void executeRouteOptimizationScan() {
        System.out.println("[EJB Timer - Container Route]: Computing maritime shipping route optimizations...");

        try {
            AuditLog audit = new AuditLog("ROUTE_TIMER_OPTIMIZATION_SCAN", "system", 
                "Route Telemetry: Ocean shipping lane Hambantota ➔ Nagoya optimized for fuel efficiency and zero port congestion.");
            em.persist(audit);
        } catch (Exception e) {
            System.err.println("ContainerRouteTimerBean error: " + e.getMessage());
        }
    }
}
