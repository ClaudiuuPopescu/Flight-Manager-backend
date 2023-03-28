package msg.project.flightmanager.modelHelper;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EditAirportModel {

	private String codeIdentifier;
	private int runWarys;
	private int gateWays;

	@Nullable
	private CreateAddressModel addressModel;
}
