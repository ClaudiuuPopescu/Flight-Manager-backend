package msg.project.flightmanager.service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import msg.project.flightmanager.enums.RoleEnum;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Permission;
import msg.project.flightmanager.model.Role;
import msg.project.flightmanager.modelHelper.AddPermissionToRoleModel;
import msg.project.flightmanager.repository.PermissionRepository;
import msg.project.flightmanager.repository.RoleRepository;
import msg.project.flightmanager.service.interfaces.IRoleService;

@Service
public class RoleService implements IRoleService {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	
	@Override
	public List<Role> getAll() {
	
		return StreamSupport.stream(this.roleRepository.findAll().spliterator(), false).toList();
	}

	@Transactional
	@Override
	public boolean addRole(String roleEnumLabel) {
		roleEnumLabel.toLowerCase();
		
		RoleEnum roleEnum = RoleEnum.fromLabel(roleEnumLabel);
		
		Optional<Role> optionalRole = this.roleRepository.findByTitle(roleEnumLabel);
		
		if(optionalRole.isPresent()) {
			throw new FlightManagerException(
					HttpStatus.IM_USED,
					MessageFormat.format("Role [{0}] already exists", roleEnumLabel));
		}
			
		Role role = Role.builder()
				.roleEnum(roleEnum)
				.title(roleEnum.getLabel().toUpperCase())
				.build();
		this.roleRepository.save(role);
		return true;
	}

	@Transactional
	@Override
	public boolean addPermissionToRole(AddPermissionToRoleModel addPermissionToRoleModel) {
		Permission permission = this.permissionRepository.findByTitle(addPermissionToRoleModel.getPermissionTitle().toUpperCase())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Adding permission to role failed. Permission with title [{0}] not found", (addPermissionToRoleModel.getPermissionTitle()))));

		Role role = this.roleRepository.findByTitle(addPermissionToRoleModel.getRoleTitle().toUpperCase())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Adding permission to role failed. Can not find role by title [{0}] not found", addPermissionToRoleModel.getRoleTitle())));

		role.getPermissions().add(permission);
		this.roleRepository.save(role);
		return true;
	}

	@Override
	public Role getRoleByTitle(String roleTitle) {
		return this.roleRepository.findByTitle(roleTitle.toUpperCase())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not find role by title [{0}] not found", roleTitle)));
	}
	
	

}
