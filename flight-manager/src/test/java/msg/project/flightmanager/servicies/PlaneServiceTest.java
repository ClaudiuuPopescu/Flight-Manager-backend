package msg.project.flightmanager.servicies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import msg.project.flightmanager.converter.PlaneConverter;
import msg.project.flightmanager.enums.PlaneSize;
import msg.project.flightmanager.exceptions.CompanyException;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.jsonModule.JSONModuleBeanManagement;
import msg.project.flightmanager.jsonModule.LocalDateModule;
import msg.project.flightmanager.jsonModule.PlaneSizeModule;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.FlightTemplate;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.modelHelper.CreatePlaneModel;
import msg.project.flightmanager.modelHelper.EditLastRevisionPlaneModel;
import msg.project.flightmanager.repository.CompanyRepository;
import msg.project.flightmanager.repository.PlaneRepository;
import msg.project.flightmanager.service.PlaneService;
import msg.project.flightmanager.validator.PlaneValidator;

@ExtendWith(MockitoExtension.class)
public class PlaneServiceTest {
	@InjectMocks
	private PlaneService service;
	@Mock
	private PlaneValidator validator;
	@Mock
	private PlaneConverter converter;
	@Mock
	private PlaneRepository repository;
	@Mock
	private CompanyRepository companyRepository;
	@Mock
	private JSONModuleBeanManagement jsonModuleBeanManagement;

	@Test
	void getAll_throwsFlightManagerException_whenNoPlaneFound() {
		
		Mockito.when(this.repository.findAll()).thenReturn(Collections.emptyList());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.getAll());
		
