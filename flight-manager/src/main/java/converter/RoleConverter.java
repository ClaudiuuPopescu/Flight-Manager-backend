package converter;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dto.RoleDto;
import msg.project.flightmanager.model.Role;

@Component
public class RoleConverter implements IConverter<Role, RoleDto> {
	@Autowired
	private PermissionConverter permissionConverter;

	@Override
	public RoleDto convertToDTO(Role role) {
		RoleDto roleDto = RoleDto.builder().roleEnum(role.getRoleEnum()).label(role.getLabel())
				.permissions(role.getPermissions().stream()
						.map(permission -> permissionConverter.convertToDTO(permission)).collect(Collectors.toSet()))
				.build();
		return roleDto;
	}

	@Override
	public Role convertToEntity(RoleDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
