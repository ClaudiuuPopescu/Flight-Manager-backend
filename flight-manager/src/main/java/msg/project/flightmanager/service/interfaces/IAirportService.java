package msg.project.flightmanager.service.interfaces;

import java.util.List;

import msg.project.flightmanager.dto.AirportDto;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.modelHelper.ActionCompanyAirportCollab;
import msg.project.flightmanager.modelHelper.CreateAirportModel;
import msg.project.flightmanager.modelHelper.EditAirportModel;

public interface IAirportService {

	List<AirportDto> getAll();

	boolean createAirport(CreateAirportModel createAirportModel) throws ValidatorException;
	
	boolean editAirport(EditAirportModel airportModel) throws ValidatorException;

	boolean removeAirport(String codeIdentifier);

	boolean addCompanyCollab(ActionCompanyAirportCollab actionCompanyAirportCollab);

	boolean removeCompanyCollab(ActionCompanyAirportCollab actionCompanyAirportCollab);

}