		assertEquals("No planes found", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.getAll());
	}
	
	@Test
	void getAll_throwsFlightManagerException_whenFailedMappingToJSON() throws JsonProcessingException {
		Plane plane = Mockito.mock(Plane.class);
		
		List<Plane> planes = new ArrayList<>();
		planes.add(plane);
		
		ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
		LocalDateModule localDateModule = Mockito.mock(LocalDateModule.class);
		objectMapper.registerModule(localDateModule);
		
		Mockito.when(this.jsonModuleBeanManagement.getObjectMapper()).thenReturn(objectMapper);
		Mockito.when(this.jsonModuleBeanManagement.getLocalDateModule()).thenReturn(localDateModule);
		Mockito.when(this.repository.findAll()).thenReturn(Arrays.asList(plane));
		
		Mockito.doThrow(JsonProcessingException.class)
		.when(objectMapper).writeValueAsString(planes);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.getAll());

		assertEquals("Can not get all planes. Could not serialize the list of planes", thrown.getMessage());
	}
	
	@Test
	void getAll_returnsPlanesAsJSON_whenPlanesExistInDatabase() {
		Plane first_plane = Plane.builder()
				.capacity(32)
				.fuelTankCapacity(4500)
				.manufacturingDate(LocalDate.of(2017, 5, 27))
				.firstFlight(LocalDate.of(2017, 7, 1))
				.lastRevision(LocalDate.of(2022, 12, 12))
				.size(PlaneSize.SMALL)
				.model("cool")
				.tailNumber(47).build();
		
		List<Plane> planes = new ArrayList<>();
		planes.add(first_plane);
		
		ObjectMapper objectMapper = new ObjectMapper();
		LocalDateModule localDateModule = new LocalDateModule();
		PlaneSizeModule planeSizeModule = new PlaneSizeModule();
		objectMapper.registerModule(localDateModule);
		objectMapper.registerModule(planeSizeModule);
		
		Mockito.when(this.jsonModuleBeanManagement.getObjectMapper()).thenReturn(objectMapper);
		Mockito.when(this.jsonModuleBeanManagement.getLocalDateModule()).thenReturn(localDateModule);
		Mockito.when(this.jsonModuleBeanManagement.getPlaneSizeEnumModule()).thenReturn(planeSizeModule);
		Mockito.when(this.repository.findAll()).thenReturn(planes);
		
		String output = "[{\"capacity\":32,\"fuelTankCapacity\":4500,\"manufacturingDate\":\"2017-05-27\",\"firstFlight\":\"2017-07-01\",\"lastRevision\":\"2022-12-12\",\"size\":\"small\",\"model\":\"cool\",\"tailNumber\":47}]";
		assertEquals(output, this.service.getAll());
	}
	
	@Test
	void createPlane_throwsFlightManagerException_whenPlaneModelNull() throws ValidatorException {
		CreatePlaneModel model = new CreatePlaneModel(null , 1, 2, 3, LocalDate.of(2020, 10, 15), "small");
		
		Mockito.doThrow(new FlightManagerException(
				HttpStatus.EXPECTATION_FAILED,
				"The model fileld can not be null"))
		.when(this.validator).validateCreatePlaneModel(model);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createPlane(model));
		
		assertEquals("The model fileld can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createPlane(model));
	}
	
	@Test
	void createPlane_throwsValidatorException_whenPlaneModelEmpty() throws ValidatorException {
		CreatePlaneModel model = new CreatePlaneModel("" , 1, 2, 3, LocalDate.of(2020, 10, 15), "small");
		
		Mockito.doThrow(new ValidatorException(
				"The model field cannot be empty",
				ErrorCode.EMPTY_FIELD))
		.when(this.validator).validateCreatePlaneModel(model);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.service.createPlane(model));
		
		assertEquals("The model field cannot be empty", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.service.createPlane(model));
	}
	
	@Test
	void createPlane_throwsValidatorException_whenPlaneModelTooLong() throws ValidatorException {
		String planeModel = "thisIsAVeryLongString";
		CreatePlaneModel model = new CreatePlaneModel(planeModel , 1, 2, 3, LocalDate.of(2020, 10, 15), "small");
		
		Mockito.doThrow(new ValidatorException(
				"The model is too long",
				ErrorCode.IS_TOO_LONG))
		.when(this.validator).validateCreatePlaneModel(model);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.service.createPlane(model));
		
		assertEquals("The model is too long", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.service.createPlane(model));
	}
	
	@Test
	void createPlane_throwsFlightManagerException_whenTailNumberNull() throws ValidatorException {
		int tailNumber = -5;
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , tailNumber, 2, 3, LocalDate.of(2020, 10, 15), "small");
		
		Mockito.doThrow(new FlightManagerException(
						HttpStatus.IM_USED,
						MessageFormat.format("Tail number [{0}] invalid, numbers bellow 0 are not accepted", tailNumber)))
				.when(this.validator).validateCreatePlaneModel(model);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createPlane(model));
		
		assertEquals(MessageFormat.format("Tail number [{0}] invalid, numbers bellow 0 are not accepted", tailNumber), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createPlane(model));
	}
	
	@Test
	void createPlane_throwsFlightManagerException_whenTailNumberAlreadyExists() throws ValidatorException {
		int tailNumber = 1;
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , tailNumber, 2, 3, LocalDate.of(2020, 10, 15), "small");
		
		Mockito.doThrow(new FlightManagerException(
					HttpStatus.IM_USED,
					MessageFormat.format("Tail number [{0}] taken, take another one", tailNumber)))
				.when(this.validator).validateCreatePlaneModel(model);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createPlane(model));
		
		assertEquals(MessageFormat.format("Tail number [{0}] taken, take another one", tailNumber), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createPlane(model));
	}
	
	@Test
	void createPlane_throwsValidatorException_whenCapacityOutOfRange() throws ValidatorException {
		int capacity = 853;
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , 1, capacity, 3, LocalDate.of(2020, 10, 15), "small");
		
		Mockito.doThrow(new ValidatorException(
				"The capacity should be between 2 and 852",
				ErrorCode.WRONG_INTERVAL))
				.when(this.validator).validateCreatePlaneModel(model);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.service.createPlane(model));
		
		assertEquals("The capacity should be between 2 and 852", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.service.createPlane(model));
	}
	
	@Test
	void createPlane_throwsValidatorException_whenFuelTankCapaicityOutOfRangeForPlaneSizeSMALL() throws ValidatorException {
		int fuelCapacity = 6000; 
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , 1, 5, fuelCapacity, LocalDate.of(2020, 10, 15), "small");
		
		Mockito.doThrow(new ValidatorException(
					"The fuel capacity of a small plane should be between 4000 and 5000!",
					ErrorCode.WRONG_INTERVAL))
				.when(this.validator).validateCreatePlaneModel(model);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.service.createPlane(model));
		
		assertEquals("The fuel capacity of a small plane should be between 4000 and 5000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.service.createPlane(model));
	}
	
	@Test
	void createPlane_throwsValidatorException_whenFuelTankCapaicityOutOfRangeForPlaneSizeMID() throws ValidatorException {
		int fuelCapacity = 45000; 
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , 1, 5, fuelCapacity, LocalDate.of(2020, 10, 15), "mid");
		
		Mockito.doThrow(new ValidatorException(
					"The fuel capacity of a mid plane should be between 26000 and 30000!",
					ErrorCode.WRONG_INTERVAL))
				.when(this.validator).validateCreatePlaneModel(model);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.service.createPlane(model));
		
		assertEquals("The fuel capacity of a mid plane should be between 26000 and 30000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.service.createPlane(model));
	}
	
	@Test
	void createPlane_throwsValidatorException_whenFuelTankCapaicityOutOfRangeForPlaneSizeWIDE() throws ValidatorException {
		int fuelCapacity = 45000; 
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , 1, 5, fuelCapacity, LocalDate.of(2020, 10, 15), "wide");
		
		Mockito.doThrow(new ValidatorException(
					"The fuel capacity of a wide plane should be between 130000 and 190000!",
					ErrorCode.WRONG_INTERVAL))
				.when(this.validator).validateCreatePlaneModel(model);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.service.createPlane(model));
		
		assertEquals("The fuel capacity of a wide plane should be between 130000 and 190000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.service.createPlane(model));
	}
	
	@Test
	void createPlane_throwsValidatorException_whenFuelTankCapaicityOutOfRangeForPlaneSizeJUMBO() throws ValidatorException {
		int fuelCapacity = 500000; 
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , 1, 5, fuelCapacity, LocalDate.of(2020, 10, 15), "jumbo");
		
		Mockito.doThrow(new ValidatorException(
					"The fuel capacity of a jumbo plane should be between 200000 and 323000!",
					ErrorCode.WRONG_INTERVAL))
				.when(this.validator).validateCreatePlaneModel(model);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.service.createPlane(model));
		
		assertEquals("The fuel capacity of a jumbo plane should be between 200000 and 323000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.service.createPlane(model));
	}
	
	@Test
	void createPlane_throwsFlightManagerException_whenManufacturingDateNull() throws ValidatorException {
		CreatePlaneModel model = new CreatePlaneModel("pm" , 1, 5, 2000, null, "small");

		Mockito.doThrow(new FlightManagerException(
				HttpStatus.EXPECTATION_FAILED, "Manufacturing date can not be null"))
			.when(this.validator).validateCreatePlaneModel(model);
	
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
			() -> this.service.createPlane(model));
	
	assertEquals("Manufacturing date can not be null", thrown.getMessage());
	assertThrows(FlightManagerException.class, () -> this.service.createPlane(model));
	}
	
	@Test
	void createPlane_throwsFlightManagerException_whenManufacturingDateIsNotInThePast() throws ValidatorException {
		LocalDate manufacturingDate = LocalDate.of(2024, 10, 15);
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , 1, 5, 2000, manufacturingDate, "small");

		Mockito.doThrow(new FlightManagerException(
				HttpStatus.EXPECTATION_FAILED, "Plane's manufacturing date shoud be in the past!"))
			.when(this.validator).validateCreatePlaneModel(model);
	
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
			() -> this.service.createPlane(model));
	
	assertEquals("Plane's manufacturing date shoud be in the past!", thrown.getMessage());
	assertThrows(FlightManagerException.class, () -> this.service.createPlane(model));
	}
	
	@Test
	void createPlane_returnsTrue_whenAllConditionsGood() throws ValidatorException {
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , 1, 5, 2000, LocalDate.of(2022, 10, 15), "small");
		
		Plane plane = new Plane();
		
		Mockito.when(this.converter.createModelToEntity(model)).thenReturn(plane);
		
		assertTrue(this.service.createPlane(model));
	}
	
	@Test
	void editLastRevisionPlane_throwsFlightManagerException_whenPlaneNotFound() {
		EditLastRevisionPlaneModel editModel = new EditLastRevisionPlaneModel(99, LocalDate.of(2024, 10, 15));
		
		Mockito.when(this.repository.findByTailNumber(editModel.getTailNumber())).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.editLastRevisionPlane(editModel));
		
		assertEquals(MessageFormat.format("Plane with tail number [{0}] not found", editModel.getTailNumber()), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.editLastRevisionPlane(editModel));
	}
	
	@Test
	void editLastRevisionPlane_returnsTrue_whenAllConditionsGood() {
		Optional<Plane> optionalPlane = Optional.of(new Plane());
		
		EditLastRevisionPlaneModel editModel = new EditLastRevisionPlaneModel(99, LocalDate.of(2023, 10, 15));
		
		Mockito.when(this.repository.findByTailNumber(editModel.getTailNumber())).thenReturn(optionalPlane);
		
		assertTrue(this.service.editLastRevisionPlane(editModel));
	}
	
	@Test
	void removePlane_throwsFlightManagerException_whenPlaneNotFound() {
		int tailNumber = 99;
		
		Mockito.when(this.repository.findByTailNumber(tailNumber)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.removePlane(tailNumber));
		
		assertEquals(MessageFormat.format("Plane with tail number [{0}] not found", tailNumber), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.removePlane(tailNumber));
	}
	
	@Test
	void removePlane_returnsTrue_whenAllConditionsGood01_hasActiveFlights() {
		int tailNumber = 99;
		
		FlightTemplate flightTemplateMock = new FlightTemplate();
		flightTemplateMock.setPlane(true);
		
		Flight flightMock = new Flight();
		flightMock.setActiv(true);
		flightMock.setCanceled(false);
		flightMock.setFlightTemplate(flightTemplateMock);
		
		Plane planeMock = new Plane(); 
		planeMock.getFlights().add(flightMock);
		
		
		Optional<Plane> optionalPlane = Optional.of(planeMock);
		
		Mockito.when(this.repository.findByTailNumber(tailNumber)).thenReturn(optionalPlane);
		
		assertTrue(this.service.removePlane(tailNumber));
	}
	
	@Test
	void removePlane_returnsTrue_whenAllConditionsGood02_doesntHaveActiveFlights() {
		int tailNumber = 99;
		
		Flight flightMock = new Flight();
		flightMock.setActiv(false);
		flightMock.setCanceled(true);
		
		Plane planeMock = new Plane(); 
		planeMock.getFlights().add(flightMock);
		
		
		Optional<Plane> optionalPlane = Optional.of(planeMock);
		
		Mockito.when(this.repository.findByTailNumber(tailNumber)).thenReturn(optionalPlane);
		
		assertTrue(this.service.removePlane(tailNumber));
	}
	
	@Test
	void movePlaneToAnotherCompany_throwsFlightManagerException_whenPlaneNotFound() {
		int tailNumber = 99;
		String companyName = "someName";
		
		Mockito.when(this.repository.findByTailNumber(tailNumber)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.movePlaneToAnotherCompany(tailNumber,companyName));
		
		assertEquals(MessageFormat.format("Plane with tail number [{0}] not found", tailNumber), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.movePlaneToAnotherCompany(tailNumber, companyName));
	}
	
	@Test
	void movePlaneToAnotherCompany_throwsCompanyException_whenCompanyNotFound() {
		int tailNumber = 99;
		String companyName = "someName";
		
		Optional<Plane> optionalPlane = Optional.of(new Plane());
		
		Mockito.when(this.repository.findByTailNumber(tailNumber)).thenReturn(optionalPlane);
		Mockito.when(this.companyRepository.findCompanyByName(companyName)).thenReturn(Optional.empty());
		
		CompanyException thrown = assertThrows(CompanyException.class,
				() -> this.service.movePlaneToAnotherCompany(tailNumber,companyName));
		
		assertEquals("A company with this name does not exist!", thrown.getMessage());
		assertThrows(CompanyException.class, () -> this.service.movePlaneToAnotherCompany(tailNumber, companyName));
	}
	
	@Test
	void movePlaneToAnotherCompany_throwsFlightManagerException_whenPlaneCompanySameAsRequestedCompany() {
		int tailNumber = 99;
		String name = "someName";
		
		Company currnetCompany = new Company();
		currnetCompany.setName(name);
		Optional<Company> optionalCurrentCompany = Optional.of(currnetCompany);
		
		Plane plane = new Plane();
		plane.setCompany(currnetCompany);
		Optional<Plane> optionalPlane = Optional.of(plane);
		
		Mockito.when(this.repository.findByTailNumber(tailNumber)).thenReturn(optionalPlane);
		Mockito.when(this.companyRepository.findCompanyByName(name)).thenReturn(optionalCurrentCompany);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.movePlaneToAnotherCompany(tailNumber,name));
		
		assertEquals(MessageFormat.format("Company [{0}] already has this plane", name), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.movePlaneToAnotherCompany(tailNumber, name));
	}
	
	@Test
	void movePlaneToAnotherCompany_returnsTrue_whenAllConditionsGood() throws CompanyException, FlightManagerException {
		int tailNumber = 99;
		String name = "someName";
		
		Company toCompany = new Company();
		toCompany.setName("anotherName");
		Optional<Company> optionalAnotherCompany = Optional.of(toCompany);
		
		Company currnetCompany = new Company();
		currnetCompany.setName(name);
		
		Plane plane = new Plane();
		plane.setCompany(currnetCompany);
		Optional<Plane> optionalPlane = Optional.of(plane);
		
		Mockito.when(this.repository.findByTailNumber(tailNumber)).thenReturn(optionalPlane);
		Mockito.when(this.companyRepository.findCompanyByName(name)).thenReturn(optionalAnotherCompany);
		
		assertTrue(this.service.movePlaneToAnotherCompany(tailNumber, name));
	}
}
