package msg.project.flightmanager.exceptions;

public class AirportException extends ErrorCodeException{

	public AirportException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
