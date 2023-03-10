package msg.project.flightmanager.dto;

import io.micrometer.common.lang.Nullable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class AddressDto {

	private Long idAddress;
	private String country;
	private String city;
	private String street;
	private int streetNumber;
	@Nullable
	private int apartment;
}
