package lk.fujilanka.scm.ejb.timer;

import jakarta.annotation.Resource;
import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.InventoryItem;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class InventoryTimerBean {

    private static final Logger LOGGER = Logger.getLogger(InventoryTimerBean.class.getName());

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    @Resource
    private TimerService timerService;

    // Persistent Declarative EJB Timer: Automatically scans low stock items every 30 minutes
    @Schedule(minute = "*/30", hour = "*", persistent = true)
    public void scanLowStockItemsDeclarative() {
        LOGGER.info("[Persistent Declarative EJB Timer]: Initiating periodic warehouse inventory scan...");
        performLowStockScan("DeclarativeTimer");
    }

    // Persistent Programmatic EJB Timer: Dynamically created for targeted stock monitoring (persistent = true)
    public void createProgrammaticStockCheckTimer(long durationMs, String targetSku) {
        TimerConfig config = new TimerConfig(targetSku, true);
        timerService.createSingleActionTimer(durationMs, config);
        LOGGER.info("[Persistent Programmatic EJB Timer]: Persistent programmatic timer scheduled for SKU " + targetSku + " in " + durationMs + " ms.");
    }

    @Timeout
    public void handleProgrammaticTimeout(Timer timer) {
        String sku = (String) timer.getInfo();
        LOGGER.info("[Programmatic EJB Timer - Timeout]: Executing targeted scan for SKU: " + sku);
        performLowStockScan("ProgrammaticTimer-" + sku);
    }

    private void performLowStockScan(String triggeredBy) {
        try {
            List<InventoryItem> lowStockItems = em.createQuery(
                "SELECT i FROM InventoryItem i WHERE i.quantity < i.reorderThreshold", InventoryItem.class)
                .getResultList();

            for (InventoryItem item : lowStockItems) {
                AuditLog audit = new AuditLog("INVENTORY_LOW_STOCK_ALERT", "system", 
                    "Low Stock Alert (" + triggeredBy + "): Item " + item.getName() + " (SKU: " + item.getSku() + ") is below reorder threshold. Quantity: " + item.getQuantity());
                em.persist(audit);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error executing inventory scan", e);
        }
    }
}