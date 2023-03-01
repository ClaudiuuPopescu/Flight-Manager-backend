package service;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import enums.PermissionEnum;
import exceptions.FlightManagerException;
import msg.project.flightmanager.model.Permission;
import repository.PermissionRepository;
import service.interfaces.IPermissionService;

public class PermissionService implements IPermissionService {
	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public boolean addPermission(String permissionEnumName) {
		permissionEnumName.toUpperCase();

		try {
			PermissionEnum permissionEnum = PermissionEnum.valueOf(permissionEnumName);

			Permission permission = Permission.builder().permissionEnum(permissionEnum).build();
			this.permissionRepository.save(permission);

			return true;
		} catch (FlightManagerException e) {
			throw new FlightManagerException(HttpStatus.NOT_FOUND,
					MessageFormat.format("Can not add permission. The enum value does not exist", permissionEnumName));
		}
	}

	@Override
	public Permission getPermissionByEnumValue(String enumValue) {
		Permission permission = this.permissionRepository.findByEnum(enumValue)
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Permission with enum [{0}] not found", enumValue)));

		return permission;
	}

}
