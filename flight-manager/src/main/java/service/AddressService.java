package service;

import java.text.MessageFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import converter.AddressConverter;
import dto.AddressDto;
import exceptions.FlightManagerException;
import exceptions.ValidatorException;
import jakarta.transaction.Transactional;
import msg.project.flightmanager.model.Address;
import repository.AddressRepository;
import service.interfaces.IAddressService;
import validator.AddressValidator;

public class AddressService implements IAddressService {
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private AddressValidator addressValidator;
	@Autowired
	private AddressConverter addressConverter;

	@Transactional
	@Override
	public AddressDto createAddress(AddressDto addressDto) throws ValidatorException {
		this.addressValidator.validateAddress(addressDto);

		Address address = this.addressConverter.convertToEntity(addressDto);

		this.addressRepository.save(address);
		return this.addressConverter.convertToDTO(address);
	}

	public boolean getAddressByAllFields(AddressDto addressDto) {

		Optional<Address> address = this.addressRepository.findByAllAttributes(addressDto.getCoutry(),
				addressDto.getCity(), addressDto.getStreet(), addressDto.getStreetNumber(), addressDto.getApartment());

		return address.isPresent() ? true : false;
	}

	@Transactional
	@Override
	public AddressDto editAddress(AddressDto addressDto) throws ValidatorException {
		Address address = this.addressRepository.findById(addressDto.getIdAddress())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND, MessageFormat
						.format("Can not edit address. Address with id[{0}] not found", addressDto.getIdAddress())));

		// scenario 2 or more people with same address
		// check if another user shares the same address
		// if a couple shares it, and one moves:)) doesnt change the address to the
		// other person
		// and creates a new one
		if (address.getUsers().size() > 1) {
			AddressDto addressDtoCreated = createAddress(addressDto);
			return addressDtoCreated;
		}

		// scenario the only one with this address
		this.addressValidator.validateAddress(addressDto);

		address = this.addressConverter.convertToEntity(addressDto);
		this.addressRepository.save(address);

		AddressDto addressDtoEdited = this.addressConverter.convertToDTO(address);
		return addressDtoEdited;
	}
}
