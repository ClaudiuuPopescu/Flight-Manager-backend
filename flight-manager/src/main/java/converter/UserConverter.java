package converter;

import org.springframework.stereotype.Component;

import model.User;
import modelHelper.CreateUserModel;

@Component
public class UserConverter {

	public User createUserModelToUser(CreateUserModel createUserModel) {

		User user = User.builder().firstName(createUserModel.getFirstName()).lastName(createUserModel.getLastName())
				.email(createUserModel.getEmail()).phoneNumber(createUserModel.getPhoneNumber())
				.birthDate(createUserModel.getBirthDate())
				// TODO sa fac address, create, dto, aleaalea
				.build();
		return user;
	}

}
