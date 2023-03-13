package msg.project.flightmanager.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class AirportDto {

	private String airportName;
	private String codeIdentifier;
	private int runWarys;
	private int gateWays;
	private AddressDto addressDto;
}
