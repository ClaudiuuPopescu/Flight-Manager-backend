package modelHelper;

import lombok.Getter;

@Getter
public class EditItineraryModel {

	private Long itineraryId;
	private int seatsReserved;
	private String notes;
	private Long currentFlight;
	private Long newFlightId;
}
