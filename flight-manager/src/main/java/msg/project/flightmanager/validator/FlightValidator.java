package msg.project.flightmanager.validator;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import msg.project.flightmanager.dto.AirportDto;
import msg.project.flightmanager.dto.FlightDto;
import msg.project.flightmanager.dto.PlaneDto;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.repository.AirportRepository;
import msg.project.flightmanager.repository.PlaneRepository;

@Component
public class FlightValidator {
	
	@Autowired
	private PlaneRepository planeRepository;
	
	@Autowired
	private AirportRepository airportRepository;
	
	
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
	
	private void validateAirport(String airportName)  throws ValidatorException {
		
		Optional<Airport> airportOptional = this.airportRepository.findByName(airportName);
		if(airportOptional.isEmpty())
			throw new ValidatorException(String.format("An airport with the name %s does not exist!", airportName), ErrorCode.NOT_AN_EXISTING_AIRPORT);
		
	}
	
	private void validatePlane(int planeTailNumber)  throws ValidatorException {
		Optional<Plane> optionalPlane = this.planeRepository.findByTailNumber(planeTailNumber);
		if(optionalPlane.isEmpty())
			throw new ValidatorException(String.format("A plane with the tailnumber %d does not exist!", planeTailNumber), ErrorCode.NOT_AN_EXISTING_PLANE);
	}
	

}
