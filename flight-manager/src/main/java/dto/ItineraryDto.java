package dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItineraryDto {

	private Long id_itinerary;
	private int seatsReserved;
	private FlightDto flight;
	private int seatsTotal;
	private String fromCountry;
	private String fromCity;
	private String toCountry;
	private String toCity;
	private LocalDate date;
}
