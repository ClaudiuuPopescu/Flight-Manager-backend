package msg.project.flightmanager.exceptions;

public class AuthException extends ErrorCodeException{

	public AuthException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
