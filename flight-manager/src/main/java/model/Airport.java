package model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

public class Airport {

	private Long id;
	private String name;
	private String timeZone;

	@OneToOne
	@JoinColumn(name = "address_id")
	private Address address;
}
