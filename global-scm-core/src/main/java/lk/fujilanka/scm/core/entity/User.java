package lk.fujilanka.scm.core.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u ORDER BY u.id DESC")
})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "department", length = 100)
    private String department;

    @JsonbTransient
    @XmlTransient
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "requires_password_change")
    private boolean requiresPasswordChange = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public User(String username, String fullName, String email, String phone, String department, String passwordHash, Set<Role> roles, boolean requiresPasswordChange) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.passwordHash = passwordHash;
        this.roles = roles;
        this.requiresPasswordChange = requiresPasswordChange;
        this.createdAt = LocalDateTime.now();
    }

    public User(String username, String email, String passwordHash, Set<Role> roles, boolean requiresPasswordChange) {
        this(username, username, email, "+94 77 123 4567", "Logistics & SCM", passwordHash, roles, requiresPasswordChange);
    }

    public User(String username, String email, String passwordHash, Set<Role> roles) {
        this(username, email, passwordHash, roles, false);
    }

    public User(String username, String passwordHash, Set<Role> roles) {
        this(username, username + "@gmail.com", passwordHash, roles, false);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName != null && !fullName.isBlank() ? fullName : username; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email != null ? email : (username + "@gmail.com"); }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone != null ? phone : "+94 77 123 4567"; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDepartment() { return department != null ? department : "Global Logistics"; }
    public void setDepartment(String department) { this.department = department; }

    @JsonbTransient
    @XmlTransient
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isRequiresPasswordChange() { return requiresPasswordChange; }
    public void setRequiresPasswordChange(boolean requiresPasswordChange) { this.requiresPasswordChange = requiresPasswordChange; }

    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}
