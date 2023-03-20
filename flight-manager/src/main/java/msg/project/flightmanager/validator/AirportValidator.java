package msg.project.flightmanager.validator;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.dto.AirportDto;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.modelHelper.CreateAirportModel;
import msg.project.flightmanager.modelHelper.EditAirportModel;
import msg.project.flightmanager.repository.AirportRepository;
<<<<<<< HEAD
import msg.project.flightmanager.repository.CompanyRepository;
=======
import msg.project.flightmanager.service.utils.StringUtils;
>>>>>>> feature/login

@Component
public class AirportValidator {
	@Autowired
	private AirportRepository airportRepository;
	@Autowired
	private AddressValidator addressValidator;
	@Autowired
	private CompanyRepository companyRepository;

	public void validateAirport(AirportDto airportDto) throws ValidatorException {

		validateAirportName(airportDto.getAirportName());
		validateRunWays(airportDto.getRunWarys());
		validateGateWays(airportDto.getGateWays());
		validateAddress(airportDto.getAddressDto());
	}
	
	public void validateCreateAiportModel(CreateAirportModel createAirportModel) throws ValidatorException {
		validateAirportName(createAirportModel.getAirportName());
		validateRunWays(createAirportModel.getRunWarys());
		validateGateWays(createAirportModel.getGateWays());
		validateCompanyNamesToCollab(createAirportModel.getCompanyNames_toCollab());
		this.addressValidator.validateCreateModel(createAirportModel.getAddress());

	}

	private void validateCompanyNamesToCollab(List<String> companyNames) {
		for(String companyName : companyNames) {
			this.companyRepository.findCompanyByName(companyName)
			.orElseThrow(() -> new FlightManagerException(
					HttpStatus.NOT_FOUND,
					MessageFormat.format("Company [{0}] can not be added to collaboration. It does not exist.", companyName)));
		}
		
	}

	public void validateEditAirport(EditAirportModel editAirportModel) {
		validateRunWays(editAirportModel.getRunWarys());
		validateGateWays(editAirportModel.getGateWays());
	}

		
	private void validateAddress(AddressDto addressDto) throws ValidatorException {
		this.addressValidator.validateAddressDto(addressDto);
	}

	private void validateAirportName(String airportName) throws ValidatorException {

		if (airportName.isEmpty())
			throw new ValidatorException("The airport name cannot be empty!", ErrorCode.EMPTY_FIELD);

		if (airportName.length() > 30)
			throw new ValidatorException("The airport name is too long!", ErrorCode.IS_TOO_LONG);

		if (!StringUtils.isAsciiPrintable(airportName))
			throw new ValidatorException(
					"The airport name should be only out of letters!",
					ErrorCode.IS_NOT_OUT_OF_LETTERS);

		Optional<Airport> aiport = this.airportRepository.findByName(airportName);

		if (aiport.isPresent()) {
			throw new FlightManagerException(
					HttpStatus.IM_USED, 
					MessageFormat.format("An aiport with the name [{0}] already exists. Find another one", airportName));
		}
	}

	private void validateRunWays(int runWays) {
		if (runWays < 1 && runWays < 8) {
			throw new FlightManagerException(
					HttpStatus.EXPECTATION_FAILED,
					"The number of run ways has to be in between 1 and 8");
		}
	}

	private void validateGateWays(int gateWays) {
		if (gateWays < 1 && gateWays < 200) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED,
					"The number of run ways has to be in between 1 and 200");
		}
	}

}
