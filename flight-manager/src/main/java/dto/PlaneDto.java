package dto;

import java.sql.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class PlaneDto {

	private String model;
	private int capacity;
	private Long range;
	private int fuelTankCapacity;
	private Date manufacturingDate;
	private Date firstFlight;
	private Date lastRevision;
}
