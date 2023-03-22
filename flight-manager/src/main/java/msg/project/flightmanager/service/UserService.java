package msg.project.flightmanager.service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import msg.project.flightmanager.converter.AddressConverter;
import msg.project.flightmanager.converter.UserConverter;
import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.dto.UserDto;
import msg.project.flightmanager.enums.PermissionEnum;
import msg.project.flightmanager.exceptions.ErrorCode;
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
import msg.project.flightmanager.service.interfaces.IUserService;
import msg.project.flightmanager.validator.UserValidator;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private UserConverter userConverter;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AddressService addressService;
	@Autowired
	private AddressConverter addressConverter;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private RoleService roleService;

	@Override
	public List<UserDto> getAll() {
		List<User> users = StreamSupport.stream(this.userRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());

		if (users.isEmpty()) {
			throw new FlightManagerException(HttpStatus.NO_CONTENT, "No users found");
		}

		List<UserDto> usersDto = users.stream().map(this.userConverter::convertToDTO).toList();
		return usersDto;
	}

	@Transactional
	@Override
	public boolean createUser(CreateUserModel createUserModel) throws ValidatorException {
		
		this.userValidator.validateCreateUserModel(createUserModel);

		User user = this.userConverter.createUserModelToUser(createUserModel);

		String username = generateUsername(createUserModel.getFirstName(), createUserModel.getLastName());
		user.setUsername(username);

		String encodedPass = this.passwordEncoder.encode(createUserModel.getPassword());
		user.setPassword(encodedPass);
		
		Role role = this.roleService.getRoleByTitle(createUserModel.getRoleTitle().toLowerCase());
		user.setRole(role);

		Optional<Address> optionalAddress = this.addressService.getAddressByAllFields(createUserModel.getAddress());

		if (optionalAddress.isEmpty()) {
			// if the address does not match another address with all the fields, create new
			// one
			CreateAddressModel createAddressModel = this.addressConverter.converDtoToCreateModel(createUserModel.getAddress());
			AddressDto addressDto = this.addressService.createAddress(createAddressModel);
			Address address = this.addressService.getAddressByAllFields(addressDto).get();
			user.setAddress(address);
			
		} else {
			
			// if the address matches another address based on all the fields, assign that
			user.setAddress(optionalAddress.get());
		}


		this.userRepository.save(user);
		return true;
	}

	@Override
	public UserDto getByUsername(String username) {
		User user = this.userRepository.findByUsername(username)
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("User {0} not found", username)));

		UserDto userDto = this.userConverter.convertToDTO(user);
		return userDto;
	}

	@Transactional
	@Override
	public boolean editPersonalDetails(String currentUsername, EditUserModel editUserModel) {

		User user = this.userRepository.findByUsername(currentUsername).get();

		checkDifferencesAndSetValues(editUserModel, user);

		this.userRepository.save(user);

		return true;
	}
	
	@Transactional
	@Override
	public boolean editPassword(String currentUsername, EditUserPasswordModel editUserPasswordModel) {
		
		User user = this.userRepository.findByUsername(currentUsername).get();
		
		if(!this.passwordEncoder.matches(editUserPasswordModel.getCurrentPassword(), user.getPassword())) {
			throw new FlightManagerException(
					HttpStatus.FORBIDDEN,
					"The password introduced does not match with your current one");
		}
		
		this.userValidator.validatePassword(editUserPasswordModel.getNewPassword());
		
		String encodeNewPass = this.passwordEncoder.encode(editUserPasswordModel.getNewPassword());
		user.setPassword(encodeNewPass);
		return true;
	}
	
	@Transactional
	@Override
	public boolean editUserRole(UpdateUserRole updateUserRole) {
		
		User userToEdit = this.userRepository.findByUsername(updateUserRole.getUsernameToChange())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat
						.format("Can not edit user's role. User {0} not found", (updateUserRole.getUsernameToChange()))));
		
		Role role =  this.roleService.getRoleByTitle(updateUserRole.getNewRoleTitle());
		userToEdit.setRole(role);
		
		this.userRepository.save(userToEdit);
		return true;
	}

	@Transactional
	@Override
	public boolean editUserAddress(String currentUsername, AddressDto addressDto) throws ValidatorException {
		AddressDto address = this.addressService.editAddress(addressDto);
		
		Optional<Address> optionalAddress = this.addressService.getAddressByAllFields(address);;
		
		User currentUser = this.userRepository.findByUsername(currentUsername).get();
		
		if(optionalAddress.isPresent()) {
			currentUser.setAddress(optionalAddress.get());
			this.userRepository.save(currentUser);
			return true;
		}	
		
		return false;
	}

	@Transactional
	@Override
	public boolean deactivateUser(String deactivateUsername) {
		User user = this.userRepository.findByUsername(deactivateUsername)
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not deactivate user. User {0} not found", deactivateUsername)));
	
		user.setActive(false);
		this.userRepository.save(user);
		return true;
	}
	
	@Transactional
	@Override
	public boolean activateUser(String activateUsername) {
		User user = this.userRepository.findByUsername(activateUsername)
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not activate user. User {0} not found", activateUsername)));

		user.setActive(true);
		this.userRepository.save(user);
		return true;
	}

	private String generateUsername(String firstName, String lastName) {
		String username = (firstName.substring(0, 1) + lastName).toLowerCase();

		boolean usernameExists = checkIfUsernameExists(username);
		if (!usernameExists) {
			return username;
		}

		int num = 1;
		while (true) {
			String newUsername = username + num;
			boolean newUsernameExists = checkIfUsernameExists(newUsername);
			if (!newUsernameExists) {
				return newUsername;
			}
			num++;
		}
	}

	private boolean checkIfUsernameExists(String username) {
		Optional<User> user = this.userRepository.findByUsername(username);

		return user.isPresent();
	}

	private void checkDifferencesAndSetValues(EditUserModel editUserModel, User userToEdit) {
		if (!userToEdit.getFirstName().equals(editUserModel.getFirstName())) {
			this.userValidator.validateFirstName(editUserModel.getFirstName());
			userToEdit.setFirstName(editUserModel.getFirstName());
		}

		if (!userToEdit.getLastName().equals(editUserModel.getLastName())) {
			this.userValidator.validateLastName(editUserModel.getLastName());
			userToEdit.setLastName(editUserModel.getLastName());
		}

		if (!userToEdit.getEmail().equals(editUserModel.getEmail())) {
			this.userValidator.validateEmail(editUserModel.getEmail());
			userToEdit.setEmail(editUserModel.getEmail());
		}

		if (!userToEdit.getPhoneNumber().equals(editUserModel.getPhoneNumber())) {
			this.userValidator.validatePhoneNumber(editUserModel.getPhoneNumber());
			userToEdit.setPhoneNumber(editUserModel.getPhoneNumber());
		}

	}

	@Override
	public void checkPermission(String token, PermissionEnum permissionTitle) throws RoleException, UserException {

		final String userName = this.tokenService.getCurrentUserUsername(token);
		
		Optional<User> user = this.userRepository.findByUsername(userName);
		
		if(user.isEmpty()) {
			throw new UserException(String.format("A user with the username %s does not exist!", userName), ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);			
		}
		
		Role role = this.roleService.getRoleByTitle(this.tokenService.getCurrentRol(token));
		
		Set<Permission> permissionOfRole = role.getPermissions();

		List<Permission> permissionExistence = permissionOfRole.stream().filter(permission -> permission.getPermissionEnum().equals(permissionTitle))
				.collect(Collectors.toList());
		
		if(permissionExistence.isEmpty())
			throw new RoleException(String.format("The user %s does not have this permission!", userName), ErrorCode.DOES_NOT_HAVE_PERMISSION);

	}
}
