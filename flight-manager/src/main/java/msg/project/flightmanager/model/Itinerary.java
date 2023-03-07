package msg.project.flightmanager.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import enums.BookingPaymentEnum;
import enums.BookingStatusEnum;
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
	private String passengerName;


	@Column
	private int seatsReserved;

	@Column
	@Builder.Default
	private BookingStatusEnum bookingStatus = BookingStatusEnum.BOOKING_PENDING;

	@Column
	@Builder.Default
	private BookingPaymentEnum paymentStatus = BookingPaymentEnum.BOOKING_NOT_PAID;

	@Column
	private int totalCost; // NU I SIGUR


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "flight_id")
	private Flight flight;

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
