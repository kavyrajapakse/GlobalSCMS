package lk.fujilanka.scm.core.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EntityValidationTest {

    @Test
    @DisplayName("User entity should maintain proper state and default values")
    void testUserEntityState() {
        Role roleAdmin = new Role("ADMIN");
        User user = new User("kavithma", "Kavithma Rajapakse", "kavithma@gmail.com", "+94 77 123 4567", "Logistics", "pass123", Set.of(roleAdmin), true);

        assertEquals("kavithma", user.getUsername());
        assertEquals("Kavithma Rajapakse", user.getFullName());
        assertEquals("kavithma@gmail.com", user.getEmail());
        assertTrue(user.isActive());
        assertTrue(user.isRequiresPasswordChange());
        assertEquals(1, user.getRoles().size());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    @DisplayName("Shipment entity should correctly initialize tracking and default currency")
    void testShipmentEntityState() {
        Shipment shipment = new Shipment("SCM-TRK-3455", "Lanka Freight", "Colombo Port", "Nagoya Port", 500.0, 125000.0);
        shipment.setTransportMode("OCEAN");

        assertEquals("Colombo Port", shipment.getOrigin());
        assertEquals("Nagoya Port", shipment.getDestination());
        assertEquals("PENDING", shipment.getStatus());
        assertEquals(125000.0, shipment.getCostLkr());
        assertEquals("OCEAN", shipment.getTransportMode());
    }

    @Test
    @DisplayName("InventoryItem entity should validate low stock condition")
    void testInventoryItemLowStock() {
        InventoryItem item = new InventoryItem("SKU-WH-102", "Lithium Battery 48V", "Electronics", 15, 30, 85000.0);

        assertEquals("SKU-WH-102", item.getSku());
        assertEquals(15, item.getQuantity());
        assertEquals(30, item.getReorderThreshold());
        assertEquals("LOW_STOCK", item.getStatus());
    }

    @Test
    @DisplayName("Vendor entity should validate Tax ID and compliance rating")
    void testVendorEntity() {
        Vendor vendor = new Vendor("Fuji Lanka Supplies", "contact@fujilanka.lk", "+94 77 888 9999", "Sri Lanka", "TAX-LK-2303");

        assertEquals("Fuji Lanka Supplies", vendor.getCompanyName());
        assertEquals("TAX-LK-2303", vendor.getTaxId());
        assertEquals("ACTIVE", vendor.getStatus());
        assertEquals(98.5, vendor.getComplianceRating());
    }
}
