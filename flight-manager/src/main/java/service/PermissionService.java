package service;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import enums.PermissionEnum;
import exceptions.FlightManagerException;
import msg.project.flightmanager.model.Permission;
import repository.PermissionRepository;
import service.interfaces.IPermissionService;

@Service
public class PermissionService implements IPermissionService {
	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public boolean addPermission(String permissionEnumName) {
		permissionEnumName.toUpperCase();

		PermissionEnum permissionEnum = PermissionEnum.fromLabel(permissionEnumName);

		Permission permission = Permission.builder().permissionEnum(permissionEnum).build();
		this.permissionRepository.save(permission);

		return true;
	}

	@Override
	public Permission getPermissionByEnumValue(String enumValue) {
		Permission permission = this.permissionRepository.findByEnum(enumValue)
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Permission with enum [{0}] not found", enumValue)));

		return permission;
	}

	@Override
	public List<Permission> getAll() {
		
		return this.permissionRepository.getAll();
	}

}
