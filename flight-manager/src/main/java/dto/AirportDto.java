package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AirportDto {

	private String airportName;
	private AddressDto addressDto;
}
