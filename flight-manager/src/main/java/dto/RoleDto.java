package dto;

import java.util.Set;

import enums.RoleEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoleDto {

	private RoleEnum roleEnum;
	private String label;
	private Set<PermissionDto> permissions;
	private Set<String> usernames;
}
