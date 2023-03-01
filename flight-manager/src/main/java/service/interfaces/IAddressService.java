package service.interfaces;

import dto.AddressDto;
import exceptions.ValidatorException;
import modelHelper.CreateAddressModel;

public interface IAddressService {

	AddressDto createAddress(CreateAddressModel createAddressModel) throws ValidatorException;

}
