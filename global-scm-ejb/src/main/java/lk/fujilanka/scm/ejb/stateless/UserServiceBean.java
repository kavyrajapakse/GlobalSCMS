package lk.fujilanka.scm.ejb.stateless;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;
import lk.fujilanka.scm.core.entity.Role;
import lk.fujilanka.scm.core.entity.User;
import lk.fujilanka.scm.core.entity.Vendor;
import lk.fujilanka.scm.core.util.PasswordUtil;
import lk.fujilanka.scm.ejb.interceptor.binding.ExecutionPerformanceAudit;
import lk.fujilanka.scm.ejb.interceptor.binding.ScmAuditLog;
import lk.fujilanka.scm.ejb.local.UserServiceLocal;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
@ScmAuditLog
@ExecutionPerformanceAudit
public class UserServiceBean implements UserServiceLocal {

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    @PostConstruct
    public void initDefaultUsers() {
        try {
            Long count = em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
            if (count == 0) {
                System.out.println("UserServiceBean: Database empty. Seeding default JPA roles and users...");

                getOrCreateRole("ADMIN");
                getOrCreateRole("COORDINATOR");
                getOrCreateRole("CUSTOMS_AGENT");
                getOrCreateRole("WAREHOUSE_MANAGER");
                getOrCreateRole("VENDOR_REP");

                registerUser("admin", "Kavithma Rajapakse", "kavithmarajapakse03@gmail.com", "+94 77 111 2233", "Executive Administration", null, "admin123", Set.of("ADMIN", "COORDINATOR"), false);
                registerUser("coordinator", "Nimal Perera", "kavithmarajapakse03@gmail.com", "+94 71 222 3344", "Ocean Freight & Logistics", null, "pass123", Set.of("COORDINATOR"), false);
                registerUser("custom", "Sunil Jayawardena", "kavithmarajapakse03@gmail.com", "+94 76 333 4455", "Port Customs Compliance", null, "pass123", Set.of("CUSTOMS_AGENT"), false);
                registerUser("warehouse", "Ruwan Fernando", "kavithmarajapakse03@gmail.com", "+94 70 444 5566", "Depot & Stock Management", null, "pass123", Set.of("WAREHOUSE_MANAGER"), false);
                registerUser("vendor", "Fuji Lanka Supplier Rep", "kavithmarajapakse03@gmail.com", "+94 77 555 6677", "Supplier Operations", 1L, "pass123", Set.of("VENDOR_REP"), false);

                System.out.println("UserServiceBean: JPA Default users seeded successfully in MySQL.");
            }
        } catch (Exception e) {
            System.err.println("UserServiceBean seeding warning: " + e.getMessage());
        }
    }

    private Role getOrCreateRole(String roleName) {
        try {
            return em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                     .setParameter("name", roleName)
                     .getSingleResult();
        } catch (NoResultException e) {
            Role role = new Role(roleName);
            em.persist(role);
            return role;
        }
    }

    @Override
    public User registerUser(String username, String rawPassword, Set<String> roleNames) {
        return registerUser(username, username, username + "@gmail.com", "+94 77 123 4567", "Global Logistics", null, rawPassword, roleNames, true);
    }

    @Override
    public User registerUser(String username, String email, String rawPassword, Set<String> roleNames) {
        return registerUser(username, username, email, "+94 77 123 4567", "Global Logistics", null, rawPassword, roleNames, true);
    }

    @Override
    public User registerUser(String username, String email, String rawPassword, Set<String> roleNames, boolean requiresPasswordChange) {
        return registerUser(username, username, email, "+94 77 123 4567", "Global Logistics", null, rawPassword, roleNames, requiresPasswordChange);
    }

    @Override
    public User registerUser(String username, String fullName, String email, String phone, String department, String rawPassword, Set<String> roleNames, boolean requiresPasswordChange) {
        return registerUser(username, fullName, email, phone, department, null, rawPassword, roleNames, requiresPasswordChange);
    }

