package msg.project.flightmanager.validator;

import org.springframework.stereotype.Component;

import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.modelHelper.CreateAddressModel;

@Component
public class AddressValidator {

	public void validateCreateModel(CreateAddressModel createAddressModel) throws ValidatorException {
		validateCountry(createAddressModel.getCountry());
		validateCity(createAddressModel.getCity());
		validateStreet(createAddressModel.getStreet());
		validateStreetNumber(createAddressModel.getStreetNumber());
		validateApartament(createAddressModel.getApartment());
	}

	public void validateAddressDto(AddressDto addressDto) throws ValidatorException {
		validateCountry(addressDto.getCountry());
		validateCity(addressDto.getCity());
		validateStreet(addressDto.getStreet());
		validateStreetNumber(addressDto.getStreetNumber());
		validateApartament(addressDto.getApartment());
	}

	public void validateCountry(String country) throws ValidatorException {

		if (country.isEmpty())
			throw new ValidatorException("Country cannot be empty!", ErrorCode.EMPTY_FIELD);

		if (!StringUtils.isAsciiPrintable(country))
			throw new ValidatorException("Country can contain only letters!", ErrorCode.IS_NOT_OUT_OF_LETTERS);

		if (country.length() < 4)
			throw new ValidatorException("There is no Country with less than 4 characters!", ErrorCode.IS_TOO_SHORT);

	}

	public void validateCity(String city) throws ValidatorException {

		if (city.isEmpty())
			throw new ValidatorException("City cannot be empty!", ErrorCode.EMPTY_FIELD);

		if (!StringUtils.isAsciiPrintable(city))
			throw new ValidatorException("City can contain only letters!", ErrorCode.IS_NOT_OUT_OF_LETTERS);
	}

	public void validateStreet(String street) throws ValidatorException {

		if (street.isEmpty())
			throw new ValidatorException("Street cannot be empty!", ErrorCode.EMPTY_FIELD);

		if (!StringUtils.isAsciiPrintable(street))
			throw new ValidatorException("Street can contain only letters!", ErrorCode.IS_NOT_OUT_OF_LETTERS);

	}

	public void validateStreetNumber(int streetNumber) throws ValidatorException {

		if (streetNumber <= 0 || streetNumber > 1000)
			throw new ValidatorException("Street Number cannot be 0, negativ or higher than 1000!",
					ErrorCode.WRONG_INTERVAL);
	}

	public void validateApartament(int apartment) throws ValidatorException {

		if (Integer.valueOf(apartment) != null) {
			if (apartment < 0 || apartment > 1000)
				throw new ValidatorException("Street Number cannot be negativ or higher than 1000!",
						ErrorCode.WRONG_INTERVAL);
		}
	}
}
