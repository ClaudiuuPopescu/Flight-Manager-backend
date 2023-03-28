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
import msg.project.flightmanager.enums.PlaneSize;
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
		this.planeDto = new PlaneDto("model", 47, 50, 4500, LocalDate.of(2015, 3, 18),
				LocalDate.of(2017, 5, 20), LocalDate.of(2020, 7, 15),
				PlaneSize.SMALL);
		
		this.createPlaneModel = new CreatePlaneModel("model", 47, 47, 4500, LocalDate.of(2015, 3, 18), PlaneSize.SMALL);
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
		PlaneSize size = PlaneSize.SMALL;
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
		PlaneSize size = PlaneSize.SMALL;
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
		PlaneSize size = PlaneSize.MID;
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
		PlaneSize size = PlaneSize.MID;
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
		PlaneSize size = PlaneSize.WIDE;
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
		PlaneSize size = PlaneSize.WIDE;
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
		PlaneSize size = PlaneSize.JUMBO;
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
		PlaneSize size = PlaneSize.JUMBO;
		int fuelTankCapacity = 350000;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The fuel capacity of a jumbo plane should be between 200000 and 323000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsFlightManagerException_whenManufacturingDateNull() {
		this.planeDto.setManufacturingDate(null);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("Manufacturing date can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsFlightManagerException_whenManufacturingDateNotInThePast() {
		LocalDate manufacturingDate = LocalDate.of(2024, 7, 3);
		this.planeDto.setManufacturingDate(manufacturingDate);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The manufacturing date of the plane should be in the past!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenManufacturingDateLaterThanFirstFlight() {
		LocalDate firstFlight = LocalDate.of(2022, 1, 1);
		LocalDate manufacturingDate = LocalDate.of(2023, 1, 3);
		
		this.planeDto.setFirstFlight(firstFlight);
		this.planeDto.setManufacturingDate(manufacturingDate);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The manufacturing date of the plane should be earlier than the first flight date!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenManufacturingDateLaterThanLastRevision() {
		LocalDate firstFlight = LocalDate.of(2023, 2, 1);
		LocalDate lastRevision = LocalDate.of(2022, 8, 1);
		LocalDate manufacturingDate = LocalDate.of(2023, 1, 3);
		
		this.planeDto.setFirstFlight(firstFlight);
		this.planeDto.setLastRevision(lastRevision);
		this.planeDto.setManufacturingDate(manufacturingDate);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The manufacturing date of the plane should be earlier than the last revision date!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsFlightManagerException_whenFirstFightNull() {
		this.planeDto.setFirstFlight(null);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("First flight can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenFirstFightNotInThePast() {
		LocalDate firstFlight = LocalDate.of(2025, 1, 2);
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
		LocalDate firstFlight = LocalDate.of(2023, 2, 2);
		LocalDate lastRevision = LocalDate.of(2023, 1, 2);
		this.planeDto.setFirstFlight(firstFlight);
		this.planeDto.setLastRevision(lastRevision);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The first flight date of the plane should be earlier than the last revision date!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsFlightManagerException_whenLastRevisionNull() {
		this.planeDto.setLastRevision(null);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("Last revision can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_throwsValidatorException_whenLastRevisionNotInThePast() {
		LocalDate lastRevision = LocalDate.of(2023, 7, 2);
		this.planeDto.setLastRevision(lastRevision);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validatePlane(this.planeDto));

		assertEquals("The first revision date of the plane should be in the past!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validatePlane(this.planeDto));
	}
	
	@Test
	void validatePlane_returnsVoid_whenAllConditionsGood01() throws ValidatorException {
		PlaneSize size = PlaneSize.SMALL;
		int fuelTankCapacity = 4500;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		this.planeValidator.validatePlane(this.planeDto);
	}
	
	@Test
	void validatePlane_returnsVoid_whenAllConditionsGood02() throws ValidatorException {
		PlaneSize size = PlaneSize.MID;
		int fuelTankCapacity = 27000;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		this.planeValidator.validatePlane(this.planeDto);
	}
	
	@Test
	void validatePlane_returnsVoid_whenAllConditionsGood03() throws ValidatorException {
		PlaneSize size = PlaneSize.WIDE;
		int fuelTankCapacity = 135000;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		this.planeValidator.validatePlane(this.planeDto);
	}
	
	@Test
	void validatePlane_returnsVoid_whenAllConditionsGood04() throws ValidatorException {
		PlaneSize size = PlaneSize.JUMBO;
		int fuelTankCapacity = 250000;
		
		this.planeDto.setFuelTankCapacity(fuelTankCapacity);
		this.planeDto.setSize(size);
		
		this.planeValidator.validatePlane(this.planeDto);
	}
	
	@Test
	void validateNewRevision_throwsFlightManagerException_whenNewRevisionDateLaterThanToday() {
		LocalDate lastRevision = LocalDate.of(2022, 9, 10);
		LocalDate newRevision = LocalDate.of(2027, 1, 2);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.valiateNewRevision(lastRevision, newRevision));

		assertEquals("New revision must be today or earlier", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.valiateNewRevision(lastRevision, newRevision));
	}
	
	@Test
	void validateNewRevision_throwsFlightManagerException_whenNewRevisionEarlierThanLastRevision() {
		LocalDate lastRevision = LocalDate.of(2022, 9, 10);
		LocalDate newRevision = LocalDate.of(2021, 1, 2);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.valiateNewRevision(lastRevision, newRevision));

		assertEquals("New revision must be later than last revision", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.valiateNewRevision(lastRevision, newRevision));
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
		PlaneSize size = PlaneSize.SMALL;
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
		PlaneSize size = PlaneSize.SMALL;
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
		PlaneSize size = PlaneSize.MID;
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
		PlaneSize size = PlaneSize.MID;
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
		PlaneSize size = PlaneSize.WIDE;
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
		PlaneSize size = PlaneSize.WIDE;
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
		PlaneSize size = PlaneSize.JUMBO;
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
		PlaneSize size = PlaneSize.JUMBO;
		int fuelTankCapacity = 350000;
		
		this.createPlaneModel.setFuelTankCapacity(fuelTankCapacity);
		this.createPlaneModel.setSize(size);
		
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("The fuel capacity of a jumbo plane should be between 200000 and 323000!", thrown.getMessage());
		assertThrows(ValidatorException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsFlightManagerException_whenManufacturingDateNull() {
		this.createPlaneModel.setManufacturingDate(null);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("Manufacturing date can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
	
	@Test
	void validateCreatePlaneModel_throwsFlightManagerException_whenManufacturingLaterThanToday() {
		LocalDate manufacturingDate = LocalDate.of(2024, 1, 1);
		this.createPlaneModel.setManufacturingDate(manufacturingDate);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));

		assertEquals("Plane's manufacturing date shoud be in the past!", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.planeValidator.validateCreatePlaneModel(this.createPlaneModel));
	}
}
