package lk.fujilanka.scm.ejb.stateless;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.InventoryItem;
import lk.fujilanka.scm.core.exception.InsufficientStockException;
import lk.fujilanka.scm.core.exception.ResourceNotFoundException;
import lk.fujilanka.scm.ejb.interceptor.binding.ExecutionPerformanceAudit;
import lk.fujilanka.scm.ejb.interceptor.binding.ScmAuditLog;
import lk.fujilanka.scm.ejb.local.InventoryServiceLocal;

import java.util.List;

@Stateless
@ScmAuditLog
@ExecutionPerformanceAudit
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class InventoryServiceBean implements InventoryServiceLocal {

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    @Override
    public InventoryItem createItem(InventoryItem item) {
        em.persist(item);
        AuditLog audit = new AuditLog("CREATE_INVENTORY_ITEM", "warehouse", "Created SKU: " + item.getSku());
        em.persist(audit);
        return item;
    }

    @Override
    public InventoryItem adjustStock(Long itemId, int quantityDelta, String username) {
        InventoryItem item = em.find(InventoryItem.class, itemId);
        if (item == null) {
            throw new ResourceNotFoundException("Inventory item with ID " + itemId + " not found.");
        }

        int newQty = item.getQuantity() + quantityDelta;
        if (newQty < 0) {
            throw new InsufficientStockException("Stock quantity cannot drop below zero. Requested deduction: " + Math.abs(quantityDelta) + ", Current Available: " + item.getQuantity());
        }

        item.setQuantity(newQty);
        InventoryItem updated = em.merge(item);

        String userStr = (username != null && !username.isBlank()) ? username : "warehouse";
        AuditLog audit = new AuditLog("STOCK_ADJUSTMENT", userStr, "Adjusted SKU " + item.getSku() + " quantity by " + quantityDelta + ". New Qty: " + newQty);
        em.persist(audit);

        return updated;
    }

    @Override
    public List<InventoryItem> getAllItems() {
        return em.createQuery("SELECT i FROM InventoryItem i ORDER BY i.id DESC", InventoryItem.class).getResultList();
    }

    @Override
    public List<InventoryItem> getLowStockItems() {
        return em.createNamedQuery("InventoryItem.findLowStock", InventoryItem.class).getResultList();
    }
}
