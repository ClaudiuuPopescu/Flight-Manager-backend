package msg.project.flightmanager.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import jakarta.transaction.Transactional;
import msg.project.flightmanager.converter.AirportConverter;
import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.dto.AirportDto;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Address;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.modelHelper.ActionCompanyAirportCollab;
import msg.project.flightmanager.modelHelper.CreateAirportModel;
import msg.project.flightmanager.modelHelper.EditAirportModel;
import msg.project.flightmanager.repository.AddressRepository;
import msg.project.flightmanager.repository.AirportRepository;
import msg.project.flightmanager.repository.CompanyRepository;
import msg.project.flightmanager.service.interfaces.IAirportService;
import msg.project.flightmanager.validator.AirportValidator;

public class AirportService implements IAirportService {
	@Autowired
	private AirportRepository airportRepository;
	@Autowired
	private AirportConverter airportConverter;
	@Autowired
	private AirportValidator airportValidator;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private AddressService addressService;
	@Autowired
	private AddressRepository addressRepository;

	@Override
	public List<AirportDto> getAll() {
		List<Airport> airports = StreamSupport.stream(this.airportRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());

		if (airports.isEmpty()) {
			throw new FlightManagerException(HttpStatus.NO_CONTENT, "No airports found");
		}

		List<AirportDto> airportsDto = airports.stream().map(this.airportConverter::convertToDTO)
				.collect(Collectors.toList());
		return airportsDto;
	}

	@Transactional
	@Override
	public boolean createAirport(CreateAirportModel createAirportModel) throws ValidatorException {
		// TODO verificare rol current user
		// TODO we get the company from the current user to add it to

		this.airportValidator.validateCreateAiportModel(createAirportModel);

		Airport airport = this.airportConverter.convertCreateModelToEntity(createAirportModel);

		AddressDto addressDto = this.addressService.createAddress(createAirportModel.getAddress());
		Address address = this.addressRepository.findById(addressDto.getIdAddress()).get();

		airport.setAddress(address);

		String codeIdentifier = generateCodeIdentifier(airport.getAirportName());
		airport.setCodeIdentifier(codeIdentifier);

		this.airportRepository.save(airport);
		return true;
	}

	@Transactional
	@Override
	public boolean editAirport(EditAirportModel editAirportModel) throws ValidatorException {
		// TODO verificare rol current user

		Airport airport = this.airportRepository.findByCodeIdentifier(editAirportModel.getCodeIdentifier())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not perform the edit action. Airport [{0}] not found",
								editAirportModel.getCodeIdentifier())));

		this.airportValidator.validateEditAirport(editAirportModel);

		if (editAirportModel.getAddressModel() != null) {
			AddressDto addressDto = this.addressService.createAddress(editAirportModel.getAddressModel());
			Address address = this.addressRepository.findById(addressDto.getIdAddress()).get();

			airport.setAddress(address);
		}

		airport.setRunWays(editAirportModel.getRunWarys());
		airport.setGateWays(editAirportModel.getGateWays());

		this.airportRepository.save(airport);
		return true;
	}

	@Transactional
	@Override
	public boolean removeAirport(String codeIdentifier) {
		// TODO verificare rol current user

		Airport airport = this.airportRepository.findByCodeIdentifier(codeIdentifier)
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not delete airport. Airport [{0}] not found", codeIdentifier)));

		List<Flight> airportFlights = new ArrayList<>();
		airportFlights.addAll(airport.getFlightsStart());
		airportFlights.addAll(airport.getFlightsEnd());

		for (Flight flight : airportFlights) {
			if (flight.isActiv()) {
				flight.getFlightTemplate().setPlane(false);
				flight.setCanceled(true);
			}
		}

		this.airportRepository.delete(airport);
		return true;
	}

	@Transactional
	@Override
	public boolean addCompanyCollab(ActionCompanyAirportCollab actionCompanyAirportCollab) {
		// TODO verificare rol current user

		Airport airport = this.airportRepository
				.findByCodeIdentifier(actionCompanyAirportCollab.getAirportCodeIdentifier())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not add company collaboration to airport. Airport [{0}] not found",
								actionCompanyAirportCollab.getAirportCodeIdentifier())));
		
		Company company = this.companyRepository.findCompanyByName(actionCompanyAirportCollab.getCompanyName())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not add company collaboration to airport. Company [{0}] not found",
								actionCompanyAirportCollab.getCompanyName())));

		airport.getCompaniesCollab().add(company);
		this.airportRepository.save(airport);
		return true;
	}

	@Transactional
	@Override
	public boolean removeCompanyCollab(ActionCompanyAirportCollab actionCompanyAirportCollab) {
		// TODO verificare rol current user

		Airport airport = this.airportRepository
				.findByCodeIdentifier(actionCompanyAirportCollab.getAirportCodeIdentifier())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not remove company collaboration to airport. Airport [{0}] not found",
								actionCompanyAirportCollab.getAirportCodeIdentifier())));

		Company company = this.companyRepository.findCompanyByName(actionCompanyAirportCollab.getCompanyName())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not remove company collaboration to airport. Company [{0}] not found",
								actionCompanyAirportCollab.getCompanyName())));

		airport.getCompaniesCollab().remove(company);
		this.airportRepository.save(airport);
		return true;
	}

	private String generateCodeIdentifier(String airportName) {
		String codeIdentifier = "";

		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < airportName.length(); i++) {
			char c = airportName.charAt(i);
			if (Character.isUpperCase(c)) {
				strBuilder.append(c);
			}
		}

		codeIdentifier = strBuilder.toString();
		Random random = new Random();

		while (true) {
			boolean codeIdentifierExists = checkIfCodeIdentifierExists(codeIdentifier);

			if (!codeIdentifierExists) {
				return codeIdentifier;
			}

			char letter = (char) (random.nextInt(26) + 'A');

			codeIdentifier += String.valueOf(letter);
		}
	}

	private boolean checkIfCodeIdentifierExists(String codeIdentifier) {
		Optional<Airport> airport = this.airportRepository.findByCodeIdentifier(codeIdentifier);

		return airport.isPresent();
	}

}
