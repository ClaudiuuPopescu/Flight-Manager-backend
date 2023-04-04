package msg.project.flightmanager.servicies;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import msg.project.flightmanager.converter.AddressConverter;
import msg.project.flightmanager.converter.UserConverter;
import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.dto.UserDto;
import msg.project.flightmanager.enums.PermissionEnum;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.exceptions.RoleException;
import msg.project.flightmanager.exceptions.UserException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Address;
import msg.project.flightmanager.model.Permission;
import msg.project.flightmanager.model.Role;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.modelHelper.CreateAddressModel;
import msg.project.flightmanager.modelHelper.CreateUserModel;
import msg.project.flightmanager.modelHelper.EditUserModel;
import msg.project.flightmanager.modelHelper.EditUserPasswordModel;
import msg.project.flightmanager.modelHelper.UpdateUserRole;
import msg.project.flightmanager.repository.UserRepository;
import msg.project.flightmanager.service.AddressService;
import msg.project.flightmanager.service.RoleService;
import msg.project.flightmanager.service.TokenService;
import msg.project.flightmanager.service.UserService;
import msg.project.flightmanager.validator.UserValidator;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@InjectMocks
	private UserService service;
	@Mock
	private UserRepository repository;
	@Mock
	private UserValidator validator;
	@Mock
	private UserConverter converter;
	@Mock
	private AddressService addressService;
	@Mock
	private AddressConverter addressConverter;
	@Mock
	private RoleService roleService;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private TokenService tokenService;
	
	CreateUserModel createUserModel;
	
	@BeforeEach
	void init (){
		this.createUserModel = CreateUserModel.builder()
				.password("password")
				.firstName("firstName")
				.lastName("lastName")
				.email("email")
				.phoneNumber("07")
				.birthDate(LocalDate.of(2000, 5, 13))
				.roleTitle("crew")
				.address(Mockito.mock(AddressDto.class))
				.build();
	}
	
	
	@Test
	void getAll_throwsFlightManagerException_whenNoUsersFound() {
		
		Mockito.when(this.repository.findAll()).thenReturn(Collections.emptyList());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.getAll());
		
		assertEquals("No users found", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.getAll());
	}
	
	@Test
	void getAll_returnsListOfUsers_whenUsersExistInDataBase() {
		User first_user = Mockito.mock(User.class);
		User second_user = Mockito.mock(User.class);
		
		Mockito.when(this.repository.findAll()).thenReturn(Arrays.asList(first_user, second_user));
		
		assertEquals(2, this.service.getAll().size());
	}
	
	@Test
	void createUser_throwsFlightManagerException_whenFirstNameNull() {
		String firstName = null;
		
		this.createUserModel.setFirstName(firstName);
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.EXPECTATION_FAILED,
				"First name can not be null"))
		.when(this.validator).validateCreateUserModel(this.createUserModel);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createUser(this.createUserModel));
		
		assertEquals("First name can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createUser(this.createUserModel));
	}

	@Test
	void createUser_throwsFlightManagerException_whenFirstNameLenghtNotAccepted() {
		String firstName = "g";
		
		this.createUserModel.setFirstName(firstName);
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.LENGTH_REQUIRED,
				"First name requires characters"))
		.when(this.validator).validateCreateUserModel(this.createUserModel);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createUser(this.createUserModel));
		
		assertEquals("First name requires characters", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createUser(this.createUserModel));
	}
	
	@Test
	void createUser_throwsFlightManagerException_whenFirstnameInvalidFormat() {
		String firstName = "Jamal123";
		
		this.createUserModel.setFirstName(firstName);
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.FORBIDDEN,
				"First name can not contain numbers"))
		.when(this.validator).validateCreateUserModel(this.createUserModel);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createUser(this.createUserModel));
		
		assertEquals("First name can not contain numbers", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createUser(this.createUserModel));
	}
	//
	@Test
	void createUser_throwsFlightManagerException_whenLastNameNull() {
		String lastName = null;
		
		this.createUserModel.setLastName(lastName);
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.EXPECTATION_FAILED,
				"Last name can not be null"))
		.when(this.validator).validateCreateUserModel(this.createUserModel);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createUser(this.createUserModel));
		
		assertEquals("Last name can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createUser(this.createUserModel));
	}

	@Test
	void createUser_throwsFlightManagerException_whenLastNameLenghtNotAccepted() {
		String lastName = "f";
		
		this.createUserModel.setLastName(lastName);
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.LENGTH_REQUIRED,
				"Last name requires characters"))
		.when(this.validator).validateCreateUserModel(this.createUserModel);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createUser(this.createUserModel));
		
		assertEquals("Last name requires characters", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createUser(this.createUserModel));
	}
	
	@Test
	void createUser_throwsFlightManagerException_whenLastnameInvalidFormat() {
		String lastName = "Orlando123";
		
		this.createUserModel.setLastName(lastName);
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.FORBIDDEN,
				"Last name can not contain numbers"))
		.when(this.validator).validateCreateUserModel(this.createUserModel);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createUser(this.createUserModel));
		
		assertEquals("Last name can not contain numbers", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createUser(this.createUserModel));
	}
	
	@Test
	void createUser_throwsFlightManagerException_whenEmailInvlifRegex() {
		String email = "invalid@email.regex";
		
		this.createUserModel.setEmail(email);
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.EXPECTATION_FAILED,
				"The email entered does not match our convension"))
		.when(this.validator).validateCreateUserModel(this.createUserModel);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createUser(this.createUserModel));
		
		assertEquals("The email entered does not match our convension", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createUser(this.createUserModel));
	}
	
	@Test
	void createUser_throwsFlightManagerException_whenEmailAlreadyUsed() {
		String email = "email@airline.com";
		
		this.createUserModel.setEmail(email);
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.IM_USED,
				"The email is already exists"))
		.when(this.validator).validateCreateUserModel(this.createUserModel);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createUser(this.createUserModel));
		
		assertEquals("The email is already exists", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createUser(this.createUserModel));
	}
	
	@Test
	void createUser_throwsFlightManagerException_whenPhoneNumberNotRomanian() {
		String phoneNumber = "1234567890";
		
		this.createUserModel.setPhoneNumber(phoneNumber);
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.EXPECTATION_FAILED,
				"The phone number must be romanian"))
		.when(this.validator).validateCreateUserModel(this.createUserModel);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createUser(this.createUserModel));
		
		assertEquals("The phone number must be romanian", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createUser(this.createUserModel));
	}
	
	@Test
	void createUser_throwsFlightManagerException_whenPhoneNumberAlreadyUsed() {
		String phoneNumber = "0723589218";
		
		this.createUserModel.setPhoneNumber(phoneNumber);
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.IM_USED,
				"The phone number is already used"))
		.when(this.validator).validateCreateUserModel(this.createUserModel);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createUser(this.createUserModel));
		
		assertEquals("The phone number is already used", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createUser(this.createUserModel));
	}
	
	@Test
	void createUser_throwsFlightManagerException_whenPasswordNull() {
		String password = null;
		
		this.createUserModel.setPassword(password);
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.EXPECTATION_FAILED,
				"Password can not be null"))
		.when(this.validator).validateCreateUserModel(this.createUserModel);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createUser(this.createUserModel));
		
		assertEquals("Password can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createUser(this.createUserModel));
	}

	@Test
	void createUser_throwsFlightManagerException_whenPasswordLenghtNotAccepted() {
		String password = "weakpass";
		
		this.createUserModel.setPassword(password);
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.LENGTH_REQUIRED,
				"Password requires a minimum of 8 characters"))
		.when(this.validator).validateCreateUserModel(this.createUserModel);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createUser(this.createUserModel));
		
		assertEquals("Password requires a minimum of 8 characters", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createUser(this.createUserModel));
	}
	
	@Test
	void createUser_throwsFlightManagerException_whenUserNotOver18() {
		LocalDate birthdate = LocalDate.of(2023, 4, 3);
		
		this.createUserModel.setBirthDate(birthdate);
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.EXPECTATION_FAILED,
				"User must be over 18yo"))
		.when(this.validator).validateCreateUserModel(this.createUserModel);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createUser(this.createUserModel));
		
		assertEquals("User must be over 18yo", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createUser(this.createUserModel));
	}
	
	@Test
	void createUser_returnsTrue_whenAllConditionsGood01WithExistingAddress() throws ParseException, ValidatorException {
		LocalDate birthdate = LocalDate.of(2000, 5, 13);

		
		CreateUserModel createUserModel = CreateUserModel.builder()
				.password("valid-password")
				.firstName("valid-firstName")
				.lastName("valid-lastName")
				.email("email@airline.com")
				.phoneNumber("0712345678")
				.birthDate(birthdate)
				.roleTitle("crew")
				.address(Mockito.mock(AddressDto.class))
				.build();
		
		User user = Mockito.mock(User.class);
		Role crew = Mockito.mock(Role.class);
		Address address = Mockito.mock(Address.class);
		
		Mockito.when(this.converter.createUserModelToUser(createUserModel)).thenReturn(user);
		Mockito.when(this.roleService.getRoleByTitle(createUserModel.getRoleTitle())).thenReturn(crew);
		Mockito.when(this.addressService.getAddressByAllFields(createUserModel.getAddress())).thenReturn(Optional.of(address));
		
		assertTrue(this.service.createUser(createUserModel));
	}
	
	@Test
	void createUser_returnsTrue_whenAllConditionsGood02newAddress() throws ParseException, ValidatorException {
		LocalDate birthdate = LocalDate.of(2000, 5, 13);
		
		CreateUserModel createUserModel = CreateUserModel.builder()
				.password("valid-password")
				.firstName("valid-firstName")
				.lastName("valid-lastName")
				.email("email@airline.com")
				.phoneNumber("0712345678")
				.birthDate(birthdate)
				.roleTitle("crew")
				.address(Mockito.mock(AddressDto.class))
				.build();
		
		User user = Mockito.mock(User.class);
		Role crew = Mockito.mock(Role.class);
		CreateAddressModel addressModel = Mockito.mock(CreateAddressModel.class);
		AddressDto newAddressDto = Mockito.mock(AddressDto.class);
		Address newAddress = Mockito.mock(Address.class);
		
		Mockito.when(this.converter.createUserModelToUser(createUserModel)).thenReturn(user);
		Mockito.when(this.roleService.getRoleByTitle(createUserModel.getRoleTitle())).thenReturn(crew);
		Mockito.when(this.addressService.getAddressByAllFields(createUserModel.getAddress())).thenReturn(Optional.empty());
		Mockito.when(this.addressConverter.converDtoToCreateModel(createUserModel.getAddress())).thenReturn(addressModel);
		Mockito.when(this.addressService.createAddress(addressModel)).thenReturn(newAddressDto);
		Mockito.when(this.addressService.getAddressByAllFields(newAddressDto)).thenReturn(Optional.of(newAddress));
		
		assertTrue(this.service.createUser(createUserModel));
	}
	
	@Test
	void getByUsername_throwsFlightManagerException_whenCantFindUser() {
		String username = "invalidUsername";
		
		Mockito.when(this.repository.findByUsername(username)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.getByUsername(username));
		
		assertEquals(MessageFormat.format("User {0} not found", username), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.getByUsername(username));
	}
	
	@Test
	void getByUsername_returnsTrue_whenUsernameExists() {
		String username = "validUsername";
		
		User user = Mockito.mock(User.class);
		UserDto userDto = Mockito.mock(UserDto.class);
		
		Mockito.when(this.repository.findByUsername(username)).thenReturn(Optional.of(user));
		Mockito.when(this.converter.convertToDTO(user)).thenReturn(userDto);
		
		assertEquals(userDto, this.service.getByUsername(username));
	}
	
	@Test
	void editPersonalDetails_returnTrue_whenChangesDetails() {
		String username = "currentUsername";
		
		EditUserModel editUserModel = new EditUserModel("firstName", "lastName", "em@airline.com", "071352819");
		
		User user = new User();
		user.setFirstName("fn");
		user.setLastName("ln");
		user.setEmail("em");
		user.setPhoneNumber("ps");
		
		Mockito.when(this.repository.findByUsername(username)).thenReturn(Optional.of(user));
		
		assertTrue(this.service.editPersonalDetails(username, editUserModel));
	}
	
	@Test
	void editPersonalDetails_returnsTrue_whenNoChangesFound() {
		String username = "currentUsername";
		
		EditUserModel editUserModel = new EditUserModel("firstName", "lastName", "em@airline.com", "071352819");
		
		User user = new User();
		user.setFirstName("firstName");
		user.setLastName("lastName");
		user.setEmail("em@airline.com");
		user.setPhoneNumber("071352819");
		
		Mockito.when(this.repository.findByUsername(username)).thenReturn(Optional.of(user));
		
		assertTrue(this.service.editPersonalDetails(username, editUserModel));
	}
	
	@Test
	void editPassword_throwsFlightManagerException_whenPasswordsDontMatch() {
		String username = "currentUsername";
		
		EditUserPasswordModel model = new EditUserPasswordModel("invalidCurrentPass", "newPassword");
		
		User user = new User();
		user.setPassword(this.passwordEncoder.encode("anEncodedPassword"));
		
		Mockito.lenient().when(this.repository.findByUsername(username)).thenReturn(Optional.of(user));
		Mockito.lenient().when(this.passwordEncoder.matches(model.getCurrentPassword(), user.getPassword())).thenReturn(false);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.editPassword(username, model));
		
		assertEquals("The password introduced does not match with your current one", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.editPassword(username, model));
	}
	
	@Test
	void editPassword_throwsFlightManagerException_whenNewPasswordNull() {
		String username = "currentUsername";
		
		EditUserPasswordModel model = new EditUserPasswordModel("currentPass", null);
		
		User user = new User();
		user.setPassword(this.passwordEncoder.encode("currentPass"));
		
		Mockito.lenient().when(this.repository.findByUsername(username)).thenReturn(Optional.of(user));
		Mockito.lenient().when(this.passwordEncoder.matches(model.getCurrentPassword(), user.getPassword())).thenReturn(true);
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.EXPECTATION_FAILED,
				"Password can not be null"))
		.when(this.validator).validatePassword(model.getNewPassword());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.editPassword(username, model));
		
		assertEquals("Password can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.editPassword(username, model));
	}
	
	@Test
	void editPassword_throwsFlightManagerException_whenNewPasswordUnder8Characters() {
		String username = "currentUsername";
		
		EditUserPasswordModel model = new EditUserPasswordModel("currentPass", "1234567");
		
		User user = new User();
		user.setPassword(this.passwordEncoder.encode("currentPass"));
		
		Mockito.lenient().when(this.repository.findByUsername(username)).thenReturn(Optional.of(user));
		Mockito.lenient().when(this.passwordEncoder.matches(model.getCurrentPassword(), user.getPassword())).thenReturn(true);
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.LENGTH_REQUIRED,
				"Password requires a minimum of 8 characters"))
		.when(this.validator).validatePassword(model.getNewPassword());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.editPassword(username, model));
		
		assertEquals("Password requires a minimum of 8 characters", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.editPassword(username, model));
	}
	
	@Test
	void editPassword_returnsTrue_whenAllConditionsGood() {
		String username = "currentUsername";
		
		EditUserPasswordModel model = new EditUserPasswordModel("currentPass", "aValidPassword");
		
		User user = new User();
		user.setPassword(this.passwordEncoder.encode("currentPass"));
		
		Mockito.lenient().when(this.repository.findByUsername(username)).thenReturn(Optional.of(user));
		Mockito.lenient().when(this.passwordEncoder.matches(model.getCurrentPassword(), user.getPassword())).thenReturn(true);
		
		assertTrue(this.service.editPassword(username, model));
	}
	
	@Test
	void editUserRole_throwsFlightManagerException_whenCantFindUserToEdit() {
		String username = "invalidUsername";
		
		UpdateUserRole updateUserRole = new UpdateUserRole(username, "newRoleTitle");
		
		Mockito.when(this.repository.findByUsername(username)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.editUserRole(updateUserRole));
		
		assertEquals(MessageFormat
				.format("Can not edit user's role. User {0} not found", updateUserRole.getUsernameToChange()), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.editUserRole(updateUserRole));
	}
	
	@Test
	void editUserRole_throwsFlightManagerException_whenCantFindRole() {
		UpdateUserRole updateUserRole = new UpdateUserRole("username", "newRoleTitle");
		
		User user = Mockito.mock(User.class);
		Role role = Mockito.mock(Role.class);
		
		Mockito.when(this.repository.findByUsername(updateUserRole.getUsernameToChange())).thenReturn(Optional.of(user));
		Mockito.when(this.roleService.getRoleByTitle(updateUserRole.getNewRoleTitle())).thenReturn(role);
		
		assertTrue(this.service.editUserRole(updateUserRole));
	}
	
	@Test
	void editUserAddress_returnsFalse_whenAddressWasNotFound() throws ValidatorException {
		String username = "username";
		
		AddressDto addressDto = Mockito.mock(AddressDto.class);
		User user = Mockito.mock(User.class);
		
		Mockito.when(this.addressService.editAddress(addressDto)).thenReturn(addressDto);
		Mockito.when(this.repository.findByUsername(username)).thenReturn(Optional.of(user));
		Mockito.when(this.addressService.getAddressByAllFields(addressDto)).thenReturn(Optional.empty());
		
		assertFalse(this.service.editUserAddress(username, addressDto));
	}
	
	@Test
	void editUserAddress_returnsTrue_whenAddressEdittedSuccessfully() throws ValidatorException {
		String username = "username";
		
		AddressDto addressDto = Mockito.mock(AddressDto.class);
		User user = Mockito.mock(User.class);
		Address address = Mockito.mock(Address.class);
		
		Mockito.when(this.addressService.editAddress(addressDto)).thenReturn(addressDto);
		Mockito.when(this.repository.findByUsername(username)).thenReturn(Optional.of(user));
		Mockito.when(this.addressService.getAddressByAllFields(addressDto)).thenReturn(Optional.of(address));
		
		assertTrue(this.service.editUserAddress(username, addressDto));
	}
	
	@Test
	void deactivateUser_throwsFlightManagerException_whenCantFindUser(){
		String username = "invalidUsername";
		
		Mockito.when(this.repository.findByUsername(username)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.deactivateUser(username));
		
		assertEquals(MessageFormat.format("Can not deactivate user. User {0} not found", username), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.deactivateUser(username));
	}
	
	@Test
	void deactivateUser_returnsTrue_whenUserDeactivated(){
		String username = "invalidUsername";
		
		User user = Mockito.mock(User.class);
		
		Mockito.when(this.repository.findByUsername(username)).thenReturn(Optional.of(user));
		
		assertTrue(this.service.deactivateUser(username));
	}
	
	@Test
	void activateUser_throwsFlightManagerException_whenCantFindUser(){
		String username = "invalidUsername";
		
		Mockito.when(this.repository.findByUsername(username)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.activateUser(username));
		
		assertEquals(MessageFormat.format("Can not activate user. User {0} not found", username), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.activateUser(username));
	}
	
	@Test
	void activateUser_returnsTrue_whenUserActivated(){
		String username = "invalidUsername";
		
		User user = Mockito.mock(User.class);
		
		Mockito.when(this.repository.findByUsername(username)).thenReturn(Optional.of(user));
		
		assertTrue(this.service.activateUser(username));
	}
	
	@Test
	void checkPermission_throwsUserException_whenCantFindUser() throws RoleException, UserException {
		String token = "token";
		PermissionEnum permission = PermissionEnum.EXPORT_DATA;
		String username = "username";
		
		Mockito.when(this.tokenService.getCurrentUserUsername(token)).thenReturn(username);
		Mockito.when(this.repository.findByUsername(username)).thenReturn(Optional.empty());
		
		UserException thrown = assertThrows(UserException.class,
				() -> this.service.checkPermission(token, permission));
		
		assertEquals(String.format("A user with the username %s does not exist!", username), thrown.getMessage());
		assertThrows(UserException.class, () -> this.service.checkPermission(token, permission));
	}
	
	@Test
	void checkPermission_throwsRoleException_whenRoleDoesntHavePermission() throws RoleException, UserException {
		String token = "token";
		PermissionEnum permission = PermissionEnum.EXPORT_DATA;
		String username = "username";
		
		User user = Mockito.mock(User.class);
		
		Role role = new Role();
		role.setPermissions(Collections.emptySet());
		
		Mockito.when(this.tokenService.getCurrentUserUsername(token)).thenReturn(username);
		Mockito.when(this.repository.findByUsername(username)).thenReturn(Optional.of(user));
		Mockito.when(this.roleService.getRoleByTitle(this.tokenService.getCurrentRol(token))).thenReturn(role);
		
		RoleException thrown = assertThrows(RoleException.class,
				() -> this.service.checkPermission(token, permission));
		
		assertEquals(String.format("The user %s does not have this permission!", username), thrown.getMessage());
		assertThrows(RoleException.class, () -> this.service.checkPermission(token, permission));
	}
	
	@Test
	void checkPermission_returnsVoid_whenUserHasPermissions() throws RoleException, UserException {
		String token = "token";
		PermissionEnum permissionEnum = PermissionEnum.EXPORT_DATA;
		String username = "username";
		
		Permission permission = new Permission();
		permission.setPermissionEnum(permissionEnum);
		
		User user = Mockito.mock(User.class);
		
		Role role = new Role();
		role.getPermissions().add(permission);
		
		Mockito.when(this.tokenService.getCurrentUserUsername(token)).thenReturn(username);
		Mockito.when(this.repository.findByUsername(username)).thenReturn(Optional.of(user));
		Mockito.when(this.roleService.getRoleByTitle(this.tokenService.getCurrentRol(token))).thenReturn(role);
		
		assertDoesNotThrow(() -> this.service.checkPermission(token, permissionEnum));
	}
}
