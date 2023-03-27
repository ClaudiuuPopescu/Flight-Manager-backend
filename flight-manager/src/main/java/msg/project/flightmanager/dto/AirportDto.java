package msg.project.flightmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AirportDto {

	private String airportName;
	private String codeIdentifier;
	private int runWarys;
	private int gateWays;
	private AddressDto addressDto;
}
