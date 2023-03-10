package msg.project.flightmanager.validator;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.dto.CompanyDto;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.ValidatorException;

@Component
public class CompanyValidator {

	private static final String PHONE_NUMBER_REGEX = "^(49|40)?[0-9]{9}$";
	private static final String EMAIL_REGEX = ".*\\b@company.com$";

	@Autowired
	private AddressValidator addressValidator;

	public void validateCompany(CompanyDto companyDto) throws ValidatorException {
		validateName(companyDto.getName());
		validateEmail(companyDto.getEmail());
		validatePhoneNumber(companyDto.getPhoneNumber());
		validateFoundedIn(companyDto.getFoundedIn());
		validateAddress(companyDto.getAddress());
	}

	public void validateAddress(AddressDto addressDto) throws ValidatorException {

		this.addressValidator.validateAddressDto(addressDto);
	}

	public void validateName(String name) throws ValidatorException {

		if (name.isEmpty())
			throw new ValidatorException("Name cannot be empty!", ErrorCode.EMPTY_FIELD);

		if (!StringUtils.isAsciiPrintable(name))
			throw new ValidatorException("Name can contain only letters!", ErrorCode.IS_NOT_OUT_OF_LETTERS);

		if (name.length() < 1 || name.length() > 30)
			throw new ValidatorException("Name can can have the lenght between 1 and 30!", ErrorCode.WRONG_INTERVAL);
	}

	public void validateEmail(String email) throws ValidatorException {
		
		if(email.isEmpty())
			throw new ValidatorException("Email cannot be empty!", ErrorCode.EMPTY_FIELD);

		if (!email.matches(EMAIL_REGEX)) {
			throw new ValidatorException("The email should look like wizzair@company.com!", ErrorCode.WRONG_FROMAT);
		}
		if (email.length() <= 13) {
			throw new ValidatorException("The email is not long enought!", ErrorCode.IS_TOO_SHORT);
		}
	}

	public void validatePhoneNumber(String phoneNumber) throws ValidatorException {

		if(phoneNumber.isEmpty())
			throw new ValidatorException("PhoneNumber cannot be empty!", ErrorCode.EMPTY_FIELD);
		
		if(phoneNumber.length() < 10 || phoneNumber.length() > 10)
			throw new ValidatorException("The phone number should have 10 numbers!", ErrorCode.IS_TOO_SHORT);
		
		if (!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
			throw new ValidatorException("Wrong phone number!", ErrorCode.WRONG_FROMAT);
		}
	}

	public void validateFoundedIn(LocalDate foundedIn) throws ValidatorException {

		LocalDate currentDate = java.time.LocalDate.now();

		if (currentDate.getYear() < foundedIn.getYear())
			throw new ValidatorException("The year should be lower than the current year!", ErrorCode.WRONG_INTERVAL);

		else if (currentDate.getYear() == foundedIn.getYear()) {
			if (currentDate.getMonth().compareTo(foundedIn.getMonth()) == 0) {
				if (currentDate.getDayOfMonth() < foundedIn.getDayOfMonth())
					throw new ValidatorException("The day should be lower than the current day!", ErrorCode.WRONG_INTERVAL);
			} else if (currentDate.getMonth().compareTo(foundedIn.getMonth()) == -1)
				throw new ValidatorException("The month should be lower than the current month!", ErrorCode.WRONG_INTERVAL);
		}
	}

}
