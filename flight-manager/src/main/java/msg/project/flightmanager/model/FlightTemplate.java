package msg.project.flightmanager.model;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

	@OneToMany(mappedBy = "flightTemplate", fetch = FetchType.LAZY)
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
		return Objects.hash(boardingTime, date, duration, flightName, from, gate, idFlightTemplate, plane, to);
	}

	@Override
	public String toString() {
		List<String> flights = this.getFlights().stream().map(flight -> flight.getFlightName()).collect(Collectors.toList());
		return "FlightTemplate [idFlightTemplate=" + idFlightTemplate + ", flightName=" + flightName + ", from=" + from
				+ ", plane=" + plane + ", boardingTime=" + boardingTime + ", to=" + to + ", date=" + date + ", gate="
				+ gate + ", duration=" + duration + ", flights=" + flights + "]";
	}
	
	

}
