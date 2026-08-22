package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;

@Local
public interface EmailNotificationServiceLocal {
    boolean sendOnboardingEmail(String recipientEmail, String username, String tempPassword);
    boolean sendOnboardingEmail(String recipientEmail, String username, String tempPassword, String roleName);
}
