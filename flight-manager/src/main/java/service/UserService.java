package service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import converter.UserConverter;
import dto.UserDto;
import exceptions.FlightManagerException;
import modelHelper.CreateUserModel;
import msg.project.flightmanager.model.User;
import repository.UserRepository;
import validator.UserValidator;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private UserConverter userConverter;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<UserDto> getAll() {
		List<User> users = StreamSupport.stream(userRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());

		if (users.isEmpty()) {
			throw new FlightManagerException(HttpStatus.NO_CONTENT, "No users found");
		}

		List<UserDto> usersDto = users.stream().map(user -> userConverter.convertToDTO(user))
				.collect(Collectors.toList());
		return usersDto;
	}

	public UserDto createUser(CreateUserModel createUserModel) {
		// TODO sa aiba permisiuni
		// TODO sa i se atribuie adresa, daca exista deja adresa, pe aia, daca nu, noua

		userValidator.validateCreateUserModel(createUserModel);

		User user = userConverter.createUserModelToUser(createUserModel);
		user.setActive(true);

		String username = generateUsername(createUserModel.getFirstName(), createUserModel.getLastName());
		user.setUsername(username);

		String encodedPass = passwordEncoder.encode(createUserModel.getPassword());
		user.setPassword(encodedPass);

		userRepository.save(user);

		UserDto userDto = userConverter.convertToDTO(user);
		return userDto;
	}

	public UserDto getByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("User {0} not found", username)));

		UserDto userDto = userConverter.convertToDTO(user);
		return userDto;
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
		Optional<User> user = userRepository.findByUsername(username);

		return user.isEmpty() ? true : false;
	}
}
