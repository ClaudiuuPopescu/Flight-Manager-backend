package msg.project.flightmanager.servicies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import msg.project.flightmanager.converter.AirportConverter;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Address;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.FlightTemplate;
import msg.project.flightmanager.modelHelper.ActionCompanyAirportCollab;
import msg.project.flightmanager.modelHelper.CreateAddressModel;
import msg.project.flightmanager.modelHelper.CreateAirportModel;
import msg.project.flightmanager.modelHelper.EditAirportModel;
import msg.project.flightmanager.repository.AddressRepository;
import msg.project.flightmanager.repository.AirportRepository;
import msg.project.flightmanager.repository.CompanyRepository;
import msg.project.flightmanager.service.AddressService;
import msg.project.flightmanager.service.AirportService;
import msg.project.flightmanager.validator.AirportValidator;

@ExtendWith(MockitoExtension.class)
public class AirportServiceTest {
	@InjectMocks
	private AirportService service;
	@Mock
	private AirportRepository repository;
	@Mock
	private AirportConverter airportConverter;
	@Mock
	private AirportValidator validator;
	@Mock
	private CompanyRepository companyRepository;
	@Mock
	private AddressService addressService;
	@Mock
	private AddressRepository addressRepository;
	
	@Test
	void getAll_throwsFlightManagerException_whenNoAirportFound() {
		
		Mockito.when(this.repository.findAll()).thenReturn(Collections.emptyList());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.getAll());
		
