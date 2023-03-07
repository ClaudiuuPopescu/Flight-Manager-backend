package msg.project.flightmanager.model;

import java.util.Objects;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "FlightTemplate")
@Table(name = "flight_template")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("hibernateLazyInitializer")
public class FlightTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "idFlightTemplate")
	private Long idFlightTemplate;

	@Column(name = "flight_name")
	@Builder.Default
	private boolean flightName = false;

	@Column(name = "from")
	@Builder.Default
	private boolean from = false;

	@Column(name = "plane")
	@Builder.Default
	private boolean plane = false;

	@Column(name = "boarding_time")
	@Builder.Default
	private boolean boardingTime = false;

	@Column(name = "to")
	@Builder.Default
	private boolean to = false;

	@Column(name = "date")
	@Builder.Default
	private boolean date = false;

	@Column(name = "gate")
	@Builder.Default
	private boolean gate = false;

	@Column(name = "duration")
	@Builder.Default
	private boolean duration = false;

	@OneToMany(mappedBy = "flightTemplate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Column(name = "flights")
	private Set<Flight> flights;
	
	public void addFlight(Flight flight) {
		flights.add(flight);
		flight.setFlightTemplate(this);
	}
	
	public void removeFlight(Flight flight) {
		flights.remove(flight);
		flight.setCanceled(true);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlightTemplate other = (FlightTemplate) obj;
		return boardingTime == other.boardingTime && date == other.date && duration == other.duration
				&& flightName == other.flightName && from == other.from && gate == other.gate && plane == other.plane
				&& to == other.to;
	}

	@Override
	public int hashCode() {
		return Objects.hash(boardingTime, date, duration, flightName, flights, from, gate, idFlightTemplate, plane, to);
	}

}
