package msg.project.flightmanager.service;

import java.text.MessageFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import msg.project.flightmanager.converter.AddressConverter;
import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Address;
import msg.project.flightmanager.modelHelper.CreateAddressModel;
import msg.project.flightmanager.repository.AddressRepository;
import msg.project.flightmanager.service.interfaces.IAddressService;
import msg.project.flightmanager.validator.AddressValidator;

@Service
public class AddressService implements IAddressService {
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private AddressValidator addressValidator;
	@Autowired
	private AddressConverter addressConverter;

	@Transactional
	@Override
	public AddressDto createAddress(CreateAddressModel createAddressModel) throws ValidatorException {
		this.addressValidator.validateCreateModel(createAddressModel);

		Address address = this.addressConverter.converCreateModeltToEntity(createAddressModel);

		this.addressRepository.save(address);
		return this.addressConverter.convertToDTO(address);
	}

	public Optional<Address> getAddressByAllFields(CreateAddressModel createAddressModel) {

		Optional<Address> address = this.addressRepository.findByAllAttributes(createAddressModel.getCountry(),
				createAddressModel.getCity(), createAddressModel.getStreet(), createAddressModel.getStreetNumber(),
				createAddressModel.getApartment());

		return address;
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
			AddressDto addressDtoCreated = createAddress(this.addressConverter.converCreateModeltToEntity(addressDto));
			return addressDtoCreated;
		}

		// scenario the only one with this address
		this.addressValidator.validateAddressDto(addressDto);

		address = this.addressConverter.convertToEntity(addressDto);
		this.addressRepository.save(address);

		AddressDto addressDtoEdited = this.addressConverter.convertToDTO(address);
		return addressDtoEdited;
	}
}
