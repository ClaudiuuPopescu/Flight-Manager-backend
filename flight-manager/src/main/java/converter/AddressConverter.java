package converter;

import dto.AddressDto;
import modelHelper.CreateAddressModel;
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

	public Address createModelConvertToEntity(CreateAddressModel createaddressModel) {
		return Address.builder().country(createaddressModel.getCoutry()).city(createaddressModel.getCity())
				.street(createaddressModel.getStreet()).streetNumber(createaddressModel.getStreetNumber())
				.apartment(createaddressModel.getApartment()).build();
	}

}
