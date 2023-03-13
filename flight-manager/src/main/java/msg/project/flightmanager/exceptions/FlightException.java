package msg.project.flightmanager.exceptions;

public class FlightException extends ErrorCodeException{

	public FlightException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
