package service.interfaces;

import java.util.List;

import dto.AddressDto;
import dto.UserDto;
import exceptions.ValidatorException;
import modelHelper.CreateUserModel;
import modelHelper.EditUserModel;

public interface IUserService {

	List<UserDto> getAll();

	boolean createUser(CreateUserModel createUserModel);

	UserDto getByUsername(String username);

	boolean editUserDetails(EditUserModel editUserModel);

	boolean editUserAddress(AddressDto addressDto) throws ValidatorException;

	boolean deactivateUser(String username);
}
