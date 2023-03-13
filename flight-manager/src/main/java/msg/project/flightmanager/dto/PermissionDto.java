package msg.project.flightmanager.dto;

import lombok.Builder;
import lombok.Getter;
import msg.project.flightmanager.enums.PermissionEnum;

@Getter
@Builder
public class PermissionDto {

	private PermissionEnum permissionEnum;
}
