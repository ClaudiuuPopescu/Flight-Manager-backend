package exceptions;

public class RoleException extends ErrorCodeException{

	public RoleException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
