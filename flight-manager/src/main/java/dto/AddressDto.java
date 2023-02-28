package dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressDto {

	private String coutry;
	private String city;
	private String street;
	private int streetNumber;
	private int apartment;
}
