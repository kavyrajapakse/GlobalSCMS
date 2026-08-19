package lk.fujilanka.scm.web.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lk.fujilanka.scm.core.exception.CarrierBookingRejectedException;
import lk.fujilanka.scm.core.exception.InsufficientStockException;
import lk.fujilanka.scm.core.exception.ResourceNotFoundException;
import lk.fujilanka.scm.core.exception.ScmBusinessException;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        System.err.println("[GlobalExceptionMapper]: Intercepted Exception: " + exception.getMessage());

        if (exception instanceof InsufficientStockException) {
            ErrorMessage err = new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(), "INSUFFICIENT_STOCK", exception.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(err).type(MediaType.APPLICATION_JSON).build();
        }

        if (exception instanceof CarrierBookingRejectedException) {
            ErrorMessage err = new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(), "CARRIER_BOOKING_REJECTED", exception.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(err).type(MediaType.APPLICATION_JSON).build();
        }

        if (exception instanceof ResourceNotFoundException) {
            ErrorMessage err = new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), "RESOURCE_NOT_FOUND", exception.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(err).type(MediaType.APPLICATION_JSON).build();
        }

        if (exception instanceof ScmBusinessException) {
            ErrorMessage err = new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(), "BUSINESS_RULE_VIOLATION", exception.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(err).type(MediaType.APPLICATION_JSON).build();
        }

        if (exception instanceof IllegalArgumentException) {
            ErrorMessage err = new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(), "INVALID_ARGUMENT", exception.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(err).type(MediaType.APPLICATION_JSON).build();
        }

        // Generic Fallback Exception Response (Prevents 500 HTML pages)
        ErrorMessage err = new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "INTERNAL_SERVER_ERROR", 
            exception.getMessage() != null ? exception.getMessage() : "An unexpected server error occurred.");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(err).type(MediaType.APPLICATION_JSON).build();
    }
}
