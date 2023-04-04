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

import msg.project.flightmanager.converter.PlaneConverter;
import msg.project.flightmanager.exceptions.CompanyException;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.exceptions.ValidatorException;
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

	@Test
	void getAll_throwsFlightManagerException_whenNoPlaneFound() {
		
		Mockito.when(this.repository.findAll()).thenReturn(Collections.emptyList());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.getAll());
		
		assertEquals("No planes found", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.getAll());
	}
	
	@Test
	void getAll_returnsListOfPlanes_whenPlanesExistInDataBase() {
		Plane first_plane = new Plane();
		Plane second_plane = new Plane();
		
		Mockito.when(this.repository.findAll()).thenReturn(Arrays.asList(first_plane, second_plane));
		
		assertEquals(2,this.service.getAll().size());
	}
	
	@Test
	void createPlane_throwsFlightManagerException_whenPlaneModelNull() throws ValidatorException {
		CreatePlaneModel model = new CreatePlaneModel(null , 1, 2, 3, "15/10/2020", "small");
		
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
		CreatePlaneModel model = new CreatePlaneModel("" , 1, 2, 3, "15/10/2020", "small");
		
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
		CreatePlaneModel model = new CreatePlaneModel(planeModel , 1, 2, 3, "15/10/2020", "small");
		
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
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , tailNumber, 2, 3, "15/10/2020", "small");
		
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
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , tailNumber, 2, 3, "15/10/2020", "small");
		
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
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , 1, capacity, 3, "15/10/2020", "small");
		
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
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , 1, 5, fuelCapacity, "15/10/2020", "small");
		
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
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , 1, 5, fuelCapacity, "15/10/2020", "mid");
		
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
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , 1, 5, fuelCapacity, "15/10/2020", "wide");
		
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
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , 1, 5, fuelCapacity, "15/10/2020", "jumbo");
		
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
		String manufacturingDate = "10/07/2024";
		
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
		
		CreatePlaneModel model = new CreatePlaneModel("pm" , 1, 5, 2000, "10/07/2024", "small");
		
		Plane plane = new Plane();
		
		Mockito.when(this.converter.createModelToEntity(model)).thenReturn(plane);
		
		assertTrue(this.service.createPlane(model));
	}
	
	@Test
	void editLastRevisionPlane_throwsFlightManagerException_whenPlaneNotFound() {
		EditLastRevisionPlaneModel editModel = new EditLastRevisionPlaneModel(99, "03/04/2023");
		
		Mockito.when(this.repository.findByTailNumber(editModel.getTailNumber())).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.editLastRevisionPlane(editModel));
		
		assertEquals(MessageFormat.format("Plane with tail number [{0}] not found", editModel.getTailNumber()), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.editLastRevisionPlane(editModel));
	}
	
	@Test
	void editLastRevisionPlane_returnsTrue_whenAllConditionsGood() {
		Optional<Plane> optionalPlane = Optional.of(new Plane());
		
		EditLastRevisionPlaneModel editModel = new EditLastRevisionPlaneModel(99, "03/04/2023");
		
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
