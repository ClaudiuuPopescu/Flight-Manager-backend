package msg.project.flightmanager.service.interfaces;

import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.modelHelper.CreateAddressModel;

public interface IAddressService {

	AddressDto createAddress(CreateAddressModel createAddressModel) throws ValidatorException;

	AddressDto editAddress(AddressDto addressDto) throws ValidatorException;

}
