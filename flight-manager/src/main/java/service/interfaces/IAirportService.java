package service.interfaces;

import java.util.List;

import dto.AirportDto;

public interface IAirportService {

	List<AirportDto> getAll();

	boolean createAirport(AirportDto airportDto);
	
	boolean removeAirport(String airportName);
}
