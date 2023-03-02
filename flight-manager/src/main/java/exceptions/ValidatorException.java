package exceptions;

public class ValidatorException extends ErrorCodeException{

	private static final long serialVersionUID = 1L;

	public ValidatorException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
