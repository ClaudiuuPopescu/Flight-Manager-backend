package service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import enums.RoleEnum;
import exceptions.ErrorCode;
import exceptions.FlightManagerException;
import exceptions.RoleException;
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
		roleEnumLabel.toLowerCase();
		RoleEnum roleEnum = RoleEnum.fromLabel(roleEnumLabel);
			
		Role role = Role.builder().roleEnum(roleEnum).build();
		this.roleRepository.save(role);
		return true;
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
		this.roleRepository.save(role);
		return true;
	}

	@Override
	public List<Role> getAll() {
	
		return this.roleRepository.getAll();
	}

	@Override
	public Role getRoleByEnum(RoleEnum roleEnum) throws RoleException {
		
		Optional<Role> role = this.roleRepository.findByEnum(roleEnum);
		if(role.isPresent())
			return role.get();
		else
			throw new RoleException("There is no role witn this Enum!", ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);
	}



}
