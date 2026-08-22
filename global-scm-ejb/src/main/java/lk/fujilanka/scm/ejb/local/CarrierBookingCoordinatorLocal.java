package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;

@Local
public interface CarrierBookingCoordinatorLocal {
    boolean processCarrierBooking(Long shipmentId, String carrierCode, double costLkr, String username);
}
