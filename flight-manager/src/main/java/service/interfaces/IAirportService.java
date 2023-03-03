package service.interfaces;

import java.util.List;

import dto.AirportDto;
import exceptions.ValidatorException;

public interface IAirportService {

	List<AirportDto> getAll();

	boolean createAirport(AirportDto airportDto) throws ValidatorException;
	
	boolean removeAirport(String airportName);
}
