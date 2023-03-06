package msg.project.flightmanager.model;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
	@GenericGenerator(name = "native", strategy = "native")
	@Column
	private Long id_airport;

	@Column(unique = true, length = 30)
	private String airportName;

	@Column(unique = true)
	private String codeIdentifier; // name = John F. Kennedy International Airport -> code = JFKIA

	@Length(min = 1, max = 8)
	@Column
	private int runWays;

	@Length(min = 1, max = 200)
	@Column
	private int gateWays;

	@OneToOne
	@JoinColumn(name = "address_id")
	private Address address;

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name = "airport_company", joinColumns = @JoinColumn(name = "airport_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
	@Builder.Default
	private Set<Company> companiesCollab = new HashSet<>();
	
	public void removeCollab(Company company) {
		companiesCollab.remove(company);
	}

	@OneToMany(mappedBy = "from", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	@Column(name = "flight_start")
	private Set<Flight> flightsStart;

	@OneToMany(mappedBy = "to", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	@Column(name = "flight_end")
	private Set<Flight> flightsEnd;

}
