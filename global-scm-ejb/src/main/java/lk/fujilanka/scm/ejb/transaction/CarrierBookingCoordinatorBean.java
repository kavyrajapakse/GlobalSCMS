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
import lk.fujilanka.scm.core.entity.Shipment;
import lk.fujilanka.scm.core.exception.CarrierBookingRejectedException;
import lk.fujilanka.scm.core.exception.ResourceNotFoundException;
import lk.fujilanka.scm.ejb.interceptor.binding.ExecutionPerformanceAudit;
import lk.fujilanka.scm.ejb.interceptor.binding.ScmAuditLog;
import lk.fujilanka.scm.ejb.interceptor.binding.VendorDataValidation;
import lk.fujilanka.scm.ejb.local.CarrierBookingCoordinatorLocal;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * BMT Transaction Coordinator for Logistics Ocean Freight Container Bookings.
 * Programmatically manages JTA UserTransaction begin(), commit(), and rollback().
 */
@Stateless
@VendorDataValidation
@ScmAuditLog
@ExecutionPerformanceAudit
@TransactionManagement(TransactionManagementType.BEAN)
public class CarrierBookingCoordinatorBean implements CarrierBookingCoordinatorLocal {

    private static final Logger LOGGER = Logger.getLogger(CarrierBookingCoordinatorBean.class.getName());

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    @Resource
    private SessionContext sessionContext;

    @Override
    public boolean processCarrierBooking(Long shipmentId, String carrierCode, double costLkr, String username) {
        UserTransaction userTransaction = sessionContext.getUserTransaction();

        try {
            // Programmatically BEGIN JTA Transaction (BMT)
            userTransaction.begin();

            Shipment shipment = em.find(Shipment.class, shipmentId);
            if (shipment == null) {
                userTransaction.rollback();
                throw new ResourceNotFoundException("Shipment with ID " + shipmentId + " not found for carrier booking.");
            }

            // Business Constraint: If cost exceeds container budget threshold (LKR 15,000,000), ROLLBACK programmatically!
            if (costLkr > 15000000.0) {
                LOGGER.warning("BMT Booking Rejected: Cost LKR " + costLkr + " exceeds container budget threshold.");
                userTransaction.rollback();
                throw new CarrierBookingRejectedException("BMT Booking Rejected: Container rate LKR " + costLkr + " exceeds budget threshold of LKR 15,000,000.");
            }

            // Update Status and cost while preserving cargo tracking
            shipment.setStatus("BOOKED_WITH_" + carrierCode.toUpperCase());
            shipment.setCostLkr(costLkr);
            em.merge(shipment);

            AuditLog audit = new AuditLog("CARRIER_BOOKING", username, "Booked shipment " + shipment.getTrackingNumber() + " with carrier " + carrierCode + " (Container Fee: LKR " + costLkr + ")");
            em.persist(audit);

            // Programmatically COMMIT JTA Transaction (BMT)
            userTransaction.commit();
            return true;

        } catch (CarrierBookingRejectedException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error executing BMT carrier booking transaction", e);
            try {
                if (userTransaction != null) {
                    userTransaction.rollback();
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error rolling back BMT transaction", ex);
            }
            throw new CarrierBookingRejectedException("BMT Booking Failed: " + e.getMessage());
        }
    }
}
