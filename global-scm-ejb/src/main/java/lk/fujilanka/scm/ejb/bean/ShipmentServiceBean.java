package lk.fujilanka.scm.ejb.bean;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.InventoryItem;
import lk.fujilanka.scm.core.entity.Shipment;
import lk.fujilanka.scm.core.entity.User;
import lk.fujilanka.scm.ejb.interceptor.binding.ExecutionPerformanceAudit;
import lk.fujilanka.scm.ejb.interceptor.binding.ScmAuditLog;
import lk.fujilanka.scm.ejb.local.ShipmentServiceLocal;
import lk.fujilanka.scm.ejb.remote.ShipmentServiceRemote;

import java.util.List;

@Stateless
@ScmAuditLog
@ExecutionPerformanceAudit
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ShipmentServiceBean implements ShipmentServiceLocal, ShipmentServiceRemote {

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    @Override
    public Shipment createShipment(Shipment shipment, String username) {
        User creator = null;
        if (username != null) {
            try {
                creator = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                        .setParameter("username", username)
                        .getSingleResult();
            } catch (NoResultException e) {}
        }

        shipment.setCreatedBy(creator);
        em.persist(shipment);

        // Audit Log Entry (Automatic CMT persistence)
        AuditLog audit = new AuditLog("CREATE_SHIPMENT", creator, "Created shipment: " + shipment.getTrackingNumber());
        em.persist(audit);

        // Multi-SKU Cargo Allocation: Deduct stock for all matched items in cargo description
        if (shipment.getCargoDescription() != null && !shipment.getCargoDescription().isBlank()) {
            try {
                List<InventoryItem> items = em.createQuery("SELECT i FROM InventoryItem i", InventoryItem.class).getResultList();
                String descLower = shipment.getCargoDescription().toLowerCase();

                for (InventoryItem item : items) {
                    if (descLower.contains(item.getSku().toLowerCase()) || descLower.contains(item.getName().toLowerCase())) {
                        
                        int deductQty = 20; // Default batch quantity
                        try {
                            String regex = "(\\d+)x\\s*" + java.util.regex.Pattern.quote(item.getSku().toLowerCase());
                            java.util.regex.Matcher m = java.util.regex.Pattern.compile(regex).matcher(descLower);
                            if (m.find()) {
                                deductQty = Integer.parseInt(m.group(1));
                            }
                        } catch (Exception ignore) {}

                        int newQty = Math.max(0, item.getQuantity() - deductQty);
                        item.setQuantity(newQty);
                        em.merge(item);

                        String userStr = (username != null && !username.isBlank()) ? username : "coordinator";
                        AuditLog stockAudit = new AuditLog("STOCK_DISPATCH_SHIPMENT", userStr, 
                            "Automated Cargo Stock Allocation: Deducted " + deductQty + " units of " + item.getName() + " (" + item.getSku() + ") for Shipment #" + shipment.getTrackingNumber());
                        em.persist(stockAudit);
                    }
                }
            } catch (Exception ex) {
                System.err.println("Inventory stock allocation check error: " + ex.getMessage());
            }
        }

        return shipment;
    }

    @Override
    public Shipment updateShipmentStatus(Long id, String status, String username) {
        Shipment shipment = em.find(Shipment.class, id);
        if (shipment == null) {
            throw new IllegalArgumentException("Shipment with ID " + id + " not found.");
        }

        shipment.setStatus(status);
        Shipment updated = em.merge(shipment);

        User user = null;
        if (username != null) {
            try {
                user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                        .setParameter("username", username)
                        .getSingleResult();
            } catch (NoResultException e) {}
        }

        AuditLog audit = new AuditLog("UPDATE_SHIPMENT_STATUS", user, "Updated shipment " + shipment.getTrackingNumber() + " status to " + status);
        em.persist(audit);

        return updated;
    }

    @Override
    public List<Shipment> getAllShipments() {
        return em.createNamedQuery("Shipment.findAll", Shipment.class).getResultList();
    }

    @Override
    public Shipment findById(Long id) {
        return em.find(Shipment.class, id);
    }
}