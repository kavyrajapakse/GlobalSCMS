package lk.fujilanka.scm.core.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "shipments")
@NamedQueries({
    @NamedQuery(name = "Shipment.findAll", query = "SELECT s FROM Shipment s ORDER BY s.id DESC"),
    @NamedQuery(name = "Shipment.findByStatus", query = "SELECT s FROM Shipment s WHERE s.status = :status")
})
public class Shipment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_number", nullable = false, unique = true, length = 50)
    private String trackingNumber;

    @Column(name = "vendor_name", length = 100)
    private String vendorName;

    @Column(nullable = false, length = 100)
    private String origin;

    @Column(nullable = false, length = 100)
    private String destination;

    @Column(name = "cargo_description", length = 255)
    private String cargoDescription;

    @Column(name = "transport_mode", length = 20)
    private String transportMode = "OCEAN";

    @Column(length = 20)
    private String priority = "STANDARD";

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "cost_lkr")
    private Double costLkr;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";

    @Temporal(TemporalType.DATE)
    @Column(name = "expected_delivery_date")
    private Date expectedDeliveryDate;

    @Version
    private Long version; // Optimistic locking for LO 2

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    public Shipment() {}

    public Shipment(String trackingNumber, String vendorName, String origin, String destination, Double weightKg, Double costLkr) {
        this.trackingNumber = trackingNumber;
        this.vendorName = vendorName;
        this.origin = origin;
        this.destination = destination;
        this.weightKg = weightKg;
        this.costLkr = costLkr;
        this.status = "PENDING";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getCargoDescription() { return cargoDescription; }
    public void setCargoDescription(String cargoDescription) { this.cargoDescription = cargoDescription; }

    public String getTransportMode() { return transportMode; }
    public void setTransportMode(String transportMode) { this.transportMode = transportMode; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }

    public Double getCostLkr() { return costLkr; }
    public void setCostLkr(Double costLkr) { this.costLkr = costLkr; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public void setExpectedDeliveryDate(Date expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
}