package msg.project.flightmanager.exceptions;

public class RefreshTokenException extends ErrorCodeException{

	public RefreshTokenException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
