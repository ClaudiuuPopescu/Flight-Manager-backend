package dto;

import java.time.LocalDate;

import enums.PlaneSize;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class PlaneDto {

	private String model;
	private int capacity;
	private int fuelTankCapacity;
	private LocalDate manufacturingDate;
	private LocalDate firstFlight;
	private LocalDate lastRevision;
	private PlaneSize size;
}
