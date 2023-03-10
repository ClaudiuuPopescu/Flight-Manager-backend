package msg.project.flightmanager.service.interfaces;

import java.util.List;

import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Permission;

public interface IPermissionService {
	
	List<Permission> getAll();

	boolean addPermission(String permission) throws FlightManagerException;

	Permission getPermissionByTitle(String permissionTitle);
}
