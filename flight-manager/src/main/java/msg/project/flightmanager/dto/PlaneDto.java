package msg.project.flightmanager.dto;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class PlaneDto {

	private String model;
	private int tailNumber;
	private int capacity;
	private int fuelTankCapacity;
	private String manufacturingDate;
	private String firstFlight;
	private String lastRevision;
	private String size;
	
	@Nullable
	private String newRevision;
}
