package validator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import modelHelper.CreateUserModel;

@Component
public class UserValidator {

	public void validateCreateUserModel(CreateUserModel createUserModel) {
		validateFirstName(createUserModel.getFirstName());
		validateLasttName(createUserModel.getLastName());
		validateEmail(createUserModel.getEmail());
		validatePhoneNumber(createUserModel.getPhoneNumber());
		validatePassword(createUserModel.getPassword());
		validateBithDate(createUserModel.getBirthDate());
	}

	public void validateFirstName(String firstName) {

		if (firstName == null) {
			// TODO throw exception
		}

		if (firstName.length() < 2) {
			// TODO throw exception
		}
	}

	public void validateLasttName(String lastName) {

		if (lastName == null) {
			// TODO throw exception
		}

		if (lastName.length() < 2) {
			// TODO throw exception
		}
	}

	public void validateEmail(String email) {
		// pattern: [email]@airline.com
		String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@airline.com$";

		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(email);

		if (!matcher.matches()) {
			// TODO throw exception
		}
	}

	public void validatePhoneNumber(String phoneNumber) {
		String regexRo = "^(\\+4|)?(07[0-8]{1}[0-9]{1}|02[0-9]{2}|03[0-9]{2}){1}?(\\s|\\.|\\-)?([0-9]{3}(\\s|\\.|\\-|)){2}$";

		Pattern pattern = Pattern.compile(regexRo);
		Matcher matcher = pattern.matcher(phoneNumber);

		if (!matcher.matches()) {
			// TODO throw exception
		}
	}

	public void validatePassword(String password) {

		if (password == null) {
			// TODO throw exception
		}

		if (password.length() < 8) {
			// TODO throw exception
		}
	}

	public void validateBithDate(Date birthDay) {

		Date now = new Date();

		LocalDate localNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate localBirthDate = birthDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if ((localNow.getYear() - localBirthDate.getYear()) < 18) {
			// TODO throw exception
		}

	}

}
