package msg.project.flightmanager.validator;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import msg.project.flightmanager.dto.AirportDto;
import msg.project.flightmanager.dto.FlightDto;
import msg.project.flightmanager.dto.PlaneDto;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.ValidatorException;

@Component
public class FlightValidator {
	
	@Autowired
	private PlaneValidator planeValidator;
	
	@Autowired
	private AirportValidator airportValidator;
	
	
	public void validateFlight(FlightDto flightDto) throws ValidatorException {
		
		validateAirport(flightDto.getFrom());
		validateAirport(flightDto.getTo());
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

		if (currentDate.compareTo(date) >= 1)
			throw new ValidatorException("The date should be in the future!", ErrorCode.WRONG_INTERVAL);
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
