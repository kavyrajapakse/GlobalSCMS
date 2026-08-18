package lk.fujilanka.scm.core.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "customs_filings")
public class CustomsFiling implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filing_number", nullable = false, unique = true)
    private String filingNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipment_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customs_shipment"))
    private Shipment shipment;

    @Column(nullable = false)
    private String status; // CLEARANCE_REQUESTED, APPROVED, REJECTED

    @Column(name = "declaration_details", columnDefinition = "TEXT")
    private String declarationDetails;

    @Column(name = "filed_at")
    private LocalDateTime filedAt = LocalDateTime.now();

    public CustomsFiling() {}

    public CustomsFiling(String filingNumber, Shipment shipment, String status, String declarationDetails) {
        this.filingNumber = filingNumber;
        this.shipment = shipment;
        this.status = status;
        this.declarationDetails = declarationDetails;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFilingNumber() { return filingNumber; }
    public void setFilingNumber(String filingNumber) { this.filingNumber = filingNumber; }

    public Shipment getShipment() { return shipment; }
    public void setShipment(Shipment shipment) { this.shipment = shipment; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDeclarationDetails() { return declarationDetails; }
    public void setDeclarationDetails(String declarationDetails) { this.declarationDetails = declarationDetails; }

    public LocalDateTime getFiledAt() { return filedAt; }
    public void setFiledAt(LocalDateTime filedAt) { this.filedAt = filedAt; }
}
