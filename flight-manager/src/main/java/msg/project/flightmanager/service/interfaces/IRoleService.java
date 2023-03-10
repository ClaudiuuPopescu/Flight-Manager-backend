package msg.project.flightmanager.service.interfaces;

import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.modelHelper.AddPermissionToRoleModel;

public interface IRoleService {

	boolean addRole(String roleEnumLabel) throws FlightManagerException;

	boolean addPermissionToRole(AddPermissionToRoleModel addPermissionToRoleModelModel);
}
