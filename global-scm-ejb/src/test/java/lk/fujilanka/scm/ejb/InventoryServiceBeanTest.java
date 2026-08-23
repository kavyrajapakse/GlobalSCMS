package lk.fujilanka.scm.ejb;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lk.fujilanka.scm.core.entity.InventoryItem;
import lk.fujilanka.scm.ejb.stateless.InventoryServiceBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceBeanTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<InventoryItem> typedQuery;

    @InjectMocks
    private InventoryServiceBean inventoryService;

    @BeforeEach
    void setUp() throws Exception {
        Field emField = InventoryServiceBean.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(inventoryService, em);
    }

    @Test
    @DisplayName("Should adjust item stock quantity and update status")
    void testAdjustStock() {
        Long itemId = 3L;
        InventoryItem item = new InventoryItem("SKU-WH-103", "Fiber Optic Transceiver", "Telecom", 100, 20, 32000.0);
        item.setId(itemId);

        when(em.find(InventoryItem.class, itemId)).thenReturn(item);
        when(em.merge(any(InventoryItem.class))).thenReturn(item);

        InventoryItem updated = inventoryService.adjustStock(itemId, -10, "warehouse");

        assertNotNull(updated);
        assertEquals(90, updated.getQuantity());
        assertEquals("IN_STOCK", updated.getStatus());
        verify(em, times(1)).merge(item);
    }

    @Test
    @DisplayName("Should flag LOW_STOCK status when quantity falls below minimum threshold")
    void testLowStockThresholdFlag() {
        Long itemId = 2L;
        InventoryItem item = new InventoryItem("SKU-WH-102", "Lithium Battery", "Electronics", 25, 30, 85000.0);
        item.setId(itemId);

        when(em.find(InventoryItem.class, itemId)).thenReturn(item);
        when(em.merge(any(InventoryItem.class))).thenReturn(item);

        InventoryItem updated = inventoryService.adjustStock(itemId, -10, "warehouse");

        assertNotNull(updated);
        assertEquals(15, updated.getQuantity());
        assertEquals("LOW_STOCK", updated.getStatus());
    }

    @Test
    @DisplayName("Should retrieve all low stock inventory items")
    void testGetLowStockItems() {
        InventoryItem item1 = new InventoryItem("SKU-WH-102", "Lithium Battery", "Electronics", 15, 30, 85000.0);

        when(em.createNamedQuery("InventoryItem.findLowStock", InventoryItem.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(item1));

        List<InventoryItem> lowStock = inventoryService.getLowStockItems();

        assertNotNull(lowStock);
        assertEquals(1, lowStock.size());
        assertEquals("SKU-WH-102", lowStock.get(0).getSku());
    }
}

