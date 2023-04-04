package msg.project.flightmanager.model;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "Airport")
@Table(name = "airport")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Airport {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column
	private Long id_airport;

	@Column(unique = true, length = 30)
	private String airportName;

	@Column(unique = true)
	private String codeIdentifier; // name = John F. Kennedy International Airport -> code = JFKIA

	@Min(1)
	@Max(8)
	@Column
	private int runWays;

	@Min(1)
	@Max(200)
	@Column
	private int gateWays;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Address address;

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name = "airport_company", joinColumns = @JoinColumn(name = "airport_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
	@Builder.Default
	private Set<Company> companiesCollab = new HashSet<>();
	
	public void removeCollab(Company company) {
		this.companiesCollab.remove(company);
	}

	@OneToMany(mappedBy = "from", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	@Column(name = "flight_start")
	@Builder.Default
	private Set<Flight> flightsStart = new HashSet<>();
	
	public void addFlightStart(Flight flight) {
		this.flightsStart.add(flight);
		flight.setFrom(this);
	}
	
	public void removeFlightStart(Flight flight) {
		this.flightsStart.remove(flight);
		flight.setFrom(null);
	}

	@OneToMany(mappedBy = "to", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	@Column(name = "flight_end")
	@Builder.Default
	private Set<Flight> flightsEnd = new HashSet<>();
	
	public void addFlightTo(Flight flight) {
		this.flightsEnd.add(flight);
		flight.setTo(this);
	}
	
	public void removeFlightTo(Flight flight) {
		this.flightsEnd.remove(flight);
		flight.setTo(null);
	}

}
