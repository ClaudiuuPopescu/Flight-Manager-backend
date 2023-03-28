package msg.project.flightmanager.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.dto.AirportDto;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.modelHelper.CreateAddressModel;
import msg.project.flightmanager.modelHelper.CreateAirportModel;
import msg.project.flightmanager.modelHelper.EditAirportModel;
import msg.project.flightmanager.repository.AirportRepository;
import msg.project.flightmanager.repository.CompanyRepository;
import msg.project.flightmanager.service.utils.StringUtils;
import msg.project.flightmanager.validator.AddressValidator;
import msg.project.flightmanager.validator.AirportValidator;

@ExtendWith(MockitoExtension.class)
public class AirportValidatorTest {
	@InjectMocks
	private AirportValidator airportValidator;
	@Mock
	private AirportRepository airportRepository;
	@Mock
	private AddressValidator addressValidator;
	@Mock
	private CompanyRepository companyRepository;
	
	private AirportDto airportDto;
	private CreateAirportModel createAirportModel;
	private EditAirportModel editAirportModel;
	
	@BeforeEach
	void init() {
		this.airportDto = new AirportDto("name", "", 5, 5, Mockito.mock(AddressDto.class));
		this.createAirportModel = new CreateAirportModel("name", 5, 5, Mockito.mock(CreateAddressModel.class), Collections.emptyList());
		this.editAirportModel = new EditAirportModel("", 5, 5, Mockito.mock(CreateAddressModel.class));
	}
	
	@Test
	void validateAirport_throwsValidatorException_whenNameIsEmpty() throws ValidatorException{
		String airportName = "";
		this.airportDto.setAirportName(airportName);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.airportValidator.validateAirport(this.airportDto));
		
		assertEquals("The airport name cannot be empty!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.airportValidator.validateAirport(this.airportDto));
	}
	
	@Test
	void validateAirport_throwsValidatorException_whenNameTooLong() throws ValidatorException{
		String airportName = "aTooLongAirportNameToPassThisTest";
		this.airportDto.setAirportName(airportName);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.airportValidator.validateAirport(this.airportDto));
		
		assertEquals("The airport name is too long!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.airportValidator.validateAirport(this.airportDto));
	}
	
	@Test
	void validateAirport_throwsValidatorException_whenNameContainsLetters() throws ValidatorException{
		String airportName = "airportName123";
		this.airportDto.setAirportName(airportName);
		
		try (final MockedStatic<StringUtils> mocked = Mockito.mockStatic(StringUtils.class)) {
			mocked.when(() -> StringUtils.isAsciiPrintable(this.airportDto.getAirportName())).thenReturn(false);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.airportValidator.validateAirport(this.airportDto));
		
		assertEquals("The airport name should be only out of letters!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.airportValidator.validateAirport(this.airportDto));
		}
	}
	
	@Test
	void validateAirport_throwsValidatorException_whenNameAlreadyExists() throws ValidatorException{
		String airportName = "existingAirportName";
		this.airportDto.setAirportName(airportName);
		
		Airport airportNameTaken = Mockito.mock(Airport.class);
		
		Mockito.when(this.airportRepository.findByName(airportName)).thenReturn(Optional.of(airportNameTaken));
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.airportValidator.validateAirport(this.airportDto));
		
