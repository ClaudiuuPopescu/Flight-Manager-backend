package validator;

import java.text.MessageFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import dto.AddressDto;
import dto.AirportDto;
import exceptions.ErrorCode;
import exceptions.FlightManagerException;
import exceptions.ValidatorException;
import modelHelper.CreateAirportModel;
import modelHelper.EditAirportModel;
import msg.project.flightmanager.model.Airport;
import repository.AirportRepository;

@Component
public class AirportValidator {
	@Autowired
	private AirportRepository airportRepository;
	
	
	@Autowired
	private AddressValidator addressValidator;

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
		this.addressValidator.validateCreateModel(createAirportModel.getAddress());

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
			throw new ValidatorException("The airport name should be only out of letters!",
					ErrorCode.IS_NOT_OUT_OF_LETTERS);

		Optional<Airport> aiport = this.airportRepository.findByName(airportName);

		if (aiport.isPresent()) {
			throw new FlightManagerException(HttpStatus.IM_USED, MessageFormat
					.format("An aiport with the name [{0}] already exists. Find another one", airportName));
		}
	}

	private void validateRunWays(int runWays) {
		if (Integer.valueOf(runWays) == null) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "The run ways field is required");
		}

		if (runWays < 1 && runWays < 8) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED,
					"The number of run ways has to be in between 1 and 8");
		}
	}

	private void validateGateWays(int gateWays) {
		if (Integer.valueOf(gateWays) == null) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "The gate ways field is required");
		}

		if (gateWays < 1 && gateWays < 200) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED,
					"The number of run ways has to be in between 1 and 200");
		}
	}

}
