package exceptions;

public class FlightTemplateException extends ErrorCodeException {

	public FlightTemplateException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
