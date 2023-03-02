package validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dto.AddressDto;
import dto.AirportDto;
import exceptions.ErrorCode;
import exceptions.ValidatorException;

@Component
public class AirportValidator {
	
	
	@Autowired
	private AddressValidator addressValidator;

	public void validateAirport(AirportDto airportDto) throws ValidatorException {

		validateAddress(airportDto.getAddressDto());
		validateAirportName(airportDto.getAirportName());
	}
	
	private void validateAddress(AddressDto addressDto) throws ValidatorException {
		addressValidator.validateAddress(addressDto);
	}

	private void validateAirportName(String airportName) throws ValidatorException {

		if (airportName.isEmpty())
			throw new ValidatorException("The airport name cannot be empty!", ErrorCode.EMPTY_FIELD);

		if (airportName.length() > 30)
			throw new ValidatorException("The airport name is too long!", ErrorCode.IS_TOO_LONG);

		if (!StringUtils.isAsciiPrintable(airportName))
			throw new ValidatorException("The airport name should be only out of letters!",
					ErrorCode.IS_NOT_OUT_OF_LETTERS);

	}
}
