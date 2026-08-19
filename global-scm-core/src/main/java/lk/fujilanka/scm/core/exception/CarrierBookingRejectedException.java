package lk.fujilanka.scm.core.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class CarrierBookingRejectedException extends ScmBusinessException {

    public CarrierBookingRejectedException(String message) {
        super(message);
    }
}
