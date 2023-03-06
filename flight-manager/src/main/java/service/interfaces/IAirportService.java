package service.interfaces;

import java.util.List;

import dto.AirportDto;
import exceptions.ValidatorException;
import modelHelper.ActionCompanyAirportCollab;
import modelHelper.EditAirportModel;

public interface IAirportService {

	List<AirportDto> getAll();

	boolean createAirport(AirportDto airportDto) throws ValidatorException;
	
	boolean editAirport(EditAirportModel airportModel);

	boolean removeAirport(String codeIdentifier);

	boolean addCompanyCollab(ActionCompanyAirportCollab actionCompanyAirportCollab);

	boolean removeCompanyCollab(ActionCompanyAirportCollab actionCompanyAirportCollab);

}
