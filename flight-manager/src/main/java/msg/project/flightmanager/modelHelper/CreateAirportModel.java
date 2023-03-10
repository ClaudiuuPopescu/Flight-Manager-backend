package msg.project.flightmanager.modelHelper;

import lombok.Getter;

@Getter
public class CreateAirportModel {

	private String airportName;
	private String codeIdentifier;
	private int runWarys;
	private int gateWays;
	private CreateAddressModel address;
}
