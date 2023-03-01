package validator;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import dto.PlaneDto;
import enums.PlaneSize;
import exceptions.ErrorCode;
import exceptions.ValidatorException;

@Component
public class PlaneValidator {

	public void validatePlane(PlaneDto planeDto) throws ValidatorException {

		validateModel(planeDto.getModel());
		validateCapacity(planeDto.getCapacity());
		validateFuelTankCapacity(planeDto.getFuelTankCapacity(), planeDto.getSize());
		validateManufacturingDate(planeDto.getManufacturingDate(), planeDto.getFirstFlight(),
				planeDto.getLastRevision());
		validateFirstFlightDate(planeDto.getFirstFlight(), planeDto.getFirstFlight(), planeDto.getLastRevision());
		validateLastRevision(planeDto.getLastRevision(), planeDto.getFirstFlight(), planeDto.getLastRevision());
	}

	private void validateModel(String model) throws ValidatorException {

		if (model.isEmpty())
			throw new ValidatorException("The model field cannot be empty", ErrorCode.EMPTY_FIELD);
		if (model.length() > 20)
			throw new ValidatorException("The model is too long", ErrorCode.IS_TOO_LONG);

	}

	private void validateCapacity(int capacity) throws ValidatorException {

		if (capacity < 1 || capacity > 853)
			throw new ValidatorException("The capacity should be between 1 and 852", ErrorCode.WRONG_INTERVAL);
	}

	private void validateFuelTankCapacity(int fuelTankCapacity, PlaneSize size) throws ValidatorException {

		if (size.equals(PlaneSize.SMALL) && !validateFuelCapacityForSmall(fuelTankCapacity))
			throw new ValidatorException("The fuel capacity of a small plane should be between 4000 and 5000!",
					ErrorCode.WRONG_INTERVAL);

		if (size.equals(PlaneSize.MID) && !validateFuelCapacityForMid(fuelTankCapacity))
			throw new ValidatorException("The fuel capacity of a mid plane should be between 26000 and 30000!",
					ErrorCode.WRONG_INTERVAL);

		if (size.equals(PlaneSize.WIDE) && !validateFuelCapacityForWide(fuelTankCapacity))
			throw new ValidatorException("The fuel capacity of a wide plane should be between 130000 and 190000!",
					ErrorCode.WRONG_INTERVAL);

		if (size.equals(PlaneSize.JUMBO) && !validateFuelCapacityForJumbo(fuelTankCapacity))
			throw new ValidatorException("The fuel capacity of a jumbo plane should be between 200000 and 323000!",
					ErrorCode.WRONG_INTERVAL);
	}

	private void validateManufacturingDate(LocalDate manufacturingDate, LocalDate firstFlightDate,
			LocalDate lastRevision) throws ValidatorException {

		if (manufacturingDate != null) {
			LocalDate currentDate = java.time.LocalDate.now();
			if (validateDate1IsEarlierThanDate2(currentDate, manufacturingDate))
				throw new ValidatorException("The manufacturing date of the plane should be in the past!",
						ErrorCode.WRONG_INTERVAL);

			if (firstFlightDate != null && validateDate1IsLaterThanDate2(manufacturingDate, firstFlightDate))
				throw new ValidatorException(
						"The manufacturing date of the plane should be earlier than the first flight date!",
						ErrorCode.WRONG_INTERVAL);

			if (lastRevision != null && validateDate1IsLaterThanDate2(manufacturingDate, lastRevision))
				throw new ValidatorException(
						"The manufacturing date of the plane should be earlier than the last revision date!",
						ErrorCode.WRONG_INTERVAL);
		}

	}

	private void validateFirstFlightDate(LocalDate manufacturingDate, LocalDate firstFlightDate, LocalDate lastRevision)
			throws ValidatorException {

		if (firstFlightDate != null) {
			
			LocalDate currentDate = java.time.LocalDate.now();
			
			if (validateDate1IsEarlierThanDate2(currentDate, firstFlightDate))
				throw new ValidatorException("The first flight date of the plane should be in the past!",
						ErrorCode.WRONG_INTERVAL);
			
			if (manufacturingDate != null && validateDate1IsLaterThanDate2(firstFlightDate, manufacturingDate))
				throw new ValidatorException(
						"The first flight date of the plane should be later than the manufacturing date!",
						ErrorCode.WRONG_INTERVAL);

			if (lastRevision != null && validateDate1IsLaterThanDate2(manufacturingDate, lastRevision))
				throw new ValidatorException(
						"The manufacturing date of the plane should be earlier than the last revision date!",
						ErrorCode.WRONG_INTERVAL);

		}

	}

	private void validateLastRevision(LocalDate manufacturingDate, LocalDate firstFlightDate, LocalDate lastRevision)
			throws ValidatorException {

		if (lastRevision != null) {
			
			LocalDate currentDate = java.time.LocalDate.now();
			
			if (validateDate1IsEarlierThanDate2(currentDate, firstFlightDate))
				throw new ValidatorException("The first revision date of the plane should be in the past!",
						ErrorCode.WRONG_INTERVAL);
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
