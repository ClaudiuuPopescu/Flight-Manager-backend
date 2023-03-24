package msg.project.flightmanager.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import msg.project.flightmanager.dto.AirportDto;
import msg.project.flightmanager.dto.FlightDto;
import msg.project.flightmanager.dto.PlaneDto;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.validator.AirportValidator;
import msg.project.flightmanager.validator.FlightValidator;
import msg.project.flightmanager.validator.PlaneValidator;

@ExtendWith(MockitoExtension.class)
public class FlightValidatorTest {

	@Mock
	private PlaneValidator planeValidator;

	@Mock
	private AirportValidator airportValidator;

	@Mock
	private AirportDto from;

	@Mock
	private AirportDto to;

	@Mock
	private PlaneDto plane;

	@InjectMocks
	private FlightValidator flightValidator;

	private FlightDto flight;

	@BeforeEach
	void init() {
		this.flight = new FlightDto();
	}

	@Test
	void validateFlight_throwsValidatorException_whenTheAirportForFromIsWrong() throws ValidatorException {
		this.flight.setFrom(from);
		doThrow(new ValidatorException("The airport name cannot be empty!", ErrorCode.EMPTY_FIELD))
				.when(this.airportValidator).validateAirport(from);
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.flightValidator.validateFlight(this.flight));
		assertEquals("The airport name cannot be empty!", exception.getMessage());
		assertEquals(ErrorCode.EMPTY_FIELD, exception.getErrorCode());
	}

	@Test
	void validateFlight_throwsValidatorException_whenTheAirportForToIsWrong() throws ValidatorException {
		this.flight.setFrom(from);
		this.flight.setTo(to);
		Mockito.lenient().doNothing().when(this.airportValidator).validateAirport(from);
		doThrow(new ValidatorException("The airport name cannot be empty!", ErrorCode.EMPTY_FIELD))
				.when(this.airportValidator).validateAirport(to);
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.flightValidator.validateFlight(this.flight));
		assertEquals("The airport name cannot be empty!", exception.getMessage());
		assertEquals(ErrorCode.EMPTY_FIELD, exception.getErrorCode());
	}

	@Test
	void validateFlight_throwsValidatorException_whenDurationIsTooShort() throws ValidatorException {
		this.flight.setFrom(from);
		this.flight.setTo(to);
		this.flight.setDuration(9);
		Mockito.lenient().doNothing().when(this.airportValidator).validateAirport(from);
		Mockito.lenient().doNothing().when(this.airportValidator).validateAirport(to);

		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.flightValidator.validateFlight(this.flight));
		assertEquals("The duration should be higher than 10 minutes!", exception.getMessage());
		assertEquals(ErrorCode.IS_TOO_SHORT, exception.getErrorCode());
	}

	@Test
	void validateFlight_throwsValidatorException_whenFlightNameIsTooLong() throws ValidatorException {
		this.flight.setFrom(from);
		this.flight.setTo(to);
		this.flight.setDuration(22);
		this.flight.setFlightName("thisIsAWrongFlightNameBecauseISTooLong");
		Mockito.lenient().doNothing().when(this.airportValidator).validateAirport(from);
		Mockito.lenient().doNothing().when(this.airportValidator).validateAirport(to);

		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.flightValidator.validateFlight(this.flight));
		assertEquals("The flight name is too long!", exception.getMessage());
		assertEquals(ErrorCode.IS_TOO_LONG, exception.getErrorCode());
	}

	@Test
	void validateFlight_throwsValidatorException_whenThePlaneIsWrong() throws ValidatorException {
		this.flight.setFrom(from);
		this.flight.setTo(to);
		this.flight.setDuration(22);
		this.flight.setFlightName("goodName");
		LocalDate localdate = java.time.LocalDate.now();
		this.flight.setDate(localdate.plusDays(1));
		this.flight.setPlane(plane);
		Mockito.lenient().doNothing().when(this.airportValidator).validateAirport(from);
		Mockito.lenient().doNothing().when(this.airportValidator).validateAirport(to);

		doThrow(new ValidatorException("The model field cannot be empty", ErrorCode.EMPTY_FIELD))
				.when(this.planeValidator).validatePlane(plane);
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.flightValidator.validateFlight(this.flight));
		assertEquals("The model field cannot be empty", exception.getMessage());
		assertEquals(ErrorCode.EMPTY_FIELD, exception.getErrorCode());
	}
	
	@Test
	void validateFlight_throwsValidatorException_whenTheDateIsWrong() throws ValidatorException {
		this.flight.setFrom(from);
		this.flight.setTo(to);
		this.flight.setDuration(22);
		this.flight.setFlightName("goodName");
		this.flight.setDate(LocalDate.of(2001, 8, 20));
		Mockito.lenient().doNothing().when(this.airportValidator).validateAirport(from);
		Mockito.lenient().doNothing().when(this.airportValidator).validateAirport(to);

		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.flightValidator.validateFlight(this.flight));
		assertEquals("The date should be in the future!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());
	}
	
	@Test
	void validateFlight_throwsNothing_whenTheFlightIsValid() throws ValidatorException {
		this.flight.setFrom(from);
		this.flight.setTo(to);
		this.flight.setDuration(22);
		this.flight.setFlightName("goodName");
		LocalDate localdate = java.time.LocalDate.now();
		this.flight.setDate(localdate.plusDays(1));
		this.flight.setPlane(plane);
		Mockito.lenient().doNothing().when(this.airportValidator).validateAirport(from);
		Mockito.lenient().doNothing().when(this.airportValidator).validateAirport(to);
		Mockito.lenient().doNothing().when(this.planeValidator).validatePlane(plane);
		this.flightValidator.validateFlight(this.flight);
	}
	
}
