package msg.project.flightmanager.model;

import java.sql.Time;
import java.time.LocalDate;
import java.util.HashSet;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "Flight")
@Table(name = "flight")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("hibernateLazyInitializer")
//daca un aeroport e sters --> anulez zborul si pun aeroportul pe null
//daca un plane e sters --> anulez zborul, pun activ pe false si pun plane pe null
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "idFlight")
	private Long idFlight;

	@Column(name = "flightName", length = 30, unique = true)
	private String flightName;

	@Column(name = "date")
	private LocalDate date;

	@Column(name = "gate")
	private String gate;

	@Column(name = "boarding_time")
	private Time boardingTime;

	@Column(name = "activ")
	@Builder.Default
	private boolean activ = true;

	@Column(name = "canceled")
	@Builder.Default
	private boolean canceled = false;

	@Column(name = "duration")
	private double duration;

	@ManyToOne
	@JoinColumn(name = "from")
	private Airport from;

	public void addFlightToAirportStart() {
		this.from.getFlightsStart().add(this);
	}

	public void removeFlightFromAirportStart() {
		this.from.getFlightsStart().remove(this);
		this.setFrom(null);
	}

	@ManyToOne
	@JoinColumn(name = "to")
	private Airport to;

	public void addFlightToAirportEnd() {
		this.to.getFlightsEnd().add(this);
	}

	public void removeFlightFromAirportEndt() {
		this.to.getFlightsEnd().remove(this);
		this.setTo(null);
	}

	@ManyToOne
	@JoinColumn(name = "plane")
	private Plane plane;

	@ManyToOne
	@JoinColumn(name = "idFlightTemplate", nullable = false)
	private FlightTemplate flightTemplate;

	public void addFlightToTemplate() {
		this.flightTemplate.getFlights().add(this);
	}

	@OneToMany(mappedBy = "flight", cascade = CascadeType.REMOVE)
	@Builder.Default
	private Set<Itinerary> itineraries = new HashSet<>();
	
	@OneToMany(mappedBy = "flight", fetch = FetchType.LAZY)
	@Builder.Default
	private Set<Report> reports = new HashSet<>();

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Flight other = (Flight) obj;
		return activ == other.activ && Objects.equals(boardingTime, other.boardingTime) && canceled == other.canceled
				&& Objects.equals(date, other.date)
				&& Double.doubleToLongBits(duration) == Double.doubleToLongBits(other.duration)
				&& Objects.equals(flightName, other.flightName)
				&& Objects.equals(flightTemplate, other.flightTemplate)
				&& Objects.equals(from, other.from) 
				&& Objects.equals(gate, other.gate)
				&& Objects.equals(idFlight, other.idFlight) 
				&& Objects.equals(plane, other.plane) 
				&& Objects.equals(to, other.to);
	}

	@Override
	public int hashCode() {
		return Objects.hash(activ, boardingTime, canceled, date, duration, flightName, from, gate, idFlight, to);
	}

	@Override
	public String toString() {
		return "Flight [idFlight=" + idFlight + ", flightName=" + flightName + ", date=" + date + ", gate=" + gate
				+ ", boardingTime=" + boardingTime + ", activ=" + activ + ", canceled=" + canceled + ", duration="
				+ duration + ", from=" + from + ", to=" + to + ", plane=" + plane.getTailNumber() 
				+ ", flightTemplate=" + flightTemplate.getIdFlightTemplate()
				+ "]";
	}
	
	

}
