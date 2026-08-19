package lk.fujilanka.scm.ejb.bean;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.CustomsFiling;
import lk.fujilanka.scm.core.entity.Shipment;
import lk.fujilanka.scm.core.entity.User;
import lk.fujilanka.scm.ejb.interceptor.AuditLoggingInterceptor;
import lk.fujilanka.scm.ejb.local.CustomsFilingServiceLocal;

import java.util.List;

@Stateless
@Interceptors(AuditLoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CustomsFilingServiceBean implements CustomsFilingServiceLocal {

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    @Override
    public CustomsFiling createFiling(Long shipmentId, String declarationDetails, String username) {
        Shipment shipment = em.find(Shipment.class, shipmentId);
        if (shipment == null) {
            throw new IllegalArgumentException("Shipment with ID " + shipmentId + " not found.");
        }

        String filingNumber = "CUST-FLG-" + Math.floor(1000 + Math.random() * 9000);
        CustomsFiling filing = new CustomsFiling(filingNumber, shipment, "CLEARANCE_REQUESTED", declarationDetails);
        em.persist(filing);

        User user = findUser(username);
        AuditLog audit = new AuditLog("CUSTOMS_DECLARATION_FILED", user, 
            "Customs declaration " + filingNumber + " filed for Shipment #" + shipment.getTrackingNumber());
        em.persist(audit);

        return filing;
    }

    @Override
    public CustomsFiling updateFilingStatus(Long filingId, String newStatus, String username) {
        CustomsFiling filing = em.find(CustomsFiling.class, filingId);
        if (filing == null) {
            throw new IllegalArgumentException("Customs filing with ID " + filingId + " not found.");
        }

        filing.setStatus(newStatus);
        CustomsFiling updated = em.merge(filing);

        Shipment shipment = filing.getShipment();
        if (shipment != null) {
            if ("APPROVED".equalsIgnoreCase(newStatus)) {
                shipment.setStatus("DELIVERED");
                em.merge(shipment);
            } else if ("REJECTED".equalsIgnoreCase(newStatus) || "CUSTOMS_HOLD".equalsIgnoreCase(newStatus)) {
                shipment.setStatus("CUSTOMS_HOLD");
                em.merge(shipment);
            }
        }

        User user = findUser(username);
        AuditLog audit = new AuditLog("PORT_CLEARANCE_" + newStatus.toUpperCase(), user, 
            "Customs declaration " + filing.getFilingNumber() + " updated to " + newStatus + " for Shipment #" + (shipment != null ? shipment.getTrackingNumber() : "N/A"));
        em.persist(audit);

        return updated;
    }

    @Override
    public List<CustomsFiling> getAllFilings() {
        return em.createQuery("SELECT f FROM CustomsFiling f ORDER BY f.id DESC", CustomsFiling.class).getResultList();
    }

    @Override
    public CustomsFiling findById(Long id) {
        return em.find(CustomsFiling.class, id);
    }

    private User findUser(String username) {
        if (username == null) return null;
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
