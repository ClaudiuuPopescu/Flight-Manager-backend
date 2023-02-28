package exceptions;

public class ValidatorException extends ErrorCodeException{

	public ValidatorException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
