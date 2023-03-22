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
import msg.project.flightmanager.modelHelper.EditUserPasswordModel;
import msg.project.flightmanager.modelHelper.UpdateUserRole;

public interface IUserService {

	List<UserDto> getAll();

	boolean createUser(CreateUserModel createUserModel) throws ValidatorException;

	UserDto getByUsername(String username);

	boolean editPersonalDetails(String currentUsername, EditUserModel editUserModel);
	
	boolean editPassword(String currentUsername, EditUserPasswordModel editUserPasswordModel);
	
	boolean editUserRole(UpdateUserRole updateUserRole);

	boolean editUserAddress(String currentUsername, AddressDto addressDto) throws ValidatorException;

	boolean deactivateUser(String deactivateUsername);
	
	boolean activateUser(String activateUsername);
	
	void checkPermission(String token, PermissionEnum permissionTitle) throws RoleException, UserException; 
}
