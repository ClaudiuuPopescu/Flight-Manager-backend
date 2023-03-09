package service;

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

import converter.UserConverter;
import dto.AddressDto;
import dto.UserDto;
import enums.PermissionEnum;
import enums.RoleEnum;
import exceptions.ErrorCode;
import exceptions.FlightManagerException;
import exceptions.RoleException;
import exceptions.UserException;
import exceptions.ValidatorException;
import jakarta.transaction.Transactional;
import modelHelper.CreateUserModel;
import modelHelper.EditUserModel;
import msg.project.flightmanager.model.Permission;
import msg.project.flightmanager.model.Role;
import msg.project.flightmanager.model.User;
import repository.UserRepository;
import service.interfaces.IUserService;
import validator.UserValidator;

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

		List<UserDto> usersDto = users.stream().map(this.userConverter::convertToDTO).collect(Collectors.toList());
		return usersDto;
	}

	@Transactional
	@Override
	public boolean createUser(CreateUserModel createUserModel) {
		// TODO sa aiba permisiuni
		// TODO sa i se atribuie adresa, daca exista deja adresa, pe aia, daca nu, noua

		this.userValidator.validateCreateUserModel(createUserModel);

		User user = this.userConverter.createUserModelToUser(createUserModel);

		String username = generateUsername(createUserModel.getFirstName(), createUserModel.getLastName());
		user.setUsername(username);

		String encodedPass = this.passwordEncoder.encode(createUserModel.getPassword());
		user.setPassword(encodedPass);

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
	public boolean editUserDetails(EditUserModel editUserModel) {
		// TODO verificare daca current user ii acelasi cu cel pe care vrea sa il
		// schimbe
		// de facut dupa ce se face Login ul
		// altfel ar trebui sa primesc in EditUserModel si user ul care face schimbarea
		// ca sa vad daca are voie; usernameEditing

		User userToEdit = this.userRepository.findByUsername(editUserModel.getUsernameToEdit())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND, MessageFormat
						.format("Can not edit user. User {0} not found", editUserModel.getUsernameToEdit())));

		checkDifferencesAndSetValues(editUserModel, userToEdit);

		this.userRepository.save(userToEdit);

		return true;
	}

	@Transactional
	@Override
	public boolean editUserAddress(AddressDto addressDto) throws ValidatorException {
		this.addressService.editAddress(addressDto);

		// TODO sa vedem cum luam userul de la care modificam adresa
		//
		return false;
	}

	@Transactional
	@Override
	public boolean deactivateUser(String username) {
		User user = this.userRepository.findByUsername(username)
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not deactivate user. User {0} not found", username)));

		user.setActive(false);
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
		this.userValidator.validateFirstName(editUserModel.getFirstName());
		this.userValidator.validateLastName(editUserModel.getLastName());
		userToEdit.setFirstName(editUserModel.getFirstName());
		userToEdit.setLastName(editUserModel.getLastName());

		if (!editUserModel.getEmail().equals(userToEdit.getEmail())) {
			this.userValidator.validateEmail(editUserModel.getEmail());
			userToEdit.setEmail(editUserModel.getEmail());
		}

		if (!editUserModel.getPhoneNumber().equals(editUserModel.getPhoneNumber())) {
			this.userValidator.validatePhoneNumber(editUserModel.getPhoneNumber());
			userToEdit.setPhoneNumber(editUserModel.getPhoneNumber());
		}

	}

	@Override
	public void checkPermission(String token, PermissionEnum permissionEnum) throws RoleException, UserException {

		final String userName = this.tokenService.getCurrentUserUsername(token);
		Optional<User> user = this.userRepository.findByUsername(userName);
		if(user.isEmpty())
			throw new UserException(String.format("A user with the username %d does not exist!", userName), ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);
		final RoleEnum roleEnum = RoleEnum.fromLabel(this.tokenService.getCurrentRol(token));
		Role role = this.roleService.getRoleByEnum(roleEnum);
		Set<Permission> permissionOfRole = role.getPermissions();

		List<Permission> permissionExistence = permissionOfRole.stream().filter(permission -> permission.getPermissionEnum().equals(permissionEnum))
				.collect(Collectors.toList());
		
		if(permissionExistence.isEmpty())
			throw new RoleException(String.format("The user %d does not have this permission!", userName), ErrorCode.DOES_NOT_HAVE_PERMISSION);

	}
}
