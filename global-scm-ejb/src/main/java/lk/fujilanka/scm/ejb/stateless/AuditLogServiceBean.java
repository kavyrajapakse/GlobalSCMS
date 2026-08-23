package lk.fujilanka.scm.ejb.stateless;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.ejb.local.AuditLogServiceLocal;

import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AuditLogServiceBean implements AuditLogServiceLocal {

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    @Override
    public List<AuditLog> getRecentAuditLogs(int maxResults) {
        return em.createQuery("SELECT a FROM AuditLog a ORDER BY a.id DESC", AuditLog.class)
                 .setMaxResults(maxResults)
                 .getResultList();
    }
}

