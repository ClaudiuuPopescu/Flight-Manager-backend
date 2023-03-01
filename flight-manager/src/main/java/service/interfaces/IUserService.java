package service.interfaces;

import java.util.List;

import dto.UserDto;
import modelHelper.CreateUserModel;
import modelHelper.EditUserModel;

public interface IUserService {

	List<UserDto> getAll();

	UserDto createUser(CreateUserModel createUserModel);

	UserDto getByUsername(String username);

	UserDto editUserDetails(EditUserModel editUserModel);

	UserDto editUserAddress();

	boolean deactivateUser(String username);
}
