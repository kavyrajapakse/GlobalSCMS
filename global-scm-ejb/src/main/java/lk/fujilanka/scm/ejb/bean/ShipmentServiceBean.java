package lk.fujilanka.scm.ejb.bean;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.Shipment;
import lk.fujilanka.scm.core.entity.User;
import lk.fujilanka.scm.ejb.interceptor.AuditLoggingInterceptor;
import lk.fujilanka.scm.ejb.local.ShipmentServiceLocal;

import java.util.List;

@Stateless
@Interceptors(AuditLoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ShipmentServiceBean implements ShipmentServiceLocal {

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