package converter;

import model.User;
import modelHelper.CreateUserModel;

public class UserConverter {

	public static User createUserModelToUser(CreateUserModel createUserModel) {

		User user = User.builder().firstName(createUserModel.getFirstName()).lastName(createUserModel.getLastName())
				.email(createUserModel.getEmail()).phoneNumber(createUserModel.getPhoneNumber())
				.birthDate(createUserModel.getBirthDate())
				// TODO sa fac address, create, dto, aleaalea
				.build();
		return user;
	}

}
