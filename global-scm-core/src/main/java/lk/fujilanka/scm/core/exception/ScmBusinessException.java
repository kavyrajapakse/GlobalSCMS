package lk.fujilanka.scm.core.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ScmBusinessException extends RuntimeException {

    public ScmBusinessException(String message) {
        super(message);
    }

    public ScmBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
