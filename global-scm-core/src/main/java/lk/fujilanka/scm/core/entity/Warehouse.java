package lk.fujilanka.scm.core.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "warehouses")
public class Warehouse implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "location_code", nullable = false, length = 20)
    private String locationCode;

    @Column(name = "capacity_units")
    private Integer capacityUnits;

    public Warehouse() {}

    public Warehouse(String name, String locationCode, Integer capacityUnits) {
        this.name = name;
        this.locationCode = locationCode;
        this.capacityUnits = capacityUnits;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocationCode() { return locationCode; }
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }

    public Integer getCapacityUnits() { return capacityUnits; }
    public void setCapacityUnits(Integer capacityUnits) { this.capacityUnits = capacityUnits; }
}
