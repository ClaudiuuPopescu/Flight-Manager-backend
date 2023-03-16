package msg.project.flightmanager.modelHelper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItineraryHelperModel {

	private int seatsReserved;
	private String notes;
	private Long flightId;
}
