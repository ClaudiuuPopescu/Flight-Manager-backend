package converter;

import org.springframework.stereotype.Component;

import dto.AddressDto;
import msg.project.flightmanager.model.Address;

@Component
public class AddressConverter implements IConverter<Address, AddressDto> {

	@Override
	public AddressDto convertToDTO(Address address) {
		AddressDto addressDto = AddressDto.builder().coutry(address.getCoutry()).city(address.getCity())
				.street(address.getStreet()).streetNumber(address.getStreetNumber()).apartment(address.getApartment())
				.build();
		return addressDto;
	}

	@Override
	public Address convertToEntity(AddressDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
