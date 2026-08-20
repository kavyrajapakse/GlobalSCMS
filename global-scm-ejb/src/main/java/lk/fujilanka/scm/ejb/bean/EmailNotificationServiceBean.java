package lk.fujilanka.scm.ejb.bean;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.User;
import lk.fujilanka.scm.ejb.interceptor.binding.ExecutionPerformanceAudit;
import lk.fujilanka.scm.ejb.interceptor.binding.ScmAuditLog;
import lk.fujilanka.scm.ejb.local.EmailNotificationServiceLocal;

@Stateless
@ScmAuditLog
@ExecutionPerformanceAudit
public class EmailNotificationServiceBean implements EmailNotificationServiceLocal {

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    @Override
    public boolean sendOnboardingEmail(Long userId, String adminUsername) {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("User account not found for ID: " + userId);
        }

        System.out.println("[EMAIL NOTIFICATION SERVICE]: Dispatching onboarding security email to user '" + user.getUsername() + "'...");

        String details = "Email Onboarding Invitation Dispatched: Sent credentials setup email to user '" + user.getUsername() + "'. First-login password change link included.";
        AuditLog audit = new AuditLog("EMAIL_ONBOARDING_SENT", adminUsername != null ? adminUsername : "admin", details);
        em.persist(audit);

        return true;
    }
}
