package msg.project.flightmanager.model;

import java.sql.Time;
import java.time.LocalDate;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msg.project.flightmanager.enums.ItineraryFlightStatusEnum;

@Entity
@Table(name = "itinerary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Itinerary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id_itinerary;

	@Column
	private int seatsReserved; // out of plane capacity

	@Column
	private String notes;

	@Column
	@Builder.Default
	private ItineraryFlightStatusEnum flightStatus = ItineraryFlightStatusEnum.FLIGHT_ACTIVE;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(name = "flight_id")
	private Flight flight;

	@Column
	private Time boardingTime;

	@Column
	private double duration;

	@Column
	private int seatsTotal; // plane capacity

	@Column
	private String fromCountry;
	
	@Column
	private String fromCity;
	
	@Column
	private String toCountry;
	
	@Column
	private String toCity;

	@Column
	private LocalDate date;

}
