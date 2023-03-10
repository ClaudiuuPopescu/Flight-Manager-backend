package msg.project.flightmanager.exceptions;

public class UserException extends ErrorCodeException{

	public UserException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
