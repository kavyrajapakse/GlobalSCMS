package lk.fujilanka.scm.ejb;

import jakarta.ejb.SessionContext;
import jakarta.persistence.EntityManager;
import jakarta.transaction.UserTransaction;
import lk.fujilanka.scm.core.entity.Shipment;
import lk.fujilanka.scm.core.exception.CarrierBookingRejectedException;
import lk.fujilanka.scm.ejb.transaction.CarrierBookingCoordinatorBean;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarrierBookingCoordinatorBeanTest {

    @Mock
    private EntityManager em;

    @Mock
    private SessionContext sessionContext;

    @Mock
    private UserTransaction utx;

    @InjectMocks
    private CarrierBookingCoordinatorBean bookingCoordinator;

    @BeforeEach
    void setUp() throws Exception {
        Field emField = CarrierBookingCoordinatorBean.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(bookingCoordinator, em);

        Field scField = CarrierBookingCoordinatorBean.class.getDeclaredField("sessionContext");
        scField.setAccessible(true);
        scField.set(bookingCoordinator, sessionContext);

        when(sessionContext.getUserTransaction()).thenReturn(utx);
    }

    @Test
    @DisplayName("Should successfully book carrier via BMT within standard cost threshold")
    void testStandardCarrierBookingBMT() throws Exception {
        Long shipmentId = 2L;
        Shipment shipment = new Shipment("SCM-TRK-3400", "Fuji Lanka", "Nagoya", "Hambantota", 2000.0, 150000.0);
        shipment.setId(shipmentId);

        when(em.find(Shipment.class, shipmentId)).thenReturn(shipment);
        when(em.merge(any(Shipment.class))).thenReturn(shipment);

        boolean result = bookingCoordinator.processCarrierBooking(shipmentId, "MAERSK", 150000.0, "coordinator");

        assertTrue(result);
        verify(utx, times(1)).begin();
        verify(utx, times(1)).commit();
        assertEquals("BOOKED_WITH_MAERSK", shipment.getStatus());
    }

    @Test
    @DisplayName("Should rollback transaction when booking cost exceeds enterprise threshold of LKR 15M")
    void testExceededThresholdRollback() throws Exception {
        Long shipmentId = 3L;
        Shipment shipment = new Shipment("SCM-TRK-9999", "Heavy Cargo Ltd", "Colombo", "Rotterdam", 5000.0, 20000000.0);
        shipment.setId(shipmentId);

        when(em.find(Shipment.class, shipmentId)).thenReturn(shipment);

        // Exceeds LKR 15,000,000 threshold
        assertThrows(CarrierBookingRejectedException.class, () -> {
            bookingCoordinator.processCarrierBooking(shipmentId, "EVERGREEN", 16000000.0, "coordinator");
        });

        verify(utx, times(1)).begin();
        verify(utx, times(1)).rollback();
    }
}
