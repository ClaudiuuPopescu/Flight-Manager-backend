package msg.project.flightmanager.validator;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import msg.project.flightmanager.dto.PlaneDto;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.modelHelper.CreatePlaneModel;
import msg.project.flightmanager.repository.PlaneRepository;

@Component
public class PlaneValidator {
	@Autowired
	private PlaneRepository planeRepository;

	public void validatePlane(PlaneDto planeDto) throws ValidatorException {

		validateModel(planeDto.getModel());
		validateTailNumber(planeDto.getTailNumber());
		validateCapacity(planeDto.getCapacity());
		validateFuelTankCapacity(planeDto.getFuelTankCapacity(), planeDto.getSize());
		validateManufacturingDate(planeDto.getManufacturingDate(), planeDto.getFirstFlight(),
				planeDto.getLastRevision());
		validateFirstFlightDate(planeDto.getFirstFlight(), planeDto.getLastRevision());
		validateLastRevision(planeDto.getLastRevision());
	}

	public void validateCreatePlaneModel(CreatePlaneModel createPlaneModel) throws ValidatorException {

		validateModel(createPlaneModel.getModel());
		validateTailNumber(createPlaneModel.getTailNumber());
		validateCapacity(createPlaneModel.getCapacity());
		validateFuelTankCapacity(createPlaneModel.getFuelTankCapacity(), createPlaneModel.getSize());
		validateManufacturingDate_CreatePlaneModel(createPlaneModel.getManufacturingDate());
	}

