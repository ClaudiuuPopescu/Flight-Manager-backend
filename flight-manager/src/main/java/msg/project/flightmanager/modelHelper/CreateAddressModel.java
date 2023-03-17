package msg.project.flightmanager.modelHelper;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateAddressModel {

	private String country;
	private String city;
	private String street;
	private int streetNumber;
	@Nullable
	private int apartment;

}
