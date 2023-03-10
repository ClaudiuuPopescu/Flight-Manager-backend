package msg.project.flightmanager.service;

import java.text.MessageFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import msg.project.flightmanager.enums.PermissionEnum;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Permission;
import msg.project.flightmanager.repository.PermissionRepository;
import msg.project.flightmanager.service.interfaces.IPermissionService;

@Service
public class PermissionService implements IPermissionService {
	@Autowired
	private PermissionRepository permissionRepository;
	
	@Transactional
	@Override
	public boolean addPermission(String permissionEnumName) {
		permissionEnumName.toLowerCase();

		PermissionEnum permissionEnum = PermissionEnum.fromLabel(permissionEnumName);
		
		Optional<Permission> optionalPermission = this.permissionRepository.findByTitle(permissionEnumName);
		
		if(optionalPermission.isPresent()) {
			throw new FlightManagerException(
					HttpStatus.IM_USED,
					MessageFormat.format("Permission [{0}] already exists", permissionEnumName));
		}

		Permission permission = Permission.builder()
				.permissionEnum(permissionEnum)
				.title(permissionEnum.getLabel().toUpperCase())
				.build();
		this.permissionRepository.save(permission);

		return true;
	}

	@Override
	public Permission getPermissionByTitle(String permissionTitle) {
		Permission permission = this.permissionRepository.findByTitle(permissionTitle.toUpperCase())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Permission with title [{0}] not found", permissionTitle)));

		return permission;
	}

}
