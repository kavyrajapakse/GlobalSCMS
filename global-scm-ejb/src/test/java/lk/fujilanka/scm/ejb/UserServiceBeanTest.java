package lk.fujilanka.scm.ejb;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lk.fujilanka.scm.core.entity.Role;
import lk.fujilanka.scm.core.entity.User;
import lk.fujilanka.scm.core.util.PasswordUtil;
import lk.fujilanka.scm.ejb.stateless.UserServiceBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceBeanTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<User> userQuery;

    @Mock
    private TypedQuery<Role> roleQuery;

    @InjectMocks
    private UserServiceBean userService;

    @BeforeEach
    void setUp() throws Exception {
        Field emField = UserServiceBean.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(userService, em);
    }

    @Test
    @DisplayName("Should successfully authenticate user with valid credentials")
    void testAuthenticateSuccess() {
        String hashedPassword = PasswordUtil.hashPassword("admin123");
        User user = new User("admin", "Kavithma Rajapakse", "kavithma@gmail.com", "+94 77 111 2233", "Administration", hashedPassword, Set.of(new Role("ADMIN")), false);

        when(em.createQuery(contains("SELECT u FROM User u WHERE u.username = :username"), eq(User.class))).thenReturn(userQuery);
        when(userQuery.setParameter("username", "admin")).thenReturn(userQuery);
        when(userQuery.getSingleResult()).thenReturn(user);

        User authenticated = userService.authenticate("admin", "admin123");

        assertNotNull(authenticated);
        assertEquals("admin", authenticated.getUsername());
        assertNotNull(authenticated.getLastLoginAt());
    }

    @Test
    @DisplayName("Should reject authentication with invalid password")
    void testAuthenticateFailure() {
        String hashedPassword = PasswordUtil.hashPassword("admin123");
        User user = new User("admin", "Kavithma Rajapakse", "kavithma@gmail.com", "+94 77 111 2233", "Administration", hashedPassword, Set.of(new Role("ADMIN")), false);

        when(em.createQuery(contains("SELECT u FROM User u WHERE u.username = :username"), eq(User.class))).thenReturn(userQuery);
        when(userQuery.setParameter("username", "admin")).thenReturn(userQuery);
        when(userQuery.getSingleResult()).thenReturn(user);

        User authenticated = userService.authenticate("admin", "wrong_password");

        assertNull(authenticated);
    }

    @Test
    @DisplayName("Should generate secure temporary password, store cryptographic hash, and set requiresPasswordChange flag")
    void testResetUserTemporaryPassword() {
        User user = new User("coordinator01", "Nimal Perera", "nimal@gmail.com", "+94 71 222 3344", "Logistics", "oldpass", Set.of(new Role("COORDINATOR")), false);
        user.setId(6L);

        when(em.find(User.class, 6L)).thenReturn(user);

        String tempPass = userService.resetUserTemporaryPassword(6L);

        assertNotNull(tempPass);
        assertTrue(tempPass.startsWith("Scm#"));
        assertTrue(user.isRequiresPasswordChange());
        assertTrue(PasswordUtil.verifyPassword(tempPass, user.getPasswordHash()));
        verify(em, times(1)).merge(user);
    }

    @Test
    @DisplayName("Should change permanent password, store cryptographic hash, and clear requiresPasswordChange flag")
    void testChangePassword() {
        String currentHashed = PasswordUtil.hashPassword("Scm#1234!");
        User user = new User("coordinator01", "Nimal Perera", "nimal@gmail.com", "+94 71 222 3344", "Logistics", currentHashed, Set.of(new Role("COORDINATOR")), true);

        when(em.createQuery(contains("SELECT u FROM User u WHERE u.username = :username"), eq(User.class))).thenReturn(userQuery);
        when(userQuery.setParameter("username", "coordinator01")).thenReturn(userQuery);
        when(userQuery.getSingleResult()).thenReturn(user);
        when(em.merge(any(User.class))).thenReturn(user);

        User updated = userService.changePassword("coordinator01", "Scm#1234!", "NewPermanentPass@2026");

        assertNotNull(updated);
        assertTrue(PasswordUtil.verifyPassword("NewPermanentPass@2026", updated.getPasswordHash()));
        assertFalse(updated.isRequiresPasswordChange());
    }
}

