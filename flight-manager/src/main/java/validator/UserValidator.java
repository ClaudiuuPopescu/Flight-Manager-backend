package validator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import exceptions.FlightManagerException;
import modelHelper.CreateUserModel;
import modelHelper.EditUserModel;
import msg.project.flightmanager.model.User;
import repository.UserRepository;

@Component
public class UserValidator {
	@Autowired
	private UserRepository userRepository;

	public void validateCreateUserModel(CreateUserModel createUserModel) {
		validateFirstName(createUserModel.getFirstName());
		validateLastName(createUserModel.getLastName());
		validateEmail(createUserModel.getEmail());
		validatePhoneNumber(createUserModel.getPhoneNumber());
		validatePassword(createUserModel.getPassword());
		validateBithDate(createUserModel.getBirthDate());
	}

	public void validateEditUserModel(EditUserModel editUserModel) {
		validateLastName(editUserModel.getLastName());
		validateEmail(editUserModel.getEmail());
		validatePhoneNumber(editUserModel.getPhoneNumber());
	}

	public void validateFirstName(String firstName) {

		if (firstName == null) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "First name can not be null");
		}

		if (firstName.length() < 2) {
			throw new FlightManagerException(HttpStatus.LENGTH_REQUIRED, "First name requires characters");
		}

		if (firstName.chars().anyMatch(Character::isDigit)) {
			throw new FlightManagerException(HttpStatus.FORBIDDEN, "First name can not contain numbers");
		}
	}

	public void validateLastName(String lastName) {

		if (lastName == null) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "Last name can not be null");
		}

		if (lastName.length() < 2) {
			throw new FlightManagerException(HttpStatus.LENGTH_REQUIRED, "Last name requires characters");
		}

		if (lastName.chars().anyMatch(Character::isDigit)) {
			throw new FlightManagerException(HttpStatus.FORBIDDEN, "Last name can not contain numbers");
		}
	}

	public void validateEmail(String email) {
		// pattern: [email]@airline.com
		String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@airline.com$";

		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(email);

		if (!matcher.matches()) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED,
					"The email entered does not match our convension");
		}

		Optional<User> user = this.userRepository.findByEmail(email);

		if (user.isPresent()) {
			throw new FlightManagerException(HttpStatus.IM_USED, "The email is already exists");
		}

	}

	public void validatePhoneNumber(String phoneNumber) {
		String regexRo = "^(\\+4|)?(07[0-8]{1}[0-9]{1}|02[0-9]{2}|03[0-9]{2}){1}?(\\s|\\.|\\-)?([0-9]{3}(\\s|\\.|\\-|)){2}$";

		Pattern pattern = Pattern.compile(regexRo);
		Matcher matcher = pattern.matcher(phoneNumber);

		if (!matcher.matches()) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "The phone number must be romanian");
		}

		Optional<User> user = this.userRepository.findByPhoneNumber(phoneNumber);

		if (user.isPresent()) {
			throw new FlightManagerException(HttpStatus.IM_USED, "The phone number is already exists");
		}
	}

	public void validatePassword(String password) {

		if (password == null) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "Password can not be null");
		}

		if (password.length() < 8) {
			throw new FlightManagerException(HttpStatus.LENGTH_REQUIRED, "Password requires a minimum of 8 characters");
		}
	}

	public void validateBithDate(Date birthDay) {

		Date now = new Date();

		LocalDate localNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate localBirthDate = birthDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if ((localNow.getYear() - localBirthDate.getYear()) < 18) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "User must be over 18yo");
		}

	}

}
