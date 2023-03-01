package dto;

import enums.PermissionEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PermissionDto {

	private PermissionEnum permissionEnum;
}
