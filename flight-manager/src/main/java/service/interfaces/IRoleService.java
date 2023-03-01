package service.interfaces;

import exceptions.FlightManagerException;
import modelHelper.AddPermissionToRoleModel;

public interface IRoleService {

	boolean addRole(String roleEnumLabel) throws FlightManagerException;

	boolean addPermissionToRole(AddPermissionToRoleModel addPermissionToRoleModelModel);
}
