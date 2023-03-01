package service;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import enums.RoleEnum;
import exceptions.FlightManagerException;
import modelHelper.AddPermissionToRoleModel;
import msg.project.flightmanager.model.Permission;
import msg.project.flightmanager.model.Role;
import repository.RoleRepository;
import service.interfaces.IRoleService;

@Service
public class RoleService implements IRoleService {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionService permissionService;

	@Override
	public boolean addRole(String roleEnumLabel) {
		try {
			RoleEnum roleEnum = RoleEnum.fromLabel(roleEnumLabel);
			
			Role role = Role.builder().roleEnum(roleEnum).build();
			this.roleRepository.save(role);

			return true;
		} catch (FlightManagerException e) {
			throw new FlightManagerException(HttpStatus.NOT_FOUND,
					MessageFormat.format("Can not add role. The enum label [{0}] does not exist", roleEnumLabel));
		}
	}

	@Override
	public boolean addPermissionToRole(AddPermissionToRoleModel addPermissionToRoleModel) {
		Permission permission = this.permissionService
				.getPermissionByEnumValue(addPermissionToRoleModel.getPermissionEnumValue());

		RoleEnum roleEnum = RoleEnum.fromLabel(addPermissionToRoleModel.getRoleEnumLabel());

		Role role = this.roleRepository.findByEnum(roleEnum)
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not find role by enum [{0}] not found", roleEnum)));

		role.getPermissions().add(permission);
		return true;
	}

}
