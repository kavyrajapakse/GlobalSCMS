package lk.fujilanka.scm.ejb.stateless;

import jakarta.ejb.AsyncResult;
import jakarta.ejb.Asynchronous;
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
import lk.fujilanka.scm.core.entity.Vendor;
import lk.fujilanka.scm.core.exception.InsufficientStockException;
import lk.fujilanka.scm.ejb.interceptor.binding.ExecutionPerformanceAudit;
import lk.fujilanka.scm.ejb.interceptor.binding.ScmAuditLog;
import lk.fujilanka.scm.ejb.local.ShipmentServiceLocal;
import lk.fujilanka.scm.ejb.remote.ShipmentServiceRemote;

import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Logger;

@Stateless
@ScmAuditLog
@ExecutionPerformanceAudit
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ShipmentServiceBean implements ShipmentServiceLocal, ShipmentServiceRemote {

    private static final Logger LOGGER = Logger.getLogger(ShipmentServiceBean.class.getName());

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

        // Auto-resolve Vendor entity if vendorName is supplied
        if (shipment.getVendor() == null && shipment.getVendorName() != null && !shipment.getVendorName().isBlank()) {
            try {
                List<Vendor> matched = em.createQuery("SELECT v FROM Vendor v WHERE LOWER(v.companyName) = LOWER(:name)", Vendor.class)
                        .setParameter("name", shipment.getVendorName().trim())
                        .getResultList();
                if (!matched.isEmpty()) {
                    shipment.setVendor(matched.get(0));
                }
            } catch (Exception ignore) {}
        }

        em.persist(shipment);

        // Audit Log Entry (Automatic CMT persistence)
        AuditLog audit = new AuditLog("CREATE_SHIPMENT", creator, "Created shipment: " + shipment.getTrackingNumber());
        em.persist(audit);

        // Multi-SKU Cargo Allocation: Deduct stock for all matched items in cargo description
        if (shipment.getCargoDescription() != null && !shipment.getCargoDescription().isBlank()) {
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

                    // Enforce atomic CMT transactional integrity
                    if (item.getQuantity() < deductQty) {
                        throw new InsufficientStockException("Cannot dispatch shipment: Insufficient stock for " + item.getName() + " (" + item.getSku() + "). Required: " + deductQty + ", Available: " + item.getQuantity());
                    }

                    int newQty = item.getQuantity() - deductQty;
                    item.setQuantity(newQty);
                    em.merge(item);

                    String userStr = (username != null && !username.isBlank()) ? username : "coordinator";
                    AuditLog stockAudit = new AuditLog("STOCK_DISPATCH_SHIPMENT", userStr, 
                        "Automated Cargo Stock Allocation: Deducted " + deductQty + " units of " + item.getName() + " (" + item.getSku() + ") for Shipment #" + shipment.getTrackingNumber());
                    em.persist(stockAudit);
                }
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
    public List<Shipment> getShipmentsByVendor(Long vendorId) {
        String companyName = "";
        try {
            Vendor v = em.find(Vendor.class, vendorId);
            if (v != null && v.getCompanyName() != null) {
                companyName = v.getCompanyName();
            }
        } catch (Exception ignore) {}

        return em.createQuery("SELECT s FROM Shipment s WHERE s.vendor.id = :vendorId OR (s.vendorName IS NOT NULL AND LOWER(s.vendorName) = LOWER(:companyName)) ORDER BY s.id DESC", Shipment.class)
                .setParameter("vendorId", vendorId)
                .setParameter("companyName", companyName)
                .getResultList();
    }

    @Override
    public Shipment findById(Long id) {
        return em.find(Shipment.class, id);
    }

    @Override
    @Asynchronous
    public Future<String> calculateOptimalCorridorAsync(String origin, String destination) {
        LOGGER.info("[ShipmentServiceBean]: Calculating optimal maritime shipping corridor asynchronously for " + origin + " -> " + destination);
        
        // Simulates heavy analytical geospatial calculation
        String corridorResult = "Optimal Maritime Corridor [" + origin + " -> " + destination + "]: Distance: 3,840 NM, Safe Route via Malacca Strait, Fuel Efficiency: 94.2%";
        
        return new AsyncResult<>(corridorResult);
    }
}
