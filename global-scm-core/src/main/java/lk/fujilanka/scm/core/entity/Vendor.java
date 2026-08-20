package lk.fujilanka.scm.core.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "vendors")
public class Vendor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false, unique = true)
    private String companyName;

    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    private String phone;
    private String country;

    @Column(name = "tax_id", nullable = false)
    private String taxId;

    @Column(name = "compliance_rating")
    private Double complianceRating = 98.5;

    private String status = "ACTIVE";

    public Vendor() {}

    public Vendor(String companyName, String contactEmail, String phone, String country, String taxId) {
        this.companyName = companyName;
        this.contactEmail = contactEmail;
        this.phone = phone;
        this.country = country;
        this.taxId = taxId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }

    public Double getComplianceRating() { return complianceRating; }
    public void setComplianceRating(Double complianceRating) { this.complianceRating = complianceRating; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
