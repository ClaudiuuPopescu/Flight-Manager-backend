package msg.project.flightmanager.modelHelper;

import io.micrometer.common.lang.Nullable;
import lombok.Getter;

@Getter
public class EditAirportModel {

	private String codeIdentifier;
	private int runWarys;
	private int gateWays;

	@Nullable
	private CreateAddressModel addressModel;
}
