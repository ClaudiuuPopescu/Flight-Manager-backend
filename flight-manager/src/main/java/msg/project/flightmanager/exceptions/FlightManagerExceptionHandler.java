package msg.project.flightmanager.exceptions;

import java.time.ZonedDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class FlightManagerExceptionHandler {

	@ExceptionHandler(value = FlightManagerException.class)
	public ResponseEntity<Object> request(FlightManagerException e) {

		ApiExceptionModel apiException = new ApiExceptionModel(ZonedDateTime.now(), e.getHttpStatus(),
				e.getHttpStatus().value(), e.getMessage());
		return new ResponseEntity<>(apiException, e.getHttpStatus());
	}
}
