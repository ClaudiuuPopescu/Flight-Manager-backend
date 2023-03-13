package msg.project.flightmanager.exceptions;

public class CompanyException extends ErrorCodeException{

	private static final long serialVersionUID = 1L;

	public CompanyException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
