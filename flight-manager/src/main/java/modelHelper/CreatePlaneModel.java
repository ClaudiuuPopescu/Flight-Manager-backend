package modelHelper;

import java.time.LocalDate;

import enums.PlaneSize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatePlaneModel {

	private String model;
	private int tailNumber;
	private int capacity;
	private int fuelTankCapacity;
	private LocalDate manufacturingDate;
	private PlaneSize size;
}
