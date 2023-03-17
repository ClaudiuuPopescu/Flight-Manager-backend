package msg.project.flightmanager.modelHelper;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditAirportModel {

	private String codeIdentifier;
	private int runWarys;
	private int gateWays;

	@Nullable
	private CreateAddressModel addressModel;
}
