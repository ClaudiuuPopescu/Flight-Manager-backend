package service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import dto.FlightDto;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.FlightTemplate;
import repository.FlightRepository;
import service.interfaces.IFlightService;

public class FlightService implements IFlightService {

	@Autowired
	private FlightRepository flightRepository;

	@Override
	public void addFlight(FlightDto flightDto) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFlight(FlightDto flightDto) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFlight(Long flightID) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Flight> getAllFlights() {

		return this.flightRepository.findAll();

	}

	@Override
	public List<Flight> getFlightsByFlightTemplate(FlightTemplate flightTemplate) {

		List<Flight> allFlights = getAllFlights();
		List<Flight> allFlightsWithSpecificTemplate = allFlights.stream()
				.filter(flight -> flight.getFlightTemplate().equals(flightTemplate)).collect(Collectors.toList());

		return allFlightsWithSpecificTemplate;
	}

}
