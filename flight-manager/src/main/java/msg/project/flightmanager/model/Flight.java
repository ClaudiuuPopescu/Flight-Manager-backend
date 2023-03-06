package msg.project.flightmanager.model;

import java.sql.Time;
import java.time.LocalDate;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@EqualsAndHashCode
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
	}

	@ManyToOne
	@JoinColumn(name = "to")
	private Airport to;

	public void addFlightToAirportEnd() {
		this.to.getFlightsEnd().add(this);
	}

	public void removeFlightFromAirportEndt() {
		this.to.getFlightsEnd().remove(this);
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

}
