package lk.fujilanka.scm.core.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "inventory_items")
@NamedQueries({
    @NamedQuery(name = "InventoryItem.findAll", query = "SELECT i FROM InventoryItem i"),
    @NamedQuery(name = "InventoryItem.findLowStock", query = "SELECT i FROM InventoryItem i WHERE i.quantity < i.minThreshold")
})
public class InventoryItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String sku;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "min_threshold", nullable = false)
    private Integer minThreshold;

    @Column(name = "unit_price_usd")
    private Double unitPriceUSD;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id", foreignKey = @ForeignKey(name = "fk_inventory_warehouse"))
    private Warehouse warehouse;

    public InventoryItem() {}

    public InventoryItem(String sku, String itemName, Integer quantity, Integer minThreshold, Double unitPriceUSD, Warehouse warehouse) {
        this.sku = sku;
        this.itemName = itemName;
        this.quantity = quantity;
        this.minThreshold = minThreshold;
        this.unitPriceUSD = unitPriceUSD;
        this.warehouse = warehouse;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getMinThreshold() { return minThreshold; }
    public void setMinThreshold(Integer minThreshold) { this.minThreshold = minThreshold; }

    public Double getUnitPriceUSD() { return unitPriceUSD; }
    public void setUnitPriceUSD(Double unitPriceUSD) { this.unitPriceUSD = unitPriceUSD; }

    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }
}
