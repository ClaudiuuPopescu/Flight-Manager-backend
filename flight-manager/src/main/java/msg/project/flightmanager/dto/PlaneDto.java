package msg.project.flightmanager.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import msg.project.flightmanager.enums.PlaneSize;

@Getter
@Builder(toBuilder = true)
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
