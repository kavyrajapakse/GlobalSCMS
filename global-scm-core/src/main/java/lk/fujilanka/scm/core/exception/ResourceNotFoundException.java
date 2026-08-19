package lk.fujilanka.scm.core.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = false)
public class ResourceNotFoundException extends ScmBusinessException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
