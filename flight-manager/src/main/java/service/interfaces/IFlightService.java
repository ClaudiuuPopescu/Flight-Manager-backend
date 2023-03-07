package service.interfaces;

import java.util.List;

import dto.FlightDto;
import exceptions.AirportException;
import exceptions.FlightException;
import exceptions.PlaneException;
import exceptions.ValidatorException;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.FlightTemplate;

public interface IFlightService {
	
	void addFlight(FlightDto flightDto) throws FlightException, ValidatorException, AirportException, PlaneException;
	
	void updateFlight(FlightDto flightDto) throws FlightException, ValidatorException, PlaneException, AirportException;
	
	void deleteFlight(Long flightID);
	
	List<Flight> getAllFlights();
	
	List<Flight> getFlightsByFlightTemplate(FlightTemplate flightTemplate);

	Flight getFlightById(Long flightID);
}
