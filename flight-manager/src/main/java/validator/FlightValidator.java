package validator;

import java.sql.Time;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dto.AirportDto;
import dto.FlightDto;
import dto.PlaneDto;
import exceptions.ErrorCode;
import exceptions.ValidatorException;

@Component
public class FlightValidator {
	
	@Autowired
	private PlaneValidator planeValidator;
	
	@Autowired
	private AirportValidator airportValidator;
	
	
	public void validateFlight(FlightDto flightDto) throws ValidatorException {
		
		validateAirport(flightDto.getFrom());
		validateAirport(flightDto.getTo());
		validateBoardingTime(flightDto.getBoardingTime());
		validateDuration(flightDto.getDuration());
		validateFlightName(flightDto.getFlightName());
		validateDate(flightDto.getDate());
		validatePlane(flightDto.getPlane());
	}
	
	private void validateFlightName(String flightName) throws ValidatorException {

		if (flightName.length() > 30) {
			throw new ValidatorException("The flight name is too long!", ErrorCode.IS_TOO_LONG);
		}
	}
	
	private void validateDate(LocalDate date) throws ValidatorException  {
		
		LocalDate currentDate = java.time.LocalDate.now();

		if (currentDate.getYear() > date.getYear())
			throw new ValidatorException("The year should be equale with the current year or in the future!", ErrorCode.WRONG_INTERVAL);

		else if (currentDate.getYear() == date.getYear()) {
			if (currentDate.getMonth().compareTo(date.getMonth()) == 0) {
				if (currentDate.getDayOfMonth() >= date.getDayOfMonth())
					throw new ValidatorException("The day should be higher than the current day!", ErrorCode.WRONG_INTERVAL);
			} else if (currentDate.getMonth().compareTo(date.getMonth()) == 1)
				throw new ValidatorException("The month should be higher than the current month!", ErrorCode.WRONG_INTERVAL);
		}
	}
	
	private void validateBoardingTime(Time time)  throws ValidatorException {
		//TODO mai trebuie sa gandesc aici
	}
	
	private void validateDuration(double duration) throws ValidatorException  {
		
		if(duration < 10)
			throw new ValidatorException("The duration should be higher than 10 minutes!", ErrorCode.IS_TOO_SHORT);
	}
	
	private void validateAirport(AirportDto airportDto)  throws ValidatorException {
		
		airportValidator.validateAirport(airportDto);
	}
	
	private void validatePlane(PlaneDto planeDto)  throws ValidatorException {
		
		planeValidator.validatePlane(planeDto);
	}
	

}
