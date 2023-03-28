package msg.project.flightmanager.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.modelHelper.CreateUserModel;
import msg.project.flightmanager.modelHelper.EditUserModel;
import msg.project.flightmanager.repository.UserRepository;
import msg.project.flightmanager.validator.UserValidator;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

	@InjectMocks
	private UserValidator validator;
	@Mock
	private UserRepository repository;
	
	private CreateUserModel createUserModel;
	private EditUserModel editUserModel;
	
	@BeforeEach
	void init() {
		this.createUserModel = new CreateUserModel("passssssword", "fname", "lname", "email@airline.com",
				"0712345678", LocalDate.now(), "role", Mockito.mock(AddressDto.class));
		
		this.editUserModel = new EditUserModel("fname", "lname", "email@airline.com", "0753215607");
	}
	
	@Test
	void validateCreateUserModel_throwsFlightManagerException_whenFirstNameNull() {
		String firstName = null;
		this.createUserModel.setFirstName(firstName);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateCreateUserModel(this.createUserModel));

		assertEquals("First name can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateCreateUserModel(this.createUserModel));
	}
	
	@Test
	void validateCreateUserModel_throwsFlightManagerException_whenFirstNameTooShort() {
		String firstName = "d";
		this.createUserModel.setFirstName(firstName);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateCreateUserModel(this.createUserModel));

		assertEquals("First name requires characters", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateCreateUserModel(this.createUserModel));
	}
	
	@Test
	void validateCreateUserModel_throwsFlightManagerException_whenFirstNameContainsLetters() {
		String firstName = "ame123";
		this.createUserModel.setFirstName(firstName);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateCreateUserModel(this.createUserModel));

		assertEquals("First name can not contain numbers", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateCreateUserModel(this.createUserModel));
	}
	
	@Test
	void validateCreateUserModel_throwsFlightManagerException_whenLastNameNull() {
		String lastName = null;
		this.createUserModel.setLastName(lastName);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateCreateUserModel(this.createUserModel));

		assertEquals("Last name can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateCreateUserModel(this.createUserModel));
	}
	
	@Test
	void validateCreateUserModel_throwsFlightManagerException_whenLastNameTooShort() {
		String lastName = "d";
		this.createUserModel.setLastName(lastName);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateCreateUserModel(this.createUserModel));

		assertEquals("Last name requires characters", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateCreateUserModel(this.createUserModel));
	}
	
	@Test
	void validateCreateUserModel_throwsFlightManagerException_whenLastNameContainsLetters() {
		String lastName = "ame123";
		this.createUserModel.setLastName(lastName);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateCreateUserModel(this.createUserModel));

		assertEquals("Last name can not contain numbers", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateCreateUserModel(this.createUserModel));
	}
	
	@Test
	void validateCreateUserModel_throwsFlightManagerException_whenEmailNotMatchingPattern() {
		String email = "some@invalid.email";
		this.createUserModel.setEmail(email);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateCreateUserModel(this.createUserModel));

		assertEquals("The email entered does not match our convension", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateCreateUserModel(this.createUserModel));
	}
	
	@Test
	void validateCreateUserModel_throwsFlightManagerException_whenEmailTaken() {
		String email = "email@airline.com";
		this.createUserModel.setEmail(email);
		
		User user = Mockito.mock(User.class);
		
		Mockito.when(this.repository.findByEmail(email)).thenReturn(Optional.of(user));
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateCreateUserModel(this.createUserModel));

		assertEquals("The email is already taken", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateCreateUserModel(this.createUserModel));
	}
	
	@Test
	void validateCreateUserModel_throwsFlightManagerException_whenPhoneNumberNotMatchingPattern() {
		String phoneNumber = "1234567890";
		this.createUserModel.setPhoneNumber(phoneNumber);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateCreateUserModel(this.createUserModel));

		assertEquals("The phone number must be romanian", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateCreateUserModel(this.createUserModel));
	}
	
	@Test
	void validateCreateUserModel_throwsFlightManagerException_whenPhoneNumberTaken() {
		String phoneNumber = "0712351257";
		this.createUserModel.setPhoneNumber(phoneNumber);
		
		User user = Mockito.mock(User.class);
		
		Mockito.when(this.repository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateCreateUserModel(this.createUserModel));

		assertEquals("The phone number is already taken", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateCreateUserModel(this.createUserModel));
	}
	
	@Test
	void validateCreateUserModel_throwsFlightManagerException_whenPasswordNull() {
		String password = null;
		this.createUserModel.setPassword(password);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateCreateUserModel(this.createUserModel));

		assertEquals("Password can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateCreateUserModel(this.createUserModel));
	}
	
	@Test
	void validateCreateUserModel_throwsFlightManagerException_whenPasswordTooShort() {
		String password = "short";
		this.createUserModel.setPassword(password);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateCreateUserModel(this.createUserModel));

		assertEquals("Password requires a minimum of 8 characters", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateCreateUserModel(this.createUserModel));
	}
	
	@Test
	void validateCreateUserModel_throwsFlightManagerException_whenBirthUnder18() {
		LocalDate birthday = LocalDate.of(2007, 5, 1);
		this.createUserModel.setBirthDate(birthday);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateCreateUserModel(this.createUserModel));

		assertEquals("User must be over 18yo", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateCreateUserModel(this.createUserModel));
	}
	
	@Test
	void validateCreateUserModel_allConditionsGood() {
		LocalDate birthday = LocalDate.of(2000, 5, 13);
		this.createUserModel.setBirthDate(birthday);
		
		this.validator.validateCreateUserModel(this.createUserModel);
	}
	
	@Test
	void validateEditUserModel_throwsFlightManagerException_whenFirstNameNull() {
		String firstName = null;
		this.editUserModel.setFirstName(firstName);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateEditUserModel(this.editUserModel));

		assertEquals("First name can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateEditUserModel(this.editUserModel));
	}
	
	@Test
	void validateEditUserModel_throwsFlightManagerException_whenFirstNameTooShort() {
		String firstName = "d";
		this.editUserModel.setFirstName(firstName);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateEditUserModel(this.editUserModel));

		assertEquals("First name requires characters", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateEditUserModel(this.editUserModel));
	}
	
	@Test
	void validateEditUserModel_throwsFlightManagerException_whenFirstNameContainsLetters() {
		String firstName = "ame123";
		this.editUserModel.setFirstName(firstName);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateEditUserModel(this.editUserModel));

		assertEquals("First name can not contain numbers", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateEditUserModel(this.editUserModel));
	}
	
	@Test
	void validateEditUserModel_throwsFlightManagerException_whenLastNameNull() {
		String lastName = null;
		this.editUserModel.setLastName(lastName);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateEditUserModel(this.editUserModel));

		assertEquals("Last name can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateEditUserModel(this.editUserModel));
	}
	
	@Test
	void validateEditUserModel_throwsFlightManagerException_whenLastNameTooShort() {
		String lastName = "d";
		this.editUserModel.setLastName(lastName);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateEditUserModel(this.editUserModel));

		assertEquals("Last name requires characters", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateEditUserModel(this.editUserModel));
	}
	
	@Test
	void validateEditUserModel_throwsFlightManagerException_whenLastNameContainsLetters() {
		String lastName = "ame123";
		this.editUserModel.setLastName(lastName);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateEditUserModel(this.editUserModel));

		assertEquals("Last name can not contain numbers", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateEditUserModel(this.editUserModel));
	}
	
	@Test
	void validateEditUserModel_throwsFlightManagerException_whenEmailNotMatchingPattern() {
		String email = "some@invalid.email";
		this.editUserModel.setEmail(email);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateEditUserModel(this.editUserModel));

		assertEquals("The email entered does not match our convension", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateEditUserModel(this.editUserModel));
	}
	
	@Test
	void validateEditUserModel_throwsFlightManagerException_whenEmailTaken() {
		String email = "email@airline.com";
		this.editUserModel.setEmail(email);
		
		User user = Mockito.mock(User.class);
		
		Mockito.when(this.repository.findByEmail(email)).thenReturn(Optional.of(user));
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateEditUserModel(this.editUserModel));

		assertEquals("The email is already taken", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateEditUserModel(this.editUserModel));
	}
	
	@Test
	void validateEditUserModel_throwsFlightManagerException_whenPhoneNumberNotMatchingPattern() {
		String phoneNumber = "1234567890";
		this.editUserModel.setPhoneNumber(phoneNumber);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateEditUserModel(this.editUserModel));

		assertEquals("The phone number must be romanian", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateEditUserModel(this.editUserModel));
	}
	
	@Test
	void validateEditUserModel_throwsFlightManagerException_whenPhoneNumberTaken() {
		String phoneNumber = "0753215607";
		this.createUserModel.setPhoneNumber(phoneNumber);
		
		User user = Mockito.mock(User.class);
		
		Mockito.when(this.repository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.validator.validateEditUserModel(this.editUserModel));

		assertEquals("The phone number is already taken", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.validator.validateEditUserModel(this.editUserModel));
	}
}
