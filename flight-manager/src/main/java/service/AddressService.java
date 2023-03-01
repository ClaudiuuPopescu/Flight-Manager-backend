package service;

import org.springframework.beans.factory.annotation.Autowired;

import dto.AddressDto;
import exceptions.ValidatorException;
import modelHelper.CreateAddressModel;
import msg.project.flightmanager.model.Address;
import repository.AddressRepository;
import service.interfaces.IAddressService;
import validator.AddressValidator;

public class AddressService implements IAddressService {
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private AddressValidator addressValidator;

	@Override
	public AddressDto createAddress(CreateAddressModel createAddressModel) throws ValidatorException {
		this.addressValidator.validateCreateAddressModel(createAddressModel);
		
		Address address =  
		
		return null;
	}

}
