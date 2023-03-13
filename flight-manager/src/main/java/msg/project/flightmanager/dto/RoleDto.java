package msg.project.flightmanager.dto;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import msg.project.flightmanager.enums.RoleEnum;

@Getter
@Builder
public class RoleDto {

	private RoleEnum roleEnum;
	private String label;
	private Set<PermissionDto> permissions;
	private Set<String> usernames;
}
