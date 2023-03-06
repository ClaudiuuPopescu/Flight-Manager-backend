package exceptions;

public class PlaneException extends ErrorCodeException{

	public PlaneException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
