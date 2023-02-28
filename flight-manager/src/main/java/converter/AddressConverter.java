package converter;

import dto.AddressDto;
import msg.project.flightmanager.model.Address;

public class AddressConverter implements IConverter<Address, AddressDto> {

	@Override
	public AddressDto convertToDTO(Address address) {

		return AddressDto.builder().idAddress(address.getIdAddress()).coutry(address.getCountry())
				.city(address.getCity()).street(address.getStreet()).streetNumber(address.getStreetNumber())
				.apartment(address.getApartment()).build();
	}

	@Override
	public Address convertToEntity(AddressDto addressDto) {

		return Address.builder().idAddress(addressDto.getIdAddress()).country(addressDto.getCoutry())
				.city(addressDto.getCity()).street(addressDto.getStreet()).streetNumber(addressDto.getStreetNumber())
				.apartment(addressDto.getApartment()).build();
	}

}
