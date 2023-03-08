package service.interfaces;

import java.util.List;

import dto.AirportDto;
import exceptions.ValidatorException;
import modelHelper.ActionCompanyAirportCollab;
import modelHelper.CreateAirportModel;
import modelHelper.EditAirportModel;

public interface IAirportService {

	List<AirportDto> getAll();

	boolean createAirport(CreateAirportModel createAirportModel) throws ValidatorException;
	
	boolean editAirport(EditAirportModel airportModel) throws ValidatorException;

	boolean removeAirport(String codeIdentifier);

	boolean addCompanyCollab(ActionCompanyAirportCollab actionCompanyAirportCollab);

	boolean removeCompanyCollab(ActionCompanyAirportCollab actionCompanyAirportCollab);

}
