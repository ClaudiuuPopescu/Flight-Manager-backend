package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import converter.UserConverter;
import dto.UserDto;
import model.User;
import modelHelper.CreateUserModel;
import repository.UserRepository;
import validator.UserValidator;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserValidator userValidator;

	public UserDto createUser(CreateUserModel createUserModel) {
		userValidator.validateCreateUserModel(createUserModel);

		User user = UserConverter.createUserModelToUser(createUserModel);
		user.setActive(false);
		// TODO generate unique username
		String username = generateUsername(createUserModel.getFirstName(), createUserModel.getLastName());
		user.setUsername(username);
		// TODO encrypt password

		return null;
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
		return false; // TODO add logic
	}
}