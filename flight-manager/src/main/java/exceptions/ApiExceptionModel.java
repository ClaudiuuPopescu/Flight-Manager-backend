package exceptions;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

public class ApiExceptionModel {
	public ApiExceptionModel(ZonedDateTime dateTime, HttpStatus httpStatus, Integer status, String message) {
	}
}
