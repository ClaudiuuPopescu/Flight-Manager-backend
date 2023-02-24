package model;

import java.util.HashSet;
import java.util.Set;

import enums.RoleEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

public class Role {

	private Long id;
	private RoleEnum roleEnum;
	private String label;

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name = "Role_Permission", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Set<Permission> permissions = new HashSet<>();

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	private Set<User> user = new HashSet<>();

}
