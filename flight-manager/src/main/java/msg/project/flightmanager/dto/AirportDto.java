package msg.project.flightmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class AirportDto {

	private String airportName;
	private String codeIdentifier;
	private int runWarys;
	private int gateWays;
	private AddressDto addressDto;
}
