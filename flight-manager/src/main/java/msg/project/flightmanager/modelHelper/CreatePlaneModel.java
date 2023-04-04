package msg.project.flightmanager.modelHelper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlaneModel {

	private String model;
	private int tailNumber;
	private int capacity;
	private int fuelTankCapacity;
	private String manufacturingDate;
	private String size;
}
