package converter;

import dto.AddressDto;
import modelHelper.CreateAddressModel;
import msg.project.flightmanager.model.Address;

public class AddressConverter implements IConverter<Address, AddressDto> {

	@Override
	public AddressDto convertToDTO(Address address) {

		return AddressDto.builder().idAddress(address.getIdAddress()).country(address.getCountry())
				.city(address.getCity()).street(address.getStreet()).streetNumber(address.getStreetNumber())
				.apartment(address.getApartment()).build();
	}

	@Override
	public Address convertToEntity(AddressDto addressDto) {

		return Address.builder().country(addressDto.getCountry()).city(addressDto.getCity())
				.street(addressDto.getStreet()).streetNumber(addressDto.getStreetNumber())
				.apartment(addressDto.getApartment()).build();
	}

	public Address converCreateModeltToEntity(CreateAddressModel createAddressModel) {

		return Address.builder().country(createAddressModel.getCountry()).city(createAddressModel.getCity())
				.street(createAddressModel.getStreet()).streetNumber(createAddressModel.getStreetNumber())
				.apartment(createAddressModel.getApartment()).build();
	}

	public CreateAddressModel converCreateModeltToEntity(AddressDto addressDto) {

		return CreateAddressModel.builder().country(addressDto.getCountry()).city(addressDto.getCity())
				.street(addressDto.getStreet()).streetNumber(addressDto.getStreetNumber())
				.apartment(addressDto.getApartment()).build();
	}

}
