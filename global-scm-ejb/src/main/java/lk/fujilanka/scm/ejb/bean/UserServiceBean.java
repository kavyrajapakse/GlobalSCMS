package lk.fujilanka.scm.ejb.bean;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.Role;
import lk.fujilanka.scm.core.entity.User;
import lk.fujilanka.scm.ejb.local.UserServiceLocal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class UserServiceBean implements UserServiceLocal {

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    @PostConstruct
    public void initDefaultUsers() {
        try {
            Long count = em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
            if (count == 0) {
                System.out.println("UserServiceBean: Database empty. Seeding default JPA roles and users...");

                Role adminRole = getOrCreateRole("ADMIN");
                Role coordRole = getOrCreateRole("COORDINATOR");
                Role customsRole = getOrCreateRole("CUSTOMS_AGENT");
                Role warehouseRole = getOrCreateRole("WAREHOUSE_MANAGER");
                Role vendorRole = getOrCreateRole("VENDOR_REP");

                registerUser("admin", "admin123", Set.of("ADMIN", "COORDINATOR"));
                registerUser("coordinator", "pass123", Set.of("COORDINATOR"));
                registerUser("customs", "pass123", Set.of("CUSTOMS_AGENT"));
                registerUser("warehouse", "pass123", Set.of("WAREHOUSE_MANAGER"));
                registerUser("vendor", "pass123", Set.of("VENDOR_REP"));

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

        User user = new User(username, rawPassword, roles);
        em.persist(user);
        return user;
    }

    @Override
    public User authenticate(String username, String rawPassword) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                          .setParameter("username", username)
                          .getSingleResult();

            if (user != null && user.isActive() && user.getPasswordHash().equals(rawPassword)) {
                return user;
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
}
