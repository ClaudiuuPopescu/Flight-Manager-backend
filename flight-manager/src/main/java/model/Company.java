package model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

public class Company {

	private Long id;
	private String name;
	private String phoneNumber;
	private String email;
	private Date foundedIn;

	@OneToOne
	@JoinColumn(name = "address_id")
	private Address address;

	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
	private Set<Plane> planes = new HashSet<>();

	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
	private Set<User> employees = new HashSet<>();
}
