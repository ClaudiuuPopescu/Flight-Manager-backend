package msg.project.flightmanager.dto;

import java.time.LocalDate;

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
	private LocalDate manufacturingDate;
	private LocalDate firstFlight;
	private LocalDate lastRevision;
	private String size;
	
	@Nullable
	private LocalDate newRevision;
}
