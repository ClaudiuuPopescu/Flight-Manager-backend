package msg.project.flightmanager.modelHelper;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msg.project.flightmanager.dto.AddressDto;

@Data
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserModel {

	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private LocalDate birthDate;
	private String roleTitle;

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
