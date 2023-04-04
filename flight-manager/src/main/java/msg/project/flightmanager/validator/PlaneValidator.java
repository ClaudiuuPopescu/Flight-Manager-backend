package msg.project.flightmanager.validator;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	private void validateManufacturingDate(String manufacturingDate, String firstFlightDate,
			String lastRevision) throws ValidatorException {
		
		String regexDate = "^(0[1-9]|[12]\\d|3[01])/(0[1-9]|1[0-2])/\\d{4}$"; // dd/MM/yyyy
		
		Pattern pattern = Pattern.compile(regexDate);
		Matcher matcher = pattern.matcher(manufacturingDate);
		Matcher matcher2 = pattern.matcher(firstFlightDate);
		Matcher matcher3 = pattern.matcher(lastRevision);

		if (!matcher.matches()) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "Manufacturing date must have the following format: dd/MM/yyyy");
		}
		
		if (!matcher2.matches()) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "First flight date must have the following format: dd/MM/yyyy");
		}
		
		if (!matcher3.matches()) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "Last revision date must have the following format: dd/MM/yyyy");
		}
		
		LocalDate manufDate = LocalDate.parse(manufacturingDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		LocalDate firstFDate = LocalDate.parse(firstFlightDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		LocalDate lastRevDate = LocalDate.parse(lastRevision, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		LocalDate currentDate = java.time.LocalDate.now();

		if (validateDate1IsEarlierThanDate2(currentDate, manufDate))
			throw new ValidatorException("The manufacturing date of the plane should be in the past!",
					ErrorCode.WRONG_INTERVAL);

		if (validateDate1IsLaterThanDate2(manufDate, firstFDate))
			throw new ValidatorException(
					"The manufacturing date of the plane should be earlier than the first flight date!",
					ErrorCode.WRONG_INTERVAL);

		if (validateDate1IsLaterThanDate2(manufDate, lastRevDate))
			throw new ValidatorException(
					"The manufacturing date of the plane should be earlier than the last revision date!",
					ErrorCode.WRONG_INTERVAL);
	}

	private void validateManufacturingDate_CreatePlaneModel(String manufacturingDate) {
		String regexDate = "^(0[1-9]|[12]\\d|3[01])/(0[1-9]|1[0-2])/\\d{4}$"; // dd/MM/yyyy
		
		Pattern pattern = Pattern.compile(regexDate);
		Matcher matcher = pattern.matcher(manufacturingDate);
		
		if (!matcher.matches()) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED, "Manufacturing date must have the following format: dd/MM/yyyy");
		}
		
		LocalDate manufDate = LocalDate.parse(manufacturingDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		LocalDate currentDate = LocalDate.now();

		if (validateDate1IsEarlierThanDate2(currentDate, manufDate)) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED,
					"Plane's manufacturing date shoud be in the past!");
		}
	}

	private void validateFirstFlightDate(String firstFlightDate, String lastRevision)
			throws ValidatorException {
		
		LocalDate firstFDate = LocalDate.parse(firstFlightDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		LocalDate lastRevDate = LocalDate.parse(lastRevision, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		LocalDate currentDate = java.time.LocalDate.now();
			
		if (validateDate1IsEarlierThanDate2(currentDate, firstFDate))
			throw new ValidatorException("The first flight date of the plane should be in the past!",
					ErrorCode.WRONG_INTERVAL);
			

		if (validateDate1IsLaterThanDate2(firstFDate, lastRevDate))
			throw new ValidatorException(
					"The first flight date of the plane should be earlier than the last revision date!",
					ErrorCode.WRONG_INTERVAL);
	}

	private void validateLastRevision(String lastRevision)
			throws ValidatorException {	
		
		LocalDate lastRevDate = LocalDate.parse(lastRevision, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		LocalDate currentDate = java.time.LocalDate.now();
			
		if (validateDate1IsEarlierThanDate2(currentDate, lastRevDate))
			throw new ValidatorException("The first revision date of the plane should be in the past!",
					ErrorCode.WRONG_INTERVAL);
	}

	public void validateNewRevision(LocalDate lastRevision, String newRevisionS) {
		LocalDate newRevision = LocalDate.parse(newRevisionS, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		if(newRevision.compareTo(LocalDate.now()) > 0) {
			throw new FlightManagerException(
					HttpStatus.EXPECTATION_FAILED,
					"New revision must be today or earlier");
		}
		
		if(!validateDate1IsEarlierThanDate2(lastRevision, newRevision)) {
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
