package converter;

import org.springframework.stereotype.Component;

import dto.UserDto;
import model.User;
import modelHelper.CreateUserModel;

@Component
public class UserConverter implements IConverter<User, UserDto> {

	@Override
	public UserDto convertToDTO(User entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User convertToEntity(UserDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	public User createUserModelToUser(CreateUserModel createUserModel) {

		User user = User.builder().firstName(createUserModel.getFirstName()).lastName(createUserModel.getLastName())
				.email(createUserModel.getEmail()).phoneNumber(createUserModel.getPhoneNumber())
				.birthDate(createUserModel.getBirthDate())
				// TODO sa fac address, create, dto, aleaalea
				.build();
		return user;
	}

}
