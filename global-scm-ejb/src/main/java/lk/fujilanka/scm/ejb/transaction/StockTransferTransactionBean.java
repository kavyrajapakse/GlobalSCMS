package lk.fujilanka.scm.ejb.transaction;

import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.UserTransaction;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.InventoryItem;
import lk.fujilanka.scm.core.exception.InsufficientStockException;
import lk.fujilanka.scm.core.exception.ResourceNotFoundException;
import lk.fujilanka.scm.ejb.interceptor.binding.ExecutionPerformanceAudit;
import lk.fujilanka.scm.ejb.interceptor.binding.ScmAuditLog;
import lk.fujilanka.scm.ejb.local.StockTransferTransactionLocal;

/**
 * BMT Transaction Coordinator for Inter-Depot Warehouse Stock Transfers.
 * Programmatically manages JTA UserTransaction begin(), commit(), and rollback().
 */
@Stateless
@ScmAuditLog
@ExecutionPerformanceAudit
@TransactionManagement(TransactionManagementType.BEAN)
public class StockTransferTransactionBean implements StockTransferTransactionLocal {

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    @Resource
    private SessionContext sessionContext;

    @Override
    public boolean executeStockTransfer(Long sourceItemId, Long targetItemId, int transferQuantity, String username) {
        UserTransaction userTransaction = sessionContext.getUserTransaction();

        try {
            // Programmatically BEGIN JTA Transaction (BMT)
            userTransaction.begin();

            InventoryItem sourceItem = em.find(InventoryItem.class, sourceItemId);
            InventoryItem targetItem = em.find(InventoryItem.class, targetItemId);

            if (sourceItem == null || targetItem == null) {
                userTransaction.rollback();
                throw new ResourceNotFoundException("Source or Target warehouse inventory item not found for transfer.");
            }

            if (sourceItem.getQuantity() < transferQuantity) {
                userTransaction.rollback();
                throw new InsufficientStockException("Insufficient stock in source depot. Available: " + sourceItem.getQuantity() + ", Requested: " + transferQuantity);
            }

            // Deduct from source depot and credit to target depot atomically
            sourceItem.setQuantity(sourceItem.getQuantity() - transferQuantity);
            targetItem.setQuantity(targetItem.getQuantity() + transferQuantity);

            em.merge(sourceItem);
            em.merge(targetItem);

            AuditLog audit = new AuditLog("STOCK_TRANSFER", username,
                "Inter-Depot Stock Transfer: Transferred " + transferQuantity + " units from SKU " + sourceItem.getSku() + " to SKU " + targetItem.getSku());
            em.persist(audit);

            // Programmatically COMMIT JTA Transaction (BMT)
            userTransaction.commit();
            return true;

        } catch (InsufficientStockException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            try {
                if (userTransaction != null) {
                    userTransaction.rollback();
                }
            } catch (Exception ex) {
                System.err.println("Error rolling back BMT stock transfer: " + ex.getMessage());
            }
            return false;
        }
    }
}