	private void validateModel(String model) throws ValidatorException {

		if(model  == null) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "The model fileld can not be null");
		}
		if (model.isEmpty())
			throw new ValidatorException("The model field can not be empty", ErrorCode.EMPTY_FIELD);
		if (model.length() > 20)
			throw new ValidatorException("The model is too long", ErrorCode.IS_TOO_LONG);
	}

	private void validateTailNumber(int tailNumber) {
		if (tailNumber <= 0) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED,
					MessageFormat.format("Tail number [{0}] invalid, numbers bellow 0 are not accepted", tailNumber));
		}

		Optional<Plane> plane = this.planeRepository.findByTailNumber(tailNumber);

		if (plane.isPresent()) {
			throw new FlightManagerException(HttpStatus.IM_USED,
					MessageFormat.format("Tail number [{0}] taken, take another one", tailNumber));
		}
	}

	private void validateCapacity(int capacity) throws ValidatorException {

		if (capacity < 1 || capacity > 853)
			throw new ValidatorException("The capacity should be between 2 and 852", ErrorCode.WRONG_INTERVAL);
	}

	private void validateFuelTankCapacity(int fuelTankCapacity, String size) throws ValidatorException {

		if (size.equals("small") && !validateFuelCapacityForSmall(fuelTankCapacity))
			throw new ValidatorException("The fuel capacity of a small plane should be between 4000 and 5000!",
					ErrorCode.WRONG_INTERVAL);

		if (size.equals("mid") && !validateFuelCapacityForMid(fuelTankCapacity))
			throw new ValidatorException("The fuel capacity of a mid plane should be between 26000 and 30000!",
					ErrorCode.WRONG_INTERVAL);

		if (size.equals("wide") && !validateFuelCapacityForWide(fuelTankCapacity))
			throw new ValidatorException("The fuel capacity of a wide plane should be between 130000 and 190000!",
					ErrorCode.WRONG_INTERVAL);

		if (size.equals("jumbo") && !validateFuelCapacityForJumbo(fuelTankCapacity))
			throw new ValidatorException("The fuel capacity of a jumbo plane should be between 200000 and 323000!",
					ErrorCode.WRONG_INTERVAL);
	}

	private void validateManufacturingDate(LocalDate manufacturingDate, LocalDate firstFlightDate,
			LocalDate lastRevision) throws ValidatorException {
		
		if(manufacturingDate == null) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "The manufacturing date can not be null");
		}
		
		if(firstFlightDate == null) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "The first flight date can not be null");
		}
		
		if(lastRevision == null) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "The last revision date can not be null");
		}

		LocalDate currentDate = java.time.LocalDate.now();

		if (validateDate1IsEarlierThanDate2(currentDate, manufacturingDate))
			throw new ValidatorException("The manufacturing date of the plane should be in the past!",
					ErrorCode.WRONG_INTERVAL);

		if (validateDate1IsLaterThanDate2(manufacturingDate, firstFlightDate))
			throw new ValidatorException(
					"The manufacturing date of the plane should be earlier than the first flight date!",
					ErrorCode.WRONG_INTERVAL);

		if (validateDate1IsLaterThanDate2(manufacturingDate, lastRevision))
			throw new ValidatorException(
					"The manufacturing date of the plane should be earlier than the last revision date!",
					ErrorCode.WRONG_INTERVAL);
	}

	private void validateManufacturingDate_CreatePlaneModel(LocalDate manufacturingDate) {
		if(manufacturingDate == null) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "The manufacturing date can not be null");
		}

		LocalDate currentDate = LocalDate.now();

		if (validateDate1IsEarlierThanDate2(currentDate, manufacturingDate)) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED,
					"Plane's manufacturing date shoud be in the past!");
		}
	}

	private void validateFirstFlightDate(LocalDate firstFlightDate, LocalDate lastRevision)
			throws ValidatorException {

		LocalDate currentDate = java.time.LocalDate.now();
			
		if (validateDate1IsEarlierThanDate2(currentDate, firstFlightDate))
			throw new ValidatorException("The first flight date of the plane should be in the past!",
					ErrorCode.WRONG_INTERVAL);

		if (validateDate1IsLaterThanDate2(firstFlightDate, lastRevision))
			throw new ValidatorException(
					"The first flight date of the plane should be earlier than the last revision date!",
					ErrorCode.WRONG_INTERVAL);
	}

	private void validateLastRevision(LocalDate lastRevision)
			throws ValidatorException {	
		
		LocalDate currentDate = java.time.LocalDate.now();
			
		if (validateDate1IsEarlierThanDate2(currentDate, lastRevision))
			throw new ValidatorException("The first revision date of the plane should be in the past!",
					ErrorCode.WRONG_INTERVAL);
	}

	public void validateNewRevision(LocalDate lastRevision, LocalDate newRevisionS) {
		
		if(newRevisionS.compareTo(LocalDate.now()) > 0) {
			throw new FlightManagerException(
					HttpStatus.EXPECTATION_FAILED,
					"New revision must be today or earlier");
		}
		
		if(!validateDate1IsEarlierThanDate2(lastRevision, newRevisionS)) {
			throw new FlightManagerException(
					HttpStatus.EXPECTATION_FAILED,
					"New revision must be later than last revision");
		}
	}

	private boolean validateFuelCapacityForSmall(int fuelTankCapacity) {
		return (fuelTankCapacity >= 4000 && fuelTankCapacity <= 5000);
	}

	private boolean validateFuelCapacityForMid(int fuelTankCapacity) {
		return (fuelTankCapacity >= 26000 && fuelTankCapacity <= 30000);
	}

	private boolean validateFuelCapacityForWide(int fuelTankCapacity) {
		return (fuelTankCapacity >= 130000 && fuelTankCapacity <= 190000);
	}

	private boolean validateFuelCapacityForJumbo(int fuelTankCapacity) {
		return (fuelTankCapacity >= 200000 && fuelTankCapacity <= 323000);
	}

	private boolean validateDate1IsEarlierThanDate2(LocalDate date1, LocalDate date2) {
		return date1.compareTo(date2) < 0;
	}

	private boolean validateDate1IsLaterThanDate2(LocalDate date1, LocalDate date2) {
		return date1.compareTo(date2) > 0;
	}
}