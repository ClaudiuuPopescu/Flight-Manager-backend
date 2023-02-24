package model;

import java.sql.Date;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class Plane {

	private Long id;
	private String model;
	private int capacity;
	private Long range;
	private int fuelTankCapacity;
	private Date manufacturingDate;
	private Date firstFlight;
	private Date lastRevision;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
}
