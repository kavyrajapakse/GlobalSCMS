package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;
import lk.fujilanka.scm.core.entity.InventoryItem;
import java.util.List;

@Local
public interface InventoryServiceLocal {
    InventoryItem createItem(InventoryItem item);
    InventoryItem adjustStock(Long itemId, int quantityDelta, String username);
    List<InventoryItem> getAllItems();
    List<InventoryItem> getLowStockItems();
}