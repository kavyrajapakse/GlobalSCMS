package lk.fujilanka.scm.ejb;

import lk.fujilanka.scm.core.dto.DraftCargoItem;
import lk.fujilanka.scm.core.entity.Shipment;
import lk.fujilanka.scm.ejb.local.ShipmentServiceLocal;
import lk.fujilanka.scm.ejb.stateful.ShipmentDraftSessionBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentDraftSessionBeanTest {

    @Mock
    private ShipmentServiceLocal shipmentService;

    @InjectMocks
    private ShipmentDraftSessionBean draftSession;

    @BeforeEach
    void setUp() throws Exception {
        draftSession.initializeStatefulSession();
        Field serviceField = ShipmentDraftSessionBean.class.getDeclaredField("shipmentService");
        serviceField.setAccessible(true);
        serviceField.set(draftSession, shipmentService);
    }

    @Test
    @DisplayName("Stateful session should maintain conversational draft items across multi-step additions")
    void testStatefulItemAccumulation() {
        DraftCargoItem item1 = new DraftCargoItem("SKU-WH-101", "Microcontroller Unit", 10, 50.0, 45000.0);
        DraftCargoItem item2 = new DraftCargoItem("SKU-WH-102", "Lithium Battery", 5, 120.0, 425000.0);

        draftSession.addCargoItem(item1);
        draftSession.addCargoItem(item2);

        assertEquals(2, draftSession.getDraftItems().size());
        assertEquals(15, draftSession.getTotalItemCount());
        assertEquals(170.0, draftSession.getTotalEstimatedWeightKg());
        assertEquals(470000.0, draftSession.getTotalEstimatedCostLkr());
    }

    @Test
    @DisplayName("Stateful session should allow item removal and clearing")
    void testRemoveAndClear() {
        DraftCargoItem item1 = new DraftCargoItem("SKU-WH-101", "Microcontroller Unit", 10, 50.0, 45000.0);
        DraftCargoItem item2 = new DraftCargoItem("SKU-WH-102", "Lithium Battery", 5, 120.0, 425000.0);

        draftSession.addCargoItem(item1);
        draftSession.addCargoItem(item2);
        draftSession.removeCargoItem("SKU-WH-101");

        assertEquals(1, draftSession.getDraftItems().size());
        assertEquals("SKU-WH-102", draftSession.getDraftItems().get(0).getSku());

        draftSession.clearDraft();
        assertEquals(0, draftSession.getDraftItems().size());
    }

    @Test
    @DisplayName("Stateful session should finalize draft into active shipment via ShipmentService")
    void testFinalizeAndDispatch() {
        DraftCargoItem item = new DraftCargoItem("SKU-WH-104", "Steel Bracket", 20, 200.0, 25000.0);
        draftSession.addCargoItem(item);

        Shipment mockShipment = new Shipment("SCM-TRK-9999", "Lanka Freight", "Colombo Port", "Nagoya Port", 200.0, 25000.0);
        when(shipmentService.createShipment(any(Shipment.class), eq("coordinator"))).thenReturn(mockShipment);

        Shipment finalized = draftSession.finalizeAndDispatch("Lanka Freight", "SCM-TRK", "coordinator");

        assertNotNull(finalized);
        assertEquals("SCM-TRK-9999", finalized.getTrackingNumber());
        verify(shipmentService, times(1)).createShipment(any(Shipment.class), eq("coordinator"));
    }

    @Test
    @DisplayName("Finalize should throw IllegalStateException if draft manifest is empty")
    void testFinalizeEmptyDraft() {
        assertThrows(IllegalStateException.class, () -> {
            draftSession.finalizeAndDispatch("Lanka Freight", "SCM-TRK", "coordinator");
        });
    }
}
