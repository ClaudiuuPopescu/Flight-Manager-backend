package msg.project.flightmanager.modelHelper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditItineraryModel {

	private Long itineraryId;
	private int seatsReserved;
	private String notes;
	private Long currentFlightId;
	private Long newFlightId;
}
