package validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dto.AddressDto;

@Component
public class AddressValidator {

	public void validateAddress(AddressDto addressDto) throws Exception {

		validateApartament(addressDto.getApartment());
		validateCity(addressDto.getCity());
		validateCountry(addressDto.getCoutry());
		validateStreet(addressDto.getStreet());
		validateStreetNumber(addressDto.getStreetNumber());
	}

	public void validateCountry(String country) throws Exception {

		if(country.isEmpty())
			throw new IllegalAccessException("Country cannot be empty!");
		
		if(!StringUtils.isAsciiPrintable(country))
			throw new Exception("Country can contain only letters!");
		
		if(country.length() < 4)
			throw new Exception("There is no Country with less than 4 characters!");
		
	}

	public void validateCity(String city) throws Exception {

		if(city.isEmpty())
			throw new IllegalAccessException("City cannot be empty!");
		
		if(!StringUtils.isAsciiPrintable(city))
			throw new Exception("City can contain only letters!");
	}

	public void validateStreet(String street) throws Exception {
		
		if(street.isEmpty())
			throw new IllegalAccessException("Street cannot be empty!");
		
		if(!StringUtils.isAsciiPrintable(street))
			throw new Exception("Street can contain only letters!");

	}

	public void validateStreetNumber(int streetNumber) throws IllegalAccessException {


		if(streetNumber <= 0 || streetNumber > 1000)
			throw new IllegalAccessException("Street Number cannot be 0, negativ or higher than 1000!");
	}

	public void validateApartament(int apartment) throws IllegalAccessException {
		
		if(apartment < 0 || apartment > 1000)
			throw new IllegalAccessException("Street Number cannot be negativ or higher than 1000!");
	

	}

}
