package service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import converter.AirportConverter;
import dto.AirportDto;
import exceptions.FlightManagerException;
import exceptions.ValidatorException;
import modelHelper.ActionCompanyAirportCollab;
import modelHelper.EditAirportModel;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Company;
import repository.AirportRepository;
import repository.CompanyRepository;
import service.interfaces.IAirportService;
import validator.AirportValidator;

public class AirportService implements IAirportService {
	@Autowired
	private AirportRepository airportRepository;
	@Autowired
	private AirportConverter airportConverter;
	@Autowired
	private AirportValidator airportValidator;
	@Autowired
	private CompanyRepository companyRepository;

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

	@Override
	public boolean createAirport(AirportDto airportDto) throws ValidatorException {
		// TODO verificare rol current user
		// TODO we get the company from the current user to add it to

		this.airportValidator.validateAirport(airportDto);

		Airport airport = this.airportConverter.convertToEntity(airportDto);

		String codeIdentifier = generateCodeIdentifier(airport.getAirportName());
		airport.setCodeIdentifier(codeIdentifier);

		this.airportRepository.save(airport);
		return true;
	}

	@Override
	public boolean editAirport(EditAirportModel editAirportModel) {
		// TODO verificare rol current user

		Airport airport = this.airportRepository.findByCodeIdentifier(editAirportModel.getCodeIdentifier())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not perform the edit action. Airport [{0}] not found",
								editAirportModel.getCodeIdentifier())));

		this.airportValidator.validateEditAirport(editAirportModel);

		airport.setRunWays(editAirportModel.getRunWarys());
		airport.setGateWays(editAirportModel.getGateWays());

		this.airportRepository.save(airport);
		return true;
	}

	@Override
	public boolean removeAirport(String codeIdentifier) {
		// TODO verificare rol current user

		Airport airport = this.airportRepository.findByCodeIdentifier(codeIdentifier)
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not delete airport. Airport [{0}] not found", codeIdentifier)));

		// TODO set field false in flight template.
		// TODO delete flight

		this.airportRepository.delete(airport);
		return true;
	}

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