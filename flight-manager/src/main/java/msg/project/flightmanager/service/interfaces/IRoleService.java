package msg.project.flightmanager.service.interfaces;

import java.util.List;

import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Role;
import msg.project.flightmanager.modelHelper.AddPermissionToRoleModel;

public interface IRoleService {
	
	List<Role> getAll();

	boolean addRole(String roleEnumLabel) throws FlightManagerException;

	boolean addPermissionToRole(AddPermissionToRoleModel addPermissionToRoleModelModel);
	
	Role getRoleByTitle(String roleTitle); 
}
