package lk.fujilanka.scm.core.dto;

import java.io.Serializable;

/**
 * DTO representing a line item in a stateful draft shipment session.
 */
public class DraftCargoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sku;
    private String itemName;
    private int quantity;
    private double weightKg;
    private double estimatedCostLkr;

    public DraftCargoItem() {}

    public DraftCargoItem(String sku, String itemName, int quantity, double weightKg, double estimatedCostLkr) {
        this.sku = sku;
        this.itemName = itemName;
        this.quantity = quantity;
        this.weightKg = weightKg;
        this.estimatedCostLkr = estimatedCostLkr;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    public double getEstimatedCostLkr() { return estimatedCostLkr; }
    public void setEstimatedCostLkr(double estimatedCostLkr) { this.estimatedCostLkr = estimatedCostLkr; }
}
