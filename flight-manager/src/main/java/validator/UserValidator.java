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
		// pattern: [email]@msggroup.com
		String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@msggroup.com$";

		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(email);

		if (!matcher.matches()) {
			// TODO throw exception
		}
	}

	public void validatePhoneNumber(String phoneNumber) {
		// TODO sa vad aici ce criterii sa fie
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
