package msg.project.flightmanager.modelHelper;

import java.util.ArrayList;
import java.util.List;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAirportModel {

	private String airportName;
	private int runWays;
	private int gateWays;
	private CreateAddressModel address = new CreateAddressModel();
	
	@Nullable
	List<String> companyNames_toCollab = new ArrayList<>();
	
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
