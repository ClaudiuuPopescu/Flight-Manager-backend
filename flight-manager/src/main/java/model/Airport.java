package model;

import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
	@Column(name = "airportName", length = 30)
	private String airportName;

	@OneToOne
	@JoinColumn(name = "address_id")
	private Address address;

	@OneToMany(mappedBy = "from", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	@Column(name = "flight_start")
	private Set<Flight> flightsStart;

	@OneToMany(mappedBy = "to", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	@Column(name = "flight_end")
	private Set<Flight> flightsEnd;

}
