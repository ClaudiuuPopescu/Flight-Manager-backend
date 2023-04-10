package msg.project.flightmanager.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {

	private String name;

	private String phoneNumber;

	private String email;

	private LocalDate foundedIn;

	@Builder.Default
	private AddressDto address = new AddressDto();

	public void setCountry(String country) {
		this.address.setCountry(country);
	}
	
	public void setCity (String city) {
		this.address.setCity(city);
	}
	
	public void setStreet (String street) {
		this.address.setStreet(street);
	}
	
	public void setStreetNumber (int streetNumber) {
		this.address.setStreetNumber(streetNumber);
	}
	
	public void setApartment (int apartment) {
		this.address.setApartment(apartment);
	}
}
