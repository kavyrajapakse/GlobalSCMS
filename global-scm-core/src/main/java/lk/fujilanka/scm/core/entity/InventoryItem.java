package lk.fujilanka.scm.core.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "inventory_items")
@NamedQueries({
    @NamedQuery(name = "InventoryItem.findAll", query = "SELECT i FROM InventoryItem i ORDER BY i.id DESC"),
    @NamedQuery(name = "InventoryItem.findLowStock", query = "SELECT i FROM InventoryItem i WHERE i.quantity <= i.reorderThreshold")
})
public class InventoryItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SKU", nullable = false, unique = true, length = 50)
    private String sku;

    @Column(name = "item_name", nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String category = "General Supply";

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "min_threshold", nullable = false)
    private Integer reorderThreshold = 30;

    @Column(name = "unit_price_usd")
    private Double unitPriceUSD;

    @Column(name = "unit_price_lkr")
    private Double unitPriceLkr = 4500.0;

    @Column(name = "warehouse_location", length = 100)
    private String warehouseLocation = "Colombo Central Depot";

    @Column(length = 30)
    private String status = "IN_STOCK";

    @Version
    private Long version;

    public InventoryItem() {}

    public InventoryItem(String sku, String name, String category, Integer quantity, Integer reorderThreshold, Double unitPriceLkr) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.reorderThreshold = reorderThreshold;
        this.unitPriceLkr = unitPriceLkr;
        this.status = quantity <= 0 ? "OUT_OF_STOCK" : (quantity <= reorderThreshold ? "LOW_STOCK" : "IN_STOCK");
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        this.status = quantity <= 0 ? "OUT_OF_STOCK" : (quantity <= (this.reorderThreshold != null ? this.reorderThreshold : 30) ? "LOW_STOCK" : "IN_STOCK");
    }

    public Integer getReorderThreshold() { return reorderThreshold; }
    public Integer getMinThreshold() { return reorderThreshold; }
    public void setReorderThreshold(Integer reorderThreshold) { this.reorderThreshold = reorderThreshold; }

    public Double getUnitPriceUSD() { return unitPriceUSD; }
    public void setUnitPriceUSD(Double unitPriceUSD) { this.unitPriceUSD = unitPriceUSD; }

    public Double getUnitPriceLkr() { 
        if (unitPriceLkr != null && unitPriceLkr > 0) return unitPriceLkr;
        if (unitPriceUSD != null && unitPriceUSD > 0) return unitPriceUSD;
        return 4500.0;
    }
    public void setUnitPriceLkr(Double unitPriceLkr) { this.unitPriceLkr = unitPriceLkr; }

    public String getWarehouseLocation() { return warehouseLocation; }
    public void setWarehouseLocation(String warehouseLocation) { this.warehouseLocation = warehouseLocation; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
