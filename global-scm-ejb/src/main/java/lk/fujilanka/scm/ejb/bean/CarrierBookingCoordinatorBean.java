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
@Interceptors(AuditLoggingInterceptor.class)
@TransactionManagement(TransactionManagementType.BEAN)
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

            // Business Constraint: If cost exceeds budget threshold ($50,000), ROLLBACK programmatically!
            if (costUSD > 50000.0) {
                System.err.println("BMT Booking Rejected: Cost $" + costUSD + " exceeds container budget threshold.");
                userTransaction.rollback();
                return false;
            }

            shipment.setStatus("BOOKED_WITH_" + carrierCode.toUpperCase());
            shipment.setCostUSD(costUSD);
            em.merge(shipment);

            AuditLog audit = new AuditLog("CARRIER_BOOKING_BMT", null, "Booked shipment " + shipment.getTrackingNumber() + " with carrier " + carrierCode + " for $" + costUSD);
            em.persist(audit);

            // Programmatically COMMIT JTA Transaction (BMT)
            userTransaction.commit();
            return true;

        } catch (Exception e) {
            try {
                userTransaction.rollback();
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        }
    }
}