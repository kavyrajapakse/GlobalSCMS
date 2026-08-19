package lk.fujilanka.scm.core.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class InsufficientStockException extends ScmBusinessException {

    public InsufficientStockException(String message) {
        super(message);
    }
}
