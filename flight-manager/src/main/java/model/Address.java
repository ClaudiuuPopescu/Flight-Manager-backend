package model;

import java.util.HashSet;
import java.util.Set;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

public class Address {

	private Long id;
	private String coutry;
	private String city;
	private String street;
	private int streetNumber;
	@Nullable
	private int apartment;

	@OneToMany(mappedBy = "address", orphanRemoval = true, cascade = CascadeType.PERSIST)
	private Set<User> users = new HashSet<>();

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
	@JoinColumn(name = "company_id")
	private Company company;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
	@JoinColumn(name = "airport_id")
	private Airport airport;
}
