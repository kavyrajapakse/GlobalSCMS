package lk.fujilanka.scm.ejb;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lk.fujilanka.scm.core.entity.InventoryItem;
import lk.fujilanka.scm.core.entity.Role;
import lk.fujilanka.scm.core.entity.Shipment;
import lk.fujilanka.scm.core.entity.User;
import lk.fujilanka.scm.ejb.stateless.ShipmentServiceBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ShipmentServiceBeanTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<User> userTypedQuery;

    @Mock
    private TypedQuery<InventoryItem> inventoryTypedQuery;

    @InjectMocks
    private ShipmentServiceBean shipmentService;

    @BeforeEach
    void setUp() throws Exception {
        Field emField = ShipmentServiceBean.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(shipmentService, em);

        User mockUser = new User("coordinator", "Nimal Perera", "nimal@gmail.com", "+94 71 222 3344", "Logistics", "pass123", Set.of(new Role("COORDINATOR")), false);

        when(em.createQuery(contains("User"), eq(User.class))).thenReturn(userTypedQuery);
        when(userTypedQuery.setParameter(eq("username"), anyString())).thenReturn(userTypedQuery);
        when(userTypedQuery.getSingleResult()).thenReturn(mockUser);

        when(em.createQuery(contains("InventoryItem"), eq(InventoryItem.class))).thenReturn(inventoryTypedQuery);
        when(inventoryTypedQuery.getResultList()).thenReturn(List.of());
    }

    @Test
    @DisplayName("Should create shipment and persist entity with audit log")
    void testCreateShipment() {
        Shipment shipment = new Shipment("SCM-TRK-3455", "Lanka Freight", "Colombo", "Nagoya", 1500.0, 250000.0);
        shipment.setCargoDescription("Standard cargo");

        Shipment created = shipmentService.createShipment(shipment, "coordinator");

        assertNotNull(created);
        assertEquals("SCM-TRK-3455", created.getTrackingNumber());
        verify(em, times(1)).persist(shipment);
    }

    @Test
    @DisplayName("Should update shipment status and record audit trail")
    void testUpdateShipmentStatus() {
        Long shipmentId = 1L;
        Shipment shipment = new Shipment("SCM-TRK-7691", "Fuji Lanka", "Nagoya", "Hambantota", 800.0, 200000.0);
        shipment.setId(shipmentId);

        when(em.find(Shipment.class, shipmentId)).thenReturn(shipment);
        when(em.merge(any(Shipment.class))).thenReturn(shipment);

        Shipment updated = shipmentService.updateShipmentStatus(shipmentId, "DELIVERED", "coordinator");

        assertNotNull(updated);
        assertEquals("DELIVERED", updated.getStatus());
        verify(em, times(1)).merge(shipment);
    }
}

