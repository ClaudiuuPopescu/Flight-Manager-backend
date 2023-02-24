package model;

import java.util.HashSet;
import java.util.Set;

import enums.PermissionEnum;
import jakarta.persistence.ManyToMany;

public class Permission {

	private Long id;

	private PermissionEnum permissionEnum;

	@ManyToMany(mappedBy = "permissions")
	private Set<Role> roles = new HashSet<>();

}
