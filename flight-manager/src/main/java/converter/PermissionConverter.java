package converter;

import org.springframework.stereotype.Component;

import dto.PermissionDto;
import msg.project.flightmanager.model.Permission;

@Component
public class PermissionConverter implements IConverter<Permission, PermissionDto> {

	@Override
	public PermissionDto convertToDTO(Permission permission) {
		PermissionDto permissionDto = PermissionDto.builder().permissionEnum(permission.getPermissionEnum()).build();
		return permissionDto;
	}

	@Override
	public Permission convertToEntity(PermissionDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
