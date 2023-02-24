package model;

import java.sql.Date;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class User {

	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private Date birthDate;
	private boolean isActive;

	@ManyToOne
	@JoinColumn(name = "address_id")
	private Address address;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
}
