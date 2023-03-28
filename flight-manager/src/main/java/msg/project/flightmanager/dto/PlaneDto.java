package msg.project.flightmanager.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import msg.project.flightmanager.enums.PlaneSize;

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
	private PlaneSize size;
}
