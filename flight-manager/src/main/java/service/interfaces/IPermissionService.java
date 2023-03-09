package service.interfaces;

import java.util.List;

import exceptions.FlightManagerException;
import msg.project.flightmanager.model.Permission;

public interface IPermissionService {

	boolean addPermission(String permission) throws FlightManagerException;

	Permission getPermissionByEnumValue(String enumValue);
	
	List<Permission> getAll();
}
