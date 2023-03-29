package msg.project.flightmanager.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import msg.project.flightmanager.dto.UserDto;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.modelHelper.CreateUserModel;

@Component
public class UserConverter implements IConverter<User, UserDto> {
	@Autowired
	private AddressConverter addressConverter;
	@Autowired
	private CompanyConverter companyConverter;
	@Autowired
	private RoleConverter roleConverter;

	@Override
	public UserDto convertToDTO(User user) {
		UserDto userDto = UserDto.builder()
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.username(user.getUsername())
				.email(user.getEmail())
				.phoneNumber(user.getPhoneNumber())
				.birthDate(user.getBirthDate())
				.address(this.addressConverter.convertToDTO(user.getAddress()))
				.role(this.roleConverter.convertToDTO(user.getRole()))
				.company(this.companyConverter.convertToDTO(user.getCompany())).build();
		return userDto;
	}

	@Override
	public User convertToEntity(UserDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	public User createUserModelToUser(CreateUserModel createUserModel) {

		User user = User.builder()
				.firstName(createUserModel.getFirstName())
				.lastName(createUserModel.getLastName())
				.email(createUserModel.getEmail())
				.phoneNumber(createUserModel.getPhoneNumber())
				.birthDate(createUserModel.getBirthDate())
				.build();
		return user;
	}

}
