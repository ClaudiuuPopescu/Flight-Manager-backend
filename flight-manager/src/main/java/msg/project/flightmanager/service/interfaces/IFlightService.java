package msg.project.flightmanager.service.interfaces;

import java.util.List;
import java.util.Optional;

import msg.project.flightmanager.dto.FlightDto;
import msg.project.flightmanager.exceptions.AirportException;
import msg.project.flightmanager.exceptions.FlightException;
import msg.project.flightmanager.exceptions.PlaneException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.FlightTemplate;

public interface IFlightService {

	void addFlight(FlightDto flightDto) throws FlightException, ValidatorException, AirportException, PlaneException;

	void updateFlight(FlightDto flightDto) throws FlightException, ValidatorException, PlaneException, AirportException;

	void deleteFlight(Long flightID) throws FlightException;

	List<Flight> getAllFlights();

	List<Flight> getFlightsByFlightTemplate(FlightTemplate flightTemplate);

	Optional<Flight> getFlightById(Long flightID);

	List<Flight> getCanceledAndNotActivFlights();

	List<Flight> getAllActiv();

}