		assertEquals(MessageFormat.format("An aiport with the name [{0}] already exists. Find another one", airportName),
				thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.airportValidator.validateAirport(this.airportDto));
	}
	
	@Test
	void validateAirport_throwsFlightManagerException_whenRunWaysOuttaRange01() {
		int runWays = 47;
		this.airportDto.setRunWarys(runWays);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.airportValidator.validateAirport(this.airportDto));
		
		assertEquals("The number of run ways has to be in between 1 and 8", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.airportValidator.validateAirport(this.airportDto));
	}
	
	@Test
	void validateAirport_throwsFlightManagerException_whenRunWaysOuttaRange02() {
		int runWays = 0;
		this.airportDto.setRunWarys(runWays);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.airportValidator.validateAirport(this.airportDto));
		
		assertEquals("The number of run ways has to be in between 1 and 8", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.airportValidator.validateAirport(this.airportDto));
	}
	
	@Test
	void validateAirport_throwsFlightManagerException_whenGateWaysOuttaRange01() {
		int gateWays = 300;
		this.airportDto.setGateWays(gateWays);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.airportValidator.validateAirport(this.airportDto));
		
		assertEquals("The number of gate ways has to be in between 1 and 200", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.airportValidator.validateAirport(this.airportDto));
	}
	
	@Test
	void validateAirport_throwsFlightManagerException_whenGateWaysOuttaRange02() {
		int gateWays = 0;
		this.airportDto.setGateWays(gateWays);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.airportValidator.validateAirport(this.airportDto));
		
		assertEquals("The number of gate ways has to be in between 1 and 200", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.airportValidator.validateAirport(this.airportDto));
	}
	
	@Test
	void validateAirport_retunsVoid_whenAddressDtoGood() throws ValidatorException {
		
		this.airportValidator.validateAirport(this.airportDto);
		verify(this.addressValidator).validateAddressDto(this.airportDto.getAddressDto());
	}
	
	@Test
	void validateCreateAiportModel_throwsValidatorException_whenNameIsEmpty() throws ValidatorException{
		String airportName = "";
		this.createAirportModel.setAirportName(airportName);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
		
		assertEquals("The airport name cannot be empty!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
	}
	
	@Test
	void validateCreateAiportModel_throwsValidatorException_whenNameTooLong() throws ValidatorException{
		String airportName = "aTooLongAirportNameToPassThisTest";
		this.createAirportModel.setAirportName(airportName);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
		
		assertEquals("The airport name is too long!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
	}
	
	@Test
	void validateCreateAiportModel_throwsValidatorException_whenNameContainsLetters() throws ValidatorException{
		String airportName = "airportName123";
		this.createAirportModel.setAirportName(airportName);
		
		try (final MockedStatic<StringUtils> mocked = Mockito.mockStatic(StringUtils.class)) {
			mocked.when(() -> StringUtils.isAsciiPrintable(this.airportDto.getAirportName())).thenReturn(false);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
		
		assertEquals("The airport name should be only out of letters!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
		}
	}
	
	@Test
	void validateCreateAiportModel_throwsValidatorException_whenNameAlreadyExists() throws ValidatorException{
		String airportName = "existingAirportName";
		this.createAirportModel.setAirportName(airportName);
		
		Airport airportNameTaken = Mockito.mock(Airport.class);
		
		Mockito.when(this.airportRepository.findByName(airportName)).thenReturn(Optional.of(airportNameTaken));
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
		
		assertEquals(MessageFormat.format("An aiport with the name [{0}] already exists. Find another one", airportName),
				thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
	}
	
	@Test
	void validateCreateAiportModel_throwsFlightManagerException_whenRunWaysOuttaRange01() {
		int runWays = 47;
		this.createAirportModel.setRunWarys(runWays);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
		
		assertEquals("The number of run ways has to be in between 1 and 8", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
	}
	
	@Test
	void validateCreateAiportModel_throwsFlightManagerException_whenRunWaysOuttaRange02() {
		int runWays = 0;
		this.createAirportModel.setRunWarys(runWays);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
		
		assertEquals("The number of run ways has to be in between 1 and 8", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
	}
	
	@Test
	void validateCreateAiportModel_throwsFlightManagerException_whenGateWaysOuttaRange01() {
		int gateWays = 300;
		this.createAirportModel.setGateWays(gateWays);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
		
		assertEquals("The number of gate ways has to be in between 1 and 200", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
	}
	
	@Test
	void validateCreateAiportModel_throwsFlightManagerException_whenGateWaysOuttaRange02() {
		int gateWays = 0;
		this.createAirportModel.setGateWays(gateWays);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
		
		assertEquals("The number of gate ways has to be in between 1 and 200", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
	}
	
	@Test
	void validateCreateAirportModel_throwsFligtManagerException_whenCantFindCompanyCollab() {
		String companyName = "company";
		List<String> companies = new ArrayList<String>();
		companies.add(companyName);
		
		this.createAirportModel.setCompanyNames_toCollab(companies);
		
		Mockito.when(this.companyRepository.findCompanyByName(companyName)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
		
		assertEquals(MessageFormat.format("Company [{0}] can not be added to collaboration. It does not exist.", companyName),
				thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.airportValidator.validateCreateAiportModel(this.createAirportModel));
	}
	
	@Test
	void validateCreateAirportModel_returnsVoid_whenAllConditionsGood() throws ValidatorException {
		String companyName = "company";
		List<String> companies = new ArrayList<String>();
		companies.add(companyName);
		
		this.createAirportModel.setCompanyNames_toCollab(companies);
		
		Company company = Mockito.mock(Company.class);
		
		Mockito.when(this.companyRepository.findCompanyByName(companyName)).thenReturn(Optional.of(company));
		
		this.airportValidator.validateCreateAiportModel(this.createAirportModel);
	}
	
	@Test
	void validateEditAirport_throwsFlightManagerException_whenRunWaysOuttaRange01() {
		int runWays = 47;
		this.editAirportModel.setRunWarys(runWays);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.airportValidator.validateEditAirport(this.editAirportModel));
		
		assertEquals("The number of run ways has to be in between 1 and 8", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.airportValidator.validateEditAirport(this.editAirportModel));
	}
	
	@Test
	void validateEditAirport_throwsFlightManagerException_whenRunWaysOuttaRange02() {
		int runWays = 0;
		this.editAirportModel.setRunWarys(runWays);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.airportValidator.validateEditAirport(this.editAirportModel));
		
		assertEquals("The number of run ways has to be in between 1 and 8", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.airportValidator.validateEditAirport(this.editAirportModel));
	}
	
	@Test
	void validateEditAirport_throwsFlightManagerException_whenGateWaysOuttaRange01() {
		int gateWays = 300;
		this.editAirportModel.setGateWays(gateWays);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.airportValidator.validateEditAirport(this.editAirportModel));
		
		assertEquals("The number of gate ways has to be in between 1 and 200", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.airportValidator.validateEditAirport(this.editAirportModel));
	}
	
	@Test
	void validateEditAirport_throwsFlightManagerException_whenGateWaysOuttaRange02() {
		int gateWays = 0;
		this.editAirportModel.setGateWays(gateWays);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.airportValidator.validateEditAirport(this.editAirportModel));
		
		assertEquals("The number of gate ways has to be in between 1 and 200", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.airportValidator.validateEditAirport(this.editAirportModel));
	}
}
