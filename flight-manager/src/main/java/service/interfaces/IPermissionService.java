package service.interfaces;

import exceptions.FlightManagerException;
import msg.project.flightmanager.model.Permission;

public interface IPermissionService {

	boolean addPermission(String permission) throws FlightManagerException;

	Permission getPermissionByEnumValue(String enumValue);
}
