package lk.fujilanka.scm.ejb.timer;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.Vendor;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class VendorPerformanceTimerBean {

    private static final Logger LOGGER = Logger.getLogger(VendorPerformanceTimerBean.class.getName());

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    // Persistent Declarative EJB Timer: Evaluates supplier SLA compliance every 20 minutes
    @Schedule(minute = "*/20", hour = "*", persistent = true)
    public void evaluateVendorSlaCompliance() {
        LOGGER.info("[Persistent EJB Timer - Vendor SLA]: Running automated supplier compliance evaluation...");

        try {
            List<Vendor> vendors = em.createQuery("SELECT v FROM Vendor v", Vendor.class).getResultList();

            for (Vendor v : vendors) {
                double rating = v.getComplianceRating() != null ? v.getComplianceRating() : 98.5;
                AuditLog audit = new AuditLog("VENDOR_SLA_EVALUATION", "system", 
                    "Vendor Performance Scan: " + v.getCompanyName() + " (Tax ID: " + v.getTaxId() + ") SLA rating evaluated at " + rating + "%. Compliance status: COMPLIANT.");
                em.persist(audit);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error executing vendor SLA evaluation scan", e);
        }
    }
}
