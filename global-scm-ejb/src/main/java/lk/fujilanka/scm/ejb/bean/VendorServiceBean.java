package lk.fujilanka.scm.ejb.bean;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.Vendor;
import lk.fujilanka.scm.ejb.interceptor.binding.ExecutionPerformanceAudit;
import lk.fujilanka.scm.ejb.interceptor.binding.ScmAuditLog;
import lk.fujilanka.scm.ejb.interceptor.binding.VendorDataValidation;
import lk.fujilanka.scm.ejb.local.VendorServiceLocal;

import java.util.List;

@Stateless
@VendorDataValidation
@ScmAuditLog
@ExecutionPerformanceAudit
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class VendorServiceBean implements VendorServiceLocal {

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    @Override
    public Vendor createVendor(Vendor vendor, String username) {
        em.persist(vendor);
        AuditLog audit = new AuditLog("CREATE_VENDOR", username, "Registered new supplier partner: " + vendor.getCompanyName() + " (Tax ID: " + vendor.getTaxId() + ")");
        em.persist(audit);
        return vendor;
    }

    @Override
    public List<Vendor> getAllVendors() {
        return em.createQuery("SELECT v FROM Vendor v ORDER BY v.id DESC", Vendor.class).getResultList();
    }

    @Override
    public Vendor findById(Long id) {
        return em.find(Vendor.class, id);
    }

    @Override
    public Vendor updateVendor(Vendor vendor, String username) {
        Vendor updated = em.merge(vendor);
        AuditLog audit = new AuditLog("UPDATE_VENDOR", username, "Updated supplier partner details for: " + vendor.getCompanyName());
        em.persist(audit);
        return updated;
    }
}
