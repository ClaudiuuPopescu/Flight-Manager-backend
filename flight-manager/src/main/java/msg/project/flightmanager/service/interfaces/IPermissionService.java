package msg.project.flightmanager.service.interfaces;

import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Permission;

public interface IPermissionService {

	boolean addPermission(String permission) throws FlightManagerException;

	Permission getPermissionByTitle(String permissionTitle);
}