		assertEquals("No airports found", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.getAll());
	}
	
	@Test
	void getAll_returnsListOfAirports_whenAirportsExistInDataBase() {
		Airport first_airport = new Airport();
		Airport second_airport = new Airport();
		
		Mockito.when(this.repository.findAll()).thenReturn(Arrays.asList(first_airport, second_airport));
		
		assertEquals(2,this.service.getAll().size());
	}
	
	@Test
	void createAirport_throwsValidatorException_whenAirportNameEmpty() throws ValidatorException {
		String airportName = "";
		CreateAddressModel addressModel = Mockito.mock(CreateAddressModel.class);
		
		CreateAirportModel model = new CreateAirportModel(airportName, 5, 2, addressModel, Collections.emptyList());
		
		Mockito.doThrow(new ValidatorException(
				"The airport name cannot be empty!",
				ErrorCode.EMPTY_FIELD))
		.when(this.validator).validateCreateAiportModel(model);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.service.createAirport(model));
		
		assertEquals("The airport name cannot be empty!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.service.createAirport(model));
	}
	
	@Test
	void createAirport_throwsValidatorException_whenAirportNameTooLong() throws ValidatorException {
		String airportName = "thisIsATooLongAirportName";
		CreateAddressModel addressModel = Mockito.mock(CreateAddressModel.class);
		
		CreateAirportModel model = new CreateAirportModel(airportName, 5, 2, addressModel, Collections.emptyList());
		
		Mockito.doThrow(new ValidatorException(
				"The airport name is too long!",
				ErrorCode.IS_TOO_LONG))
		.when(this.validator).validateCreateAiportModel(model);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.service.createAirport(model));
		
		assertEquals("The airport name is too long!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.service.createAirport(model));
	}
	
	@Test
	void createAirport_throwsValidatorException_whenAirportNameContainsOthersThanLetters() throws ValidatorException {
		String airportName = "thisIsNotAnAcceptedAirportName123";
		CreateAddressModel addressModel = Mockito.mock(CreateAddressModel.class);
		
		CreateAirportModel model = new CreateAirportModel(airportName, 5, 2, addressModel, Collections.emptyList());
		
		Mockito.doThrow(new ValidatorException(
				"The airport name should be only out of letters!",
				ErrorCode.IS_NOT_OUT_OF_LETTERS))
		.when(this.validator).validateCreateAiportModel(model);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.service.createAirport(model));
		
		assertEquals("The airport name should be only out of letters!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.service.createAirport(model));
	}
	
	@Test
	void createAirport_throwsValidatorException_whenAirportNameIsAlreadyTaken() throws ValidatorException {
		String airportName = "thisIsNotAnAcceptedAirportName123";
		CreateAddressModel addressModel = Mockito.mock(CreateAddressModel.class);
		
		CreateAirportModel model = new CreateAirportModel(airportName, 5, 2, addressModel, Collections.emptyList());
		
		Airport alreadyExistingAirport = new Airport();
		
		Mockito.lenient().when(this.repository.findByName(airportName)).thenReturn(Optional.of(alreadyExistingAirport));
		
		Mockito.doThrow(new FlightManagerException(
						HttpStatus.IM_USED, 
						MessageFormat.format("An aiport with the name [{0}] already exists. Find another one", airportName)))
				.when(this.validator).validateCreateAiportModel(model);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createAirport(model));
		
		assertEquals(MessageFormat.format("An aiport with the name [{0}] already exists. Find another one", airportName), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createAirport(model));
	}
	
	@Test
	void createAirport_throwsValidatorException_whenRunWaysOutOfRange() throws ValidatorException {
		CreateAddressModel addressModel = Mockito.mock(CreateAddressModel.class);
		
		CreateAirportModel model = new CreateAirportModel("name", 10, 2, addressModel, Collections.emptyList());
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.EXPECTATION_FAILED,
				"The number of run ways has to be in between 1 and 8"))
				.when(this.validator).validateCreateAiportModel(model);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createAirport(model));
		
		assertEquals("The number of run ways has to be in between 1 and 8", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createAirport(model));
	}
	
	@Test
	void createAirport_throwsValidatorException_whenGateWaysOutOfRange() throws ValidatorException {
		CreateAddressModel addressModel = Mockito.mock(CreateAddressModel.class);
		
		CreateAirportModel model = new CreateAirportModel("name", 5, 2, addressModel, Collections.emptyList());
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.EXPECTATION_FAILED,
				"The number of run ways has to be in between 1 and 200"))
				.when(this.validator).validateCreateAiportModel(model);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createAirport(model));
		
		assertEquals("The number of run ways has to be in between 1 and 200", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createAirport(model));
	}
	
	@Test
	void createAirport_returnsTrue_whenAllConditionsGood() throws ValidatorException {
		String companyName1 = "comapany1";
		String companyName2 = "comapany2";
		
		CreateAddressModel addressModel = Mockito.mock(CreateAddressModel.class);
		
		Address address = Mockito.mock(Address.class);
		
		Company company1 = Mockito.mock(Company.class);
		Company company2 = Mockito.mock(Company.class);
		
		Airport airport = new Airport();
		airport.setAirportName("airportName");
		
		CreateAirportModel model = new CreateAirportModel("name", 5, 2, addressModel, Arrays.asList(companyName1, companyName2));
		
		Mockito.lenient().when(this.airportConverter.convertCreateModelToEntity(model)).thenReturn(airport);
		Mockito.lenient().when(this.addressRepository.findByAllAttributes(
				addressModel.getCountry(), 
				addressModel.getCity(), 
				addressModel.getStreet(),
				addressModel.getStreetNumber(),
				addressModel.getApartment()
				)).thenReturn(Optional.of(address));
		Mockito.lenient().when(this.companyRepository.findCompanyByName(companyName1)).thenReturn(Optional.of(company1));
		Mockito.lenient().when(this.companyRepository.findCompanyByName(companyName2)).thenReturn(Optional.of(company2));
		
			
		assertTrue(this.service.createAirport(model));
	}
	
	@Test
	void editAirport_throwsFlightManagerException_whenCantFindAirport() {
		String codeIdentifier = "notExistingCodeIdentifier";
		
		EditAirportModel model = new EditAirportModel(codeIdentifier, 2, 7, null);
		
		Mockito.when(this.repository.findByCodeIdentifier(codeIdentifier)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.editAirport(model));
		
		assertEquals(MessageFormat.format("Can not perform the edit action. Airport [{0}] not found", codeIdentifier), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.editAirport(model));
	}
	
	@Test
	void editAirport_throwsValidatorException_whenRunWaysOutOfRangeWithAdress() throws ValidatorException {
		String codeIdentifier = "code";
		
		EditAirportModel model = new EditAirportModel(codeIdentifier, 2, 7, null);
		
		Airport airport = new Airport();
		
		Mockito.when(this.repository.findByCodeIdentifier(codeIdentifier)).thenReturn(Optional.of(airport));
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.EXPECTATION_FAILED,
				"The number of run ways has to be in between 1 and 8"))
				.when(this.validator).validateEditAirport(model);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.editAirport(model));
		
		assertEquals("The number of run ways has to be in between 1 and 8", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.editAirport(model));
	}
	
	@Test
	void editAirport_throwsValidatorException_whenGateWaysOutOfRangeWithAddress() throws ValidatorException {
		String codeIdentifier = "code";
		
		EditAirportModel model = new EditAirportModel(codeIdentifier, 2, 7, null);
		
		Airport airport = new Airport();
		
		Mockito.when(this.repository.findByCodeIdentifier(codeIdentifier)).thenReturn(Optional.of(airport));
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.EXPECTATION_FAILED,
				"The number of run ways has to be in between 1 and 200"))
				.when(this.validator).validateEditAirport(model);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.editAirport(model));
		
		assertEquals("The number of run ways has to be in between 1 and 200", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.editAirport(model));
	}
	
	@Test
	void editAirport_returnsTrue_whenAllCondotionsGood01withAdreessNull() throws ValidatorException {
		String codeIdentifier = "code";
		
		EditAirportModel model = new EditAirportModel(codeIdentifier, 2, 7, null);
		
		Airport airport = new Airport();
		
		Mockito.when(this.repository.findByCodeIdentifier(codeIdentifier)).thenReturn(Optional.of(airport));
		
		assertTrue(this.service.editAirport(model));
	}
	
	@Test
	void editAirport_returnsTrue_whenAllCondotionsGood01withAdreess() throws ValidatorException {
		String codeIdentifier = "code";
		
		CreateAddressModel addressModel = Mockito.mock(CreateAddressModel.class);
		
		Address address = Mockito.mock(Address.class);
		
		EditAirportModel model = new EditAirportModel(codeIdentifier, 2, 7, addressModel);
		
		Airport airport = Mockito.mock(Airport.class);
		
		Mockito.when(this.repository.findByCodeIdentifier(codeIdentifier)).thenReturn(Optional.of(airport));
		Mockito.lenient().when(this.addressRepository.findByAllAttributes(
				addressModel.getCountry(), 
				addressModel.getCity(), 
				addressModel.getStreet(),
				addressModel.getStreetNumber(),
				addressModel.getApartment()
				)).thenReturn(Optional.of(address));
		
		assertTrue(this.service.editAirport(model));
	}
	
	@Test
	void removeAirport_throwsFlightManagerException_whenCantFindAirport() {
		String codeIdentifier = "notExistingCodeIdentifier";
		
		
		Mockito.when(this.repository.findByCodeIdentifier(codeIdentifier)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.removeAirport(codeIdentifier));
		
		assertEquals(MessageFormat.format("Can not delete airport. Airport [{0}] not found", codeIdentifier), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.removeAirport(codeIdentifier));
	}
	
	@Test
	void removeAirport_returnsTrue_whenAllConditionsGood01() {
		String codeIdentifier = "codeIdentifier";
		
		FlightTemplate flightTemplate1 = new FlightTemplate();
		flightTemplate1.setFrom(true);
		flightTemplate1.setTo(true);
		
		FlightTemplate flightTemplate2 = new FlightTemplate();
		flightTemplate2.setFrom(true);
		flightTemplate2.setTo(true);
		
		Flight flight1 = new Flight();
		flight1.setFlightTemplate(flightTemplate1);
		flight1.setActiv(true);
		flight1.setCanceled(false);
		
		Flight flight2 = new Flight();
		flight2.setFlightTemplate(flightTemplate2);
		flight2.setActiv(true);
		flight2.setCanceled(false);
		
		Airport airport = new Airport();
		airport.getFlightsStart().add(flight1);
		airport.getFlightsEnd().add(flight2);
		
		
		Mockito.when(this.repository.findByCodeIdentifier(codeIdentifier)).thenReturn(Optional.of(airport));
		
		assertTrue(this.service.removeAirport(codeIdentifier));
	}
	
	@Test
	void removeAirport_returnsTrue_whenAllConditionsGood02() {
		String codeIdentifier = "codeIdentifier";
		
		Flight flight1 = new Flight();
		flight1.setActiv(false);
		
		Flight flight2 = new Flight();
		flight2.setActiv(false);
		
		Airport airport = new Airport();
		airport.getFlightsStart().add(flight1);
		airport.getFlightsEnd().add(flight2);
		
		
		Mockito.when(this.repository.findByCodeIdentifier(codeIdentifier)).thenReturn(Optional.of(airport));
		
		assertTrue(this.service.removeAirport(codeIdentifier));
	}
	
	@Test
	void addCompanyCollab_throwsFlightManagerException_whenCantFindAirport() {
		String codeIdentifier = "notExistingCodeIdentifier";
		
		ActionCompanyAirportCollab model = new ActionCompanyAirportCollab(codeIdentifier, "newCompanyName");
		
		Mockito.when(this.repository.findByCodeIdentifier(codeIdentifier)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.addCompanyCollab(model));
		
		assertEquals(MessageFormat.format("Can not add company collaboration to airport. Airport [{0}] not found",
				model.getAirportCodeIdentifier()), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.addCompanyCollab(model));
	}
	
	@Test
	void addCompanyCollab_throwsFlightManagerException_whenCantFindTheNewCompany() {
		String codeIdentifier = "codeIdentifier";
		String newCompanyName = "notExistingCompanyName";
		
		Airport airport = Mockito.mock(Airport.class);
		
		ActionCompanyAirportCollab model = new ActionCompanyAirportCollab(codeIdentifier, newCompanyName);
		
		Mockito.when(this.repository.findByCodeIdentifier(codeIdentifier)).thenReturn(Optional.of(airport));
		Mockito.when(this.companyRepository.findCompanyByName(newCompanyName)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.addCompanyCollab(model));
		
		assertEquals(MessageFormat.format("Can not add company collaboration to airport. Company [{0}] not found",
				model.getNewCompanyName()), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.addCompanyCollab(model));
	}
	
	@Test
	void addCompanyCollab_returnsTrue_whenAllConditionsGood() {
		String codeIdentifier = "codeIdentifier";
		String newCompanyName = "companyName";
		
		Airport airport = Mockito.mock(Airport.class);
		Company newCompany = Mockito.mock(Company.class);
		
		ActionCompanyAirportCollab model = new ActionCompanyAirportCollab(codeIdentifier, newCompanyName);
		
		Mockito.when(this.repository.findByCodeIdentifier(codeIdentifier)).thenReturn(Optional.of(airport));
		Mockito.when(this.companyRepository.findCompanyByName(newCompanyName)).thenReturn(Optional.of(newCompany));
		
		assertTrue(this.service.addCompanyCollab(model));
	}
	
	@Test
	void removeCompanyCollab_throwsFlightManagerException_whenCantFindAirport() {
		String codeIdentifier = "notExistingCodeIdentifier";
		
		ActionCompanyAirportCollab model = new ActionCompanyAirportCollab(codeIdentifier, "newCompanyName");
		
		Mockito.when(this.repository.findByCodeIdentifier(codeIdentifier)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.removeCompanyCollab(model));
		
		assertEquals(MessageFormat.format("Can not remove company collaboration to airport. Airport [{0}] not found",
				model.getAirportCodeIdentifier()), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.removeCompanyCollab(model));
	}
	
	@Test
	void removeCompanyCollab_throwsFlightManagerException_whenCantFindTheNewCompany() {
		String codeIdentifier = "codeIdentifier";
		String newCompanyName = "notExistingCompanyName";
		
		Airport airport = Mockito.mock(Airport.class);
		
		ActionCompanyAirportCollab model = new ActionCompanyAirportCollab(codeIdentifier, newCompanyName);
		
		Mockito.when(this.repository.findByCodeIdentifier(codeIdentifier)).thenReturn(Optional.of(airport));
		Mockito.when(this.companyRepository.findCompanyByName(newCompanyName)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.removeCompanyCollab(model));
		
		assertEquals(MessageFormat.format("Can not remove company collaboration to airport. Company [{0}] not found",
				model.getNewCompanyName()), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.removeCompanyCollab(model));
	}
	
	@Test
	void removeCompanyCollab_returnsTrue_whenAllConditionsGood() {
		String codeIdentifier = "codeIdentifier";
		String newCompanyName = "companyName";
		
		Airport airport = Mockito.mock(Airport.class);
		Company newCompany = Mockito.mock(Company.class);
		
		ActionCompanyAirportCollab model = new ActionCompanyAirportCollab(codeIdentifier, newCompanyName);
		
		Mockito.when(this.repository.findByCodeIdentifier(codeIdentifier)).thenReturn(Optional.of(airport));
		Mockito.when(this.companyRepository.findCompanyByName(newCompanyName)).thenReturn(Optional.of(newCompany));
		
		assertTrue(this.service.removeCompanyCollab(model));
	}
	
	
	
	
	
	
	
	
	
	
	

}
