package lk.fujilanka.scm.ejb.timer;

import jakarta.annotation.Resource;
import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.InventoryItem;

import java.util.List;

@Stateless
public class InventoryTimerBean {

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    @Resource
    private SessionContext sessionContext;

    // Declarative EJB Timer: Automatically runs every 5 minutes to monitor inventory stock
    @Schedule(minute = "*/5", hour = "*", persistent = false)
    public void executeAutomaticStockCheck() {
        System.out.println("EJB Timer [Declarative]: Executing automated inventory stock scan...");

        try {
            List<InventoryItem> lowStockItems = em.createNamedQuery("InventoryItem.findLowStock", InventoryItem.class)
                    .getResultList();

            for (InventoryItem item : lowStockItems) {
                System.out.println("EJB Timer Alert: Low stock detected for SKU: " + item.getSku() + " (Qty: " + item.getQuantity() + ", Min: " + item.getMinThreshold() + ")");

                // Persist EJB Timer Audit Log
                AuditLog audit = new AuditLog("EJB_TIMER_STOCK_SCAN", null, "Low stock alert for SKU " + item.getSku() + ". Current Qty: " + item.getQuantity());
                em.persist(audit);
            }
        } catch (Exception e) {
            System.err.println("EJB Timer execution error: " + e.getMessage());
        }
    }

    // Programmatic EJB Timer: Creates a one-off retry timer after specified delay
    public void scheduleCarrierRetryTimer(Long shipmentId, long delayMillis) {
        TimerService timerService = sessionContext.getTimerService();
        timerService.createTimer(delayMillis, shipmentId);
        System.out.println("EJB Timer [Programmatic]: Scheduled carrier retry timer for shipment ID " + shipmentId + " in " + (delayMillis / 1000) + " seconds.");
    }

    @Timeout
    public void handleProgrammaticTimerTimeout(Timer timer) {
        Long shipmentId = (Long) timer.getInfo();
        System.out.println("EJB Timer [Programmatic Timeout]: Executing carrier retry for shipment ID " + shipmentId);
        AuditLog audit = new AuditLog("EJB_TIMER_CARRIER_RETRY", null, "Executed programmatic carrier retry timeout for shipment ID " + shipmentId);
        em.persist(audit);
    }
}