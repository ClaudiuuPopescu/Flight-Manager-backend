package msg.project.flightmanager.service.interfaces;

import java.util.List;

import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.dto.UserDto;
import msg.project.flightmanager.enums.PermissionEnum;
import msg.project.flightmanager.exceptions.RoleException;
import msg.project.flightmanager.exceptions.UserException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.modelHelper.CreateUserModel;
import msg.project.flightmanager.modelHelper.EditUserModel;

public interface IUserService {

	List<UserDto> getAll();

	boolean createUser(CreateUserModel createUserModel) throws ValidatorException;

	UserDto getByUsername(String username);

	boolean editUserDetails(EditUserModel editUserModel);
	
	boolean editUserRole(String usernameToEdit, String newRoleTitle);

	boolean editUserAddress(AddressDto addressDto) throws ValidatorException;

	boolean deactivateUser(String username);
	
	void checkPermission(String token, PermissionEnum permissionTitle) throws RoleException, UserException; 
}