    @Override
    public User registerUser(String username, String fullName, String email, String phone, String department, Long vendorId, String rawPassword, Set<String> roleNames, boolean requiresPasswordChange) {
        try {
            em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
              .setParameter("username", username)
              .getSingleResult();
            throw new IllegalArgumentException("Username '" + username + "' is already registered.");
        } catch (NoResultException e) {
            // User does not exist, proceed
        }

        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            roles.add(getOrCreateRole(roleName.toUpperCase()));
        }

        // Secure Salted SHA-256 Hashing for Database Storage
        String passwordHash = PasswordUtil.hashPassword(rawPassword);

        User user = new User(username, fullName, email, phone, department, passwordHash, roles, requiresPasswordChange);

        // If user is a vendor representative, link to the vendor entity
        if (vendorId != null) {
            Vendor vendor = em.find(Vendor.class, vendorId);
            if (vendor != null) {
                user.setVendor(vendor);
                user.setDepartment("Vendor Partner: " + vendor.getCompanyName());
            }
        }

        em.persist(user);

        AuditLog audit = new AuditLog("USER_REGISTRATION", "admin", "Registered staff/partner: " + fullName + " (" + username + " - " + user.getDepartment() + ") with roles " + roleNames);
        em.persist(audit);

        return user;
    }

    @Override
    public String resetUserTemporaryPassword(Long userId) {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }

        int randomCode = 1000 + new SecureRandom().nextInt(9000);
        String tempPass = "Scm#" + randomCode + "!";

        // Store secure cryptographic hash in database
        user.setPasswordHash(PasswordUtil.hashPassword(tempPass));
        user.setRequiresPasswordChange(true);
        em.merge(user);

        AuditLog audit = new AuditLog("TEMP_PASSWORD_GENERATED", "admin", "Generated temporary onboarding password for " + user.getFullName() + " (" + user.getUsername() + ")");
        em.persist(audit);

        return tempPass;
    }

    @Override
    public User authenticate(String username, String rawPassword) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                          .setParameter("username", username)
                          .getSingleResult();

            if (user != null && user.isActive()) {
                if (PasswordUtil.verifyPassword(rawPassword, user.getPasswordHash())) {
                    user.setLastLoginAt(LocalDateTime.now());
                    em.merge(user);
                    return user;
                }
            }
        } catch (NoResultException e) {
            return null;
        }
        return null;
    }

    @Override
    public Set<String> getUserRoles(String username) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                          .setParameter("username", username)
                          .getSingleResult();
            
            return user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        } catch (NoResultException e) {
            return Set.of();
        }
    }

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles LEFT JOIN FETCH u.vendor ORDER BY u.id DESC", User.class).getResultList();
    }

    @Override
    public User toggleUserStatus(Long userId) {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }

        user.setActive(!user.isActive());
        User updated = em.merge(user);

        AuditLog audit = new AuditLog("USER_STATUS_TOGGLE", "admin", "Toggled staff status for " + user.getFullName() + " (" + user.getUsername() + ") to " + (user.isActive() ? "ACTIVE" : "INACTIVE"));
        em.persist(audit);

        return updated;
    }

    @Override
    public User updateUserRole(Long userId, String roleName) {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }

        Role newRole = getOrCreateRole(roleName.toUpperCase());
        user.getRoles().clear();
        user.getRoles().add(newRole);

        User updated = em.merge(user);

        AuditLog audit = new AuditLog("USER_ROLE_UPDATE", "admin", "Updated security role for " + user.getFullName() + " to " + roleName);
        em.persist(audit);

        return updated;
    }

    @Override
    public User changePassword(String username, String currentPassword, String newPassword) {
        User user = authenticate(username, currentPassword);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or current password.");
        }

        user.setPasswordHash(PasswordUtil.hashPassword(newPassword));
        user.setRequiresPasswordChange(false);
        User updated = em.merge(user);

        AuditLog audit = new AuditLog("PASSWORD_CHANGED", username, "Staff member " + user.getFullName() + " updated permanent password successfully.");
        em.persist(audit);

        return updated;
    }
}

