package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;

@Local
public interface EmailNotificationServiceLocal {
    boolean sendOnboardingEmail(Long userId, String adminUsername);
}
