package service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import converter.AirportConverter;
import dto.AirportDto;
import exceptions.FlightManagerException;
import msg.project.flightmanager.model.Airport;
import repository.AirportRepository;
import service.interfaces.IAirportService;

public class AirportService implements IAirportService {
	@Autowired
	private AirportRepository airportRepository;
	@Autowired
	private AirportConverter airportConverter;

	@Override
	public List<AirportDto> getAll() {
		List<Airport> airports = StreamSupport.stream(this.airportRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());

		if (airports.isEmpty()) {
			throw new FlightManagerException(HttpStatus.NO_CONTENT, "No airports found");
		}

		List<AirportDto> airportsDto = airports.stream().map(this.airportConverter::convertToDTO)
				.collect(Collectors.toList());
		return airportsDto;
	}

	@Override
	public boolean createAirport(AirportDto airportDto) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAirport(String airportName) {
		// TODO verificare rol current user

		Airport airport = this.airportRepository.findByName(airportName)
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not delete airport. Airport [{0}] not found", airportName)));

		// TODO set field false in flight template.
		// TODO delete flight

		this.airportRepository.delete(airport);
		return true;
	}

}
