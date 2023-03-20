package msg.project.flightmanager.exceptions;

import lombok.Data;

@Data
public class ErrorCodeException extends Exception {
	
    protected final ErrorCode errorCode;

    public ErrorCodeException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
