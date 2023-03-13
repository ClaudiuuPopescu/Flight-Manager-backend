package msg.project.flightmanager.modelHelper;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import msg.project.flightmanager.enums.PlaneSize;

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
