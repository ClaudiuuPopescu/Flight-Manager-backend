package service.interfaces;

import java.util.List;

import dto.FlightDto;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.FlightTemplate;

public interface IFlightService {
	
	void addFlight(FlightDto flightDto);
	
	void updateFlight(FlightDto flightDto);
	
	void deleteFlight(Long flightID);
	
	List<Flight> getAllFlights();
	
	List<Flight> getFlightsByFlightTemplate(FlightTemplate flightTemplate);

}