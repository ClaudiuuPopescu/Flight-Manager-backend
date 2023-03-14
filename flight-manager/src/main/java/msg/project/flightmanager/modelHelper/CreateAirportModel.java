package msg.project.flightmanager.modelHelper;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class CreateAirportModel {

	private String airportName;
	private String codeIdentifier;
	private int runWarys;
	private int gateWays;
	private CreateAddressModel address;
	
	List<String> companyNames_toCollab = new ArrayList<>();
}
