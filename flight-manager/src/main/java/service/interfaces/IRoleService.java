package service.interfaces;

import java.util.List;

import enums.RoleEnum;
import exceptions.FlightManagerException;
import exceptions.RoleException;
import modelHelper.AddPermissionToRoleModel;
import msg.project.flightmanager.model.Role;

public interface IRoleService {

	boolean addRole(String roleEnumLabel) throws FlightManagerException;

	boolean addPermissionToRole(AddPermissionToRoleModel addPermissionToRoleModelModel);

	List<Role> getAll();
	
	Role getRoleByEnum(RoleEnum roleEnum) throws RoleException;
	
}
