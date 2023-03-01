package service.interfaces;

import dto.AddressDto;
import exceptions.ValidatorException;

public interface IAddressService {

	AddressDto createAddress(AddressDto addressDto) throws ValidatorException;

	AddressDto editAddress(AddressDto addressDto) throws ValidatorException;

}
