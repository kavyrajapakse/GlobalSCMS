package lk.fujilanka.scm.web.exception;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ErrorMessage implements Serializable {

    private int statusCode;
    private String errorType;
    private String message;
    private String timestamp = LocalDateTime.now().toString();

    public ErrorMessage() {}

    public ErrorMessage(int statusCode, String errorType, String message) {
        this.statusCode = statusCode;
        this.errorType = errorType;
        this.message = message;
    }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public String getErrorType() { return errorType; }
    public void setErrorType(String errorType) { this.errorType = errorType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
