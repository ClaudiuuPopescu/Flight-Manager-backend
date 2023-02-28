package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AddressDto {

	private Long idAddress;
	private String coutry;
	private String city;
	private String street;
	private int streetNumber;
	private int apartment;
}
