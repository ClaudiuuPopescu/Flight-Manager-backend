package msg.project.flightmanager.modelHelper;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateAirportModel {

	private String airportName;
	private int runWarys;
	private int gateWays;
	private CreateAddressModel address;
	
	List<String> companyNames_toCollab = new ArrayList<>();
}
