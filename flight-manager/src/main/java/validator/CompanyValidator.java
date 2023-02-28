package validator;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dto.AddressDto;
import dto.CompanyDto;

@Component
public class CompanyValidator {

	private static final String PHONE_NUMBER_REGEX = "^(49|40)?[0-9]{9}$";
	private static final String EMAIL_REGEX = ".*\\b@company.com$";

	@Autowired
	private AddressValidator addressValidator;

	public void validateCompany(CompanyDto companyDto) throws Exception {
		validateName(companyDto.getName());
		validateEmail(companyDto.getEmail());
		validatePhoneNumber(companyDto.getPhoneNumber());
		validateFoundedIn(companyDto.getFoundedIn());
		validateAddress(companyDto.getAddress());
	}

	public void validateAddress(AddressDto addressDto) throws Exception {

		addressValidator.validateAddress(addressDto);
	}

	public void validateName(String name) throws Exception {

		if (name.isEmpty())
			throw new IllegalAccessException("Name cannot be empty!");

		if (!StringUtils.isAsciiPrintable(name))
			throw new Exception("Name can contain only letters!");

		if (name.length() < 1 || name.length() > 30)
			throw new Exception("Name can can have the lenght between 1 and 30!");
	}

	public void validateEmail(String email) throws Exception {
		
		if(email.isEmpty())
			throw new IllegalAccessException("Email cannot be empty!");

		if (!email.matches(EMAIL_REGEX)) {
			throw new Exception("The email should look like wizzair@company.com!");
		}
		if (email.length() <= 13) {
			throw new Exception("The email is not long enought!");
		}
	}

	public void validatePhoneNumber(String phoneNumber) throws Exception {

		if(phoneNumber.isEmpty())
			throw new IllegalAccessException("PhoneNumber cannot be empty!");
		
		if(phoneNumber.length() < 10 || phoneNumber.length() > 10)
			throw new Exception("The phone number should have 10 numbers!");
		
		if (!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
			throw new Exception("Wrong phone number!");
		}
	}

	public void validateFoundedIn(LocalDate foundedIn) {

		LocalDate currentDate = java.time.LocalDate.now();

		if (currentDate.getYear() < foundedIn.getYear())
			throw new IllegalArgumentException("The year should be lower than the current year!");

		else if (currentDate.getYear() == foundedIn.getYear()) {
			if (currentDate.getMonth().compareTo(foundedIn.getMonth()) == 0) {
				if (currentDate.getDayOfMonth() > foundedIn.getDayOfMonth())
					throw new IllegalArgumentException("The day should be lower than the current day!");
			} else if (currentDate.getMonth().compareTo(foundedIn.getMonth()) == -1)
				throw new IllegalArgumentException("The month should be lower than the current month!");
		}
	}

}
