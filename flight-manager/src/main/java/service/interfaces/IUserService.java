package service.interfaces;

import java.util.List;

import dto.AddressDto;
import dto.UserDto;
import enums.PermissionEnum;
import exceptions.RoleException;
import exceptions.UserException;
import exceptions.ValidatorException;
import modelHelper.CreateUserModel;
import modelHelper.EditUserModel;

public interface IUserService {

	List<UserDto> getAll();

	boolean createUser(CreateUserModel createUserModel) throws ValidatorException;

	UserDto getByUsername(String username);

	boolean editUserDetails(EditUserModel editUserModel);

	boolean editUserAddress(AddressDto addressDto) throws ValidatorException;

	boolean deactivateUser(String username);
	
	void checkPermission(String token, PermissionEnum permissionEnum) throws RoleException, UserException; 
}
