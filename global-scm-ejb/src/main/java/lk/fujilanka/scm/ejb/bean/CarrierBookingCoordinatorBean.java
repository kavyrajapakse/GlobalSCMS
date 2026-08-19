package lk.fujilanka.scm.ejb.bean;

import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.UserTransaction;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.Shipment;
import lk.fujilanka.scm.ejb.interceptor.AuditLoggingInterceptor;
import lk.fujilanka.scm.ejb.local.CarrierBookingCoordinatorLocal;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors(AuditLoggingInterceptor.class)
public class CarrierBookingCoordinatorBean implements CarrierBookingCoordinatorLocal {

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    @Resource
    private SessionContext sessionContext;

    @Override
    public boolean processCarrierBooking(Long shipmentId, String carrierCode, double costUSD, String username) {
        UserTransaction userTransaction = sessionContext.getUserTransaction();

        try {
            // Programmatically BEGIN JTA Transaction (BMT)
            userTransaction.begin();

            Shipment shipment = em.find(Shipment.class, shipmentId);
            if (shipment == null) {
                userTransaction.rollback();
                return false;
            }

            // Business Constraint: If cost exceeds budget threshold (LKR 15,000,000), ROLLBACK programmatically!
            if (costUSD > 15000000.0) {
                System.err.println("BMT Booking Rejected: Cost LKR " + costUSD + " exceeds container budget threshold.");
                userTransaction.rollback();
                return false;
            }

            // Update Status while preserving original cargo shipment cost
            shipment.setStatus("BOOKED_WITH_" + carrierCode.toUpperCase());
            em.merge(shipment);

            AuditLog audit = new AuditLog("CARRIER_BOOKING", username, "Booked shipment " + shipment.getTrackingNumber() + " with carrier " + carrierCode + " (Container Fee: LKR " + costUSD + ")");
            em.persist(audit);

            // Programmatically COMMIT JTA Transaction (BMT)
            userTransaction.commit();
            return true;

        } catch (Exception e) {
            try {
                if (userTransaction != null) {
                    userTransaction.rollback();
                }
            } catch (Exception ex) {
                System.err.println("Error rolling back BMT transaction: " + ex.getMessage());
            }
            return false;
        }
    }
}