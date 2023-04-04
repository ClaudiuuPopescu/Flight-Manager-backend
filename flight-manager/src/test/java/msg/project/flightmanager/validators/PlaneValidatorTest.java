package msg.project.flightmanager.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import msg.project.flightmanager.dto.PlaneDto;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.modelHelper.CreatePlaneModel;
import msg.project.flightmanager.repository.PlaneRepository;
import msg.project.flightmanager.validator.PlaneValidator;

@ExtendWith(MockitoExtension.class)
public class PlaneValidatorTest {
	@InjectMocks
	private PlaneValidator planeValidator;
	@Mock
	private PlaneRepository planeRepository;
	
	private PlaneDto planeDto;
	private CreatePlaneModel createPlaneModel;
	
	@BeforeEach
	void init() {
		this.planeDto = new PlaneDto("model", 47, 50, 4500, "18/03/2015",
				"20/05/2017", "15/07/2020", "small", null);
		
		this.createPlaneModel = new CreatePlaneModel("model", 47, 47, 4500, "18/03/2015", "small");
	}
	
	@Test
	void validatePlane_throwsFlightManagerException_whenModelNull() {
		String model = null;
		this.planeDto.setModel(model);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The model fileld can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenModelEmpty() {
		String model = "";
		this.planeDto.setModel(model);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The model field can not be empty", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenModelTooLong() {
		String model = "aTooLongModelFiledToBeValid";
		this.planeDto.setModel(model);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The model is too long", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsFlightManagerException_whenTailNumberUnder0() {
		int tailNumber = -5;
		this.planeDto.setTailNumber(tailNumber);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals(MessageFormat.format("Tail number [{0}] invalid, numbers bellow 0 are not accepted", tailNumber),
				thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsFlightManagerException_whenTailNumberAlreadyExists() {
		int tailNumber = 47;
		this.planeDto.setTailNumber(tailNumber);
		
		Plane plane = Mockito.mock(Plane.class);
		
		Mockito.when(this.planeRepository.findByTailNumber(tailNumber)).thenReturn(Optional.of(plane));
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals(MessageFormat.format("Tail number [{0}] taken, take another one", tailNumber),
				thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenCapacityOutOfRange01() {
		int capacity = -5;
		this.planeDto.setCapacity(capacity);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The capacity should be between 2 and 852", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenCapacityOutOfRange02() {
		int capacity = 888;
		this.planeDto.setCapacity(capacity);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The capacity should be between 2 and 852", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeSmall01() {
		String size = "small";
		int fuelTankCapacity = 150;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The fuel capacity of a small plane should be between 4000 and 5000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeSmall02() {
		String size = "small";
		int fuelTankCapacity = 5555;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The fuel capacity of a small plane should be between 4000 and 5000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeMid01() {
		String size = "mid";
		int fuelTankCapacity = 150;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The fuel capacity of a mid plane should be between 26000 and 30000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeMid02() {
		String size = "mid";
		int fuelTankCapacity = 35000;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The fuel capacity of a mid plane should be between 26000 and 30000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeWide01() {
		String size = "wide";
		int fuelTankCapacity = 150;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The fuel capacity of a wide plane should be between 130000 and 190000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeWide02() {
		String size = "wide";
		int fuelTankCapacity = 200000;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The fuel capacity of a wide plane should be between 130000 and 190000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeJumbo01() {
		String size = "jumbo";
		int fuelTankCapacity = 150;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The fuel capacity of a jumbo plane should be between 200000 and 323000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeJumbo02() {
		String size = "jumbo";
		int fuelTankCapacity = 350000;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The fuel capacity of a jumbo plane should be between 200000 and 323000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsFlightManagerException_whenManufacturingDateWrongFormat() {
		this.planeDto.setManufacturingDate("2000/05/07");
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("Manufacturing date must have the following format: dd/MM/yyyy", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsFlightManagerException_whenManufacturingDateNotInThePast() {
		String manufacturingDate = "03/07/2024";
		this.planeDto.setManufacturingDate(manufacturingDate);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The manufacturing date of the plane should be in the past!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenManufacturingDateLaterThanFirstFlight() {
		String firstFlight = "01/01/2020";
		String manufacturingDate = "01/01/2023";
		
		this.planeDto.setFirstFlight(firstFlight);
		this.planeDto.setManufacturingDate(manufacturingDate);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The manufacturing date of the plane should be earlier than the first flight date!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenManufacturingDateLaterThanLastRevision() {
		String firstFlight = "01/02/2023";
		String lastRevision = "01/08/2022";
		String manufacturingDate = "03/01/2023";
		
		this.planeDto.setFirstFlight(firstFlight);
		this.planeDto.setLastRevision(lastRevision);
		this.planeDto.setManufacturingDate(manufacturingDate);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The manufacturing date of the plane should be earlier than the last revision date!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsFlightManagerException_whenFirstWrongFormat() {
		this.planeDto.setFirstFlight("2017/05/02");
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("First flight date must have the following format: dd/MM/yyyy", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenFirstFightNotInThePast() {
		String firstFlight = "07/01/2025";
		this.planeDto.setFirstFlight(firstFlight);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The first flight date of the plane should be in the past!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
//	@Test Can not be tested, cuz it wouldnt pass the manufacturing date validator
//	void validatePlane_throwsValidatorException_whenFirstFightEarlierThanManufacturingDate() {
//		LocalDate firstFlight = LocalDate.of(2023, 1, 2);
//		LocalDate manufacturingDate = LocalDate.of(2023, 2, 2);
//		this.planeDto.setFirstFlight(firstFlight);
//		this.planeDto.setManufacturingDate(manufacturingDate);
//		
//		ValidatorException thrown = assertThrows(ValidatorException.class,
//				() -> this.planeValidator.validatePlane(this.planeDto));
//
//		assertEquals("The first flight date of the plane should be later than the manufacturing date!", thrown.getMessage());
//		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
//	}
	
	@Test
	void validatePlane_throwsValidatorException_whenFirstFightEarlierThanManufacturingDate() {
		String firstFlight = "02/02/2023";
		String lastRevision = "02/01/2023";
		this.planeDto.setFirstFlight(firstFlight);
		this.planeDto.setLastRevision(lastRevision);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The first flight date of the plane should be earlier than the last revision date!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsFlightManagerException_whenLastRevisionWrongFormat() {
		this.planeDto.setLastRevision("13/05-2007");
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("Last revision date must have the following format: dd/MM/yyyy", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenLastRevisionNotInThePast() {
		String lastRevision = "02/07/2023";
		this.planeDto.setLastRevision(lastRevision);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The first revision date of the plane should be in the past!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_returnsVoid_whenAllConditionsGood01() throws ValidatorException {
		String size = "small";
		int fuelTankCapacity = 4500;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		this.planeValidator.validatePlane(this.planeDto);
	}
	
	@Test
	void validatePlane_returnsVoid_whenAllConditionsGood02() throws ValidatorException {
		String size = "mid";
		int fuelTankCapacity = 27000;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		this.planeValidator.validatePlane(this.planeDto);
	}
	
	@Test
	void validatePlane_returnsVoid_whenAllConditionsGood03() throws ValidatorException {
		String size = "wide";
		int fuelTankCapacity = 135000;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		this.planeValidator.validatePlane(this.planeDto);
	}
	
	@Test
	void validatePlane_returnsVoid_whenAllConditionsGood04() throws ValidatorException {
		String size = "jumbo";
		int fuelTankCapacity = 250000;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		this.planeValidator.validatePlane(this.planeDto);
	}
	
	@Test
	void validateNewRevision_throwsFlightManagerException_whenNewRevisionDateLaterThanToday() {
		LocalDate lastRevision = LocalDate.of(2021,10,9);
		String newRevision = "02/01/2025";
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validateNewRevision(lastRevision, newRevision));

		assertEquals("New revision must be today or earlier", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validateNewRevision(lastRevision, newRevision));
	}
	
	@Test
	void validateNewRevision_throwsFlightManagerException_whenNewRevisionEarlierThanLastRevision() {
		LocalDate lastRevision = LocalDate.of(2022, 10, 9);
		String newRevision = "02/01/2017";
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validateNewRevision(lastRevision, newRevision));

		assertEquals("New revision must be later than last revision", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validateNewRevision(lastRevision, newRevision));
	}
	
	@Test
	void validateNewRevision_returnsVoid_whenAllConditionsGood() {
		this.planeValidator.validateNewRevision(LocalDate.of(2022,10,9), "15/03/2023");
	}
	
	@Test
	void validateCreatePlaneModel_throwsFlightManagerException_whenModelNull() {
		String model = null;
		this.createPlaneModel.setModel(model);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("The model fileld can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsValidatorException_whenModelEmpty() {
		String model = "";
		this.createPlaneModel.setModel(model);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("The model field can not be empty", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsValidatorException_whenModelTooLong() {
		String model = "aTooLongModelFiledToBeValid";
		this.createPlaneModel.setModel(model);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("The model is too long", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsFlightManagerException_whenTailNumberUnder0() {
		int tailNumber = -5;
		this.createPlaneModel.setTailNumber(tailNumber);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals(MessageFormat.format("Tail number [{0}] invalid, numbers bellow 0 are not accepted", tailNumber),
				thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsFlightManagerException_whenTailNumberAlreadyExists() {
		int tailNumber = 47;
		this.createPlaneModel.setTailNumber(tailNumber);
		
		Plane plane = Mockito.mock(Plane.class);
		
		Mockito.when(this.planeRepository.findByTailNumber(tailNumber)).thenReturn(Optional.of(plane));
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals(MessageFormat.format("Tail number [{0}] taken, take another one", tailNumber),
				thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsValidatorException_whenCapacityOutOfRange01() {
		int capacity = -5;
		this.createPlaneModel.setCapacity(capacity);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("The capacity should be between 2 and 852", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsValidatorException_whenCapacityOutOfRange02() {
		int capacity = 888;
		this.createPlaneModel.setCapacity(capacity);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("The capacity should be between 2 and 852", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeSmall01() {
		String size = "small";
		int fuelTankCapacity = 150;
		
		this.createPlaneModel.setFuelTankCapacity(fuelTankCapacity);
		this.createPlaneModel.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("The fuel capacity of a small plane should be between 4000 and 5000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeSmall02() {
		String size = "small";
		int fuelTankCapacity = 5555;
		
		this.createPlaneModel.setFuelTankCapacity(fuelTankCapacity);
		this.createPlaneModel.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("The fuel capacity of a small plane should be between 4000 and 5000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeMid01() {
		String size = "mid";
		int fuelTankCapacity = 150;
		
		this.createPlaneModel.setFuelTankCapacity(fuelTankCapacity);
		this.createPlaneModel.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("The fuel capacity of a mid plane should be between 26000 and 30000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeMid02() {
		String size = "mid";
		int fuelTankCapacity = 35000;
		
		this.createPlaneModel.setFuelTankCapacity(fuelTankCapacity);
		this.createPlaneModel.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("The fuel capacity of a mid plane should be between 26000 and 30000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeWide01() {
		String size = "wide";
		int fuelTankCapacity = 150;
		
		this.createPlaneModel.setFuelTankCapacity(fuelTankCapacity);
		this.createPlaneModel.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("The fuel capacity of a wide plane should be between 130000 and 190000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeWide02() {
		String size = "wide";
		int fuelTankCapacity = 200000;
		
		this.createPlaneModel.setFuelTankCapacity(fuelTankCapacity);
		this.createPlaneModel.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("The fuel capacity of a wide plane should be between 130000 and 190000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeJumbo01() {
		String size = "jumbo";
		int fuelTankCapacity = 150;
		
		this.createPlaneModel.setFuelTankCapacity(fuelTankCapacity);
		this.createPlaneModel.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("The fuel capacity of a jumbo plane should be between 200000 and 323000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsValidatorException_whenFuelTankCapacityOutOfRangeForSizeJumbo02() {
		String size = "jumbo";
		int fuelTankCapacity = 350000;
		
		this.createPlaneModel.setFuelTankCapacity(fuelTankCapacity);
		this.createPlaneModel.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("The fuel capacity of a jumbo plane should be between 200000 and 323000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsFlightManagerException_whenManufacturingDateWrongFormat() {
		this.createPlaneModel.setManufacturingDate("2000/05/05");
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("Manufacturing date must have the following format: dd/MM/yyyy", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsFlightManagerException_whenManufacturingLaterThanToday() {
		String manufacturingDate = "01/01/2024";
		this.createPlaneModel.setManufacturingDate(manufacturingDate);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("Plane's manufacturing date shoud be in the past!", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlane_returnsVoid_whenAllConditionsGood() throws ValidatorException {
		this.planeValidator.validateCreatePlaneModel(this.createPlaneModel);
	}
}
