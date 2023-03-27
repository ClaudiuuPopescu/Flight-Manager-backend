package msg.project.flightmanager.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.modelHelper.CreateAddressModel;
import msg.project.flightmanager.service.utils.StringUtils;
import msg.project.flightmanager.validator.AddressValidator;

@ExtendWith(MockitoExtension.class)
public class AddressValidatorTest {

	@InjectMocks
	private AddressValidator addressValidator;

	private AddressDto addressDto;

	private CreateAddressModel createAddressModel;

	@BeforeEach
	void init() {
		this.addressDto = new AddressDto();
		this.createAddressModel = new CreateAddressModel();
	}

	@Test
	void validateAddressDto_throwsValidatorException_whenCountryIsEmpty() {
		this.addressDto.setCountry("");
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateAddressDto(this.addressDto));
		assertEquals("Country cannot be empty!", exception.getMessage());
		assertEquals(ErrorCode.EMPTY_FIELD, exception.getErrorCode());
	}

	@Test
	void validateAddressDto_throwsValidatorException_whenCountryIsTooShort() {
		this.addressDto.setCountry("DA");
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateAddressDto(this.addressDto));
		assertEquals("There is no Country with less than 4 characters!", exception.getMessage());
		assertEquals(ErrorCode.IS_TOO_SHORT, exception.getErrorCode());
	}

	@Test
	void validateAddressDto_throwsValidatorException_whenCountryIsNotPrintable() {
		this.addressDto.setCountry("wrongCountryName");
		try (final MockedStatic<StringUtils> mocked = Mockito.mockStatic(StringUtils.class)) {

			mocked.when(() -> StringUtils.isAsciiPrintable(this.addressDto.getCountry())).thenReturn(false);
			ValidatorException exception = assertThrows(ValidatorException.class,
					() -> this.addressValidator.validateAddressDto(this.addressDto));

			assertEquals("Country can contain only letters!", exception.getMessage());
			assertEquals(ErrorCode.IS_NOT_OUT_OF_LETTERS, exception.getErrorCode());

		}
	}

	@Test
	void validateAddressDto_throwsValidatorException_whenCityIsNotPrintable() {
		this.addressDto.setCountry("Romania");
		this.addressDto.setCity("wrongCityName");
		try (final MockedStatic<StringUtils> mocked = Mockito.mockStatic(StringUtils.class)) {
			mocked.when(() -> StringUtils.isAsciiPrintable(this.addressDto.getCountry())).thenReturn(true);
			mocked.when(() -> StringUtils.isAsciiPrintable(this.addressDto.getCity())).thenReturn(false);
			ValidatorException exception = assertThrows(ValidatorException.class,
					() -> this.addressValidator.validateAddressDto(this.addressDto));

			assertEquals("City can contain only letters!", exception.getMessage());
			assertEquals(ErrorCode.IS_NOT_OUT_OF_LETTERS, exception.getErrorCode());

		}
	}

	@Test
	void validateAddressDto_throwsValidatorException_whenCityIsEmpty() {
		this.addressDto.setCountry("Romania");
		this.addressDto.setCity("");

		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateAddressDto(this.addressDto));

		assertEquals("City cannot be empty!", exception.getMessage());
		assertEquals(ErrorCode.EMPTY_FIELD, exception.getErrorCode());

	}

	@Test
	void validateAddressDto_throwsValidatorException_whenStreetIsNotPrintable() {
		this.addressDto.setCountry("Romania");
		this.addressDto.setCity("Sibiu");
		this.addressDto.setStreet("wrongStreet");
		try (final MockedStatic<StringUtils> mocked = Mockito.mockStatic(StringUtils.class)) {

			mocked.when(() -> StringUtils.isAsciiPrintable(this.addressDto.getCountry())).thenReturn(true);
			mocked.when(() -> StringUtils.isAsciiPrintable(this.addressDto.getCity())).thenReturn(true);
			mocked.when(() -> StringUtils.isAsciiPrintable(this.addressDto.getStreet())).thenReturn(false);
			ValidatorException exception = assertThrows(ValidatorException.class,
					() -> this.addressValidator.validateAddressDto(this.addressDto));

			assertEquals("Street can contain only letters!", exception.getMessage());
			assertEquals(ErrorCode.IS_NOT_OUT_OF_LETTERS, exception.getErrorCode());
		}
	}

	@Test
	void validateAddressDto_throwsValidatorException_whenStreetIsEmpty() {
		this.addressDto.setCountry("Romania");
		this.addressDto.setCity("Sibiu");
		this.addressDto.setStreet("");
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateAddressDto(this.addressDto));

		assertEquals("Street cannot be empty!", exception.getMessage());
		assertEquals(ErrorCode.EMPTY_FIELD, exception.getErrorCode());
	}
	
	@Test
	void validateAddressDto_throwsValidatorException_whenStreetNumberIsTooBig() {
		this.addressDto.setCountry("Romania");
		this.addressDto.setCity("Sibiu");
		this.addressDto.setStreet("Brukenthal");
		this.addressDto.setStreetNumber(100000);
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateAddressDto(this.addressDto));

		assertEquals("Street Number cannot be 0, negativ or higher than 1000!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());
	}
	
	@Test
	void validateAddressDto_throwsValidatorException_whenStreetNumberIsNegativ() {
		this.addressDto.setCountry("Romania");
		this.addressDto.setCity("Sibiu");
		this.addressDto.setStreet("Brukenthal");
		this.addressDto.setStreetNumber(-100000);
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateAddressDto(this.addressDto));

		assertEquals("Street Number cannot be 0, negativ or higher than 1000!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());
	}
	
	@Test
	void validateAddressDto_throwsValidatorException_whenStreetNumberIsZero() {
		this.addressDto.setCountry("Romania");
		this.addressDto.setCity("Sibiu");
		this.addressDto.setStreet("Brukenthal");
		this.addressDto.setStreetNumber(0);
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateAddressDto(this.addressDto));

		assertEquals("Street Number cannot be 0, negativ or higher than 1000!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());
	}
	
	@Test
	void validateAddressDto_throwsValidatorException_whenApartamentIsTooBig() {
		this.addressDto.setCountry("Romania");
		this.addressDto.setCity("Sibiu");
		this.addressDto.setStreet("Brukenthal");
		this.addressDto.setStreetNumber(10);
		this.addressDto.setApartment(100000);
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateAddressDto(this.addressDto));

		assertEquals("Street Number cannot be negativ or higher than 1000!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());
	}
	
	@Test
	void validateAddressDto_throwsValidatorException_whenApartamentIsNegativ() {
		this.addressDto.setCountry("Romania");
		this.addressDto.setCity("Sibiu");
		this.addressDto.setStreet("Brukenthal");
		this.addressDto.setStreetNumber(10);
		this.addressDto.setApartment(-100000);
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateAddressDto(this.addressDto));

		assertEquals("Street Number cannot be negativ or higher than 1000!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());
	}
	
	@Test
	void validateAddressDto_throwsNothing_whenAddressIsValid() throws ValidatorException {
		this.addressDto.setCountry("Romania");
		this.addressDto.setCity("Sibiu");
		this.addressDto.setStreet("Brukenthal");
		this.addressDto.setStreetNumber(10);
		this.addressValidator.validateAddressDto(this.addressDto);
	}

	@Test
	void validateCompany_throwsValidatorException_whenCountryIsEmpty() {
		this.createAddressModel.setCountry("");
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateCreateModel(this.createAddressModel));
		assertEquals("Country cannot be empty!", exception.getMessage());
		assertEquals(ErrorCode.EMPTY_FIELD, exception.getErrorCode());
	}

	@Test
	void validateCompany_throwsValidatorException_whenCountryIsTooShort() {
		this.createAddressModel.setCountry("DA");
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateCreateModel(this.createAddressModel));
		assertEquals("There is no Country with less than 4 characters!", exception.getMessage());
		assertEquals(ErrorCode.IS_TOO_SHORT, exception.getErrorCode());
	}

	@Test
	void validateCompany_throwsValidatorException_whenCountryIsNotPrintable() {
		this.createAddressModel.setCountry("wrongCountryName");
		try (final MockedStatic<StringUtils> mocked = Mockito.mockStatic(StringUtils.class)) {

			mocked.when(() -> StringUtils.isAsciiPrintable(this.createAddressModel.getCountry())).thenReturn(false);
			ValidatorException exception = assertThrows(ValidatorException.class,
					() -> this.addressValidator.validateCreateModel(this.createAddressModel));

			assertEquals("Country can contain only letters!", exception.getMessage());
			assertEquals(ErrorCode.IS_NOT_OUT_OF_LETTERS, exception.getErrorCode());

		}
	}

	@Test
	void validateCompany_throwsValidatorException_whenCityIsNotPrintable() {
		this.createAddressModel.setCountry("Romania");
		this.createAddressModel.setCity("wrongCityName");
		try (final MockedStatic<StringUtils> mocked = Mockito.mockStatic(StringUtils.class)) {
			mocked.when(() -> StringUtils.isAsciiPrintable(this.createAddressModel.getCountry())).thenReturn(true);
			mocked.when(() -> StringUtils.isAsciiPrintable(this.createAddressModel.getCity())).thenReturn(false);
			ValidatorException exception = assertThrows(ValidatorException.class,
					() -> this.addressValidator.validateCreateModel(this.createAddressModel));

			assertEquals("City can contain only letters!", exception.getMessage());
			assertEquals(ErrorCode.IS_NOT_OUT_OF_LETTERS, exception.getErrorCode());

		}
	}

	@Test
	void validateCompany_throwsValidatorException_whenCityIsEmpty() {
		this.createAddressModel.setCountry("Romania");
		this.createAddressModel.setCity("");

		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateCreateModel(this.createAddressModel));

		assertEquals("City cannot be empty!", exception.getMessage());
		assertEquals(ErrorCode.EMPTY_FIELD, exception.getErrorCode());

	}

	@Test
	void validateCompany_throwsValidatorException_whenStreetIsNotPrintable() {
		this.createAddressModel.setCountry("Romania");
		this.createAddressModel.setCity("Sibiu");
		this.createAddressModel.setStreet("wrongStreet");
		try (final MockedStatic<StringUtils> mocked = Mockito.mockStatic(StringUtils.class)) {

			mocked.when(() -> StringUtils.isAsciiPrintable(this.createAddressModel.getCountry())).thenReturn(true);
			mocked.when(() -> StringUtils.isAsciiPrintable(this.createAddressModel.getCity())).thenReturn(true);
			mocked.when(() -> StringUtils.isAsciiPrintable(this.createAddressModel.getStreet())).thenReturn(false);
			ValidatorException exception = assertThrows(ValidatorException.class,
					() -> this.addressValidator.validateCreateModel(this.createAddressModel));

			assertEquals("Street can contain only letters!", exception.getMessage());
			assertEquals(ErrorCode.IS_NOT_OUT_OF_LETTERS, exception.getErrorCode());
		}
	}

	@Test
	void validateCompany_throwsValidatorException_whenStreetIsEmpty() {
		this.createAddressModel.setCountry("Romania");
		this.createAddressModel.setCity("Sibiu");
		this.createAddressModel.setStreet("");
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateCreateModel(this.createAddressModel));

		assertEquals("Street cannot be empty!", exception.getMessage());
		assertEquals(ErrorCode.EMPTY_FIELD, exception.getErrorCode());
	}
	
	@Test
	void validateCompany_throwsValidatorException_whenStreetNumberIsTooBig() {
		this.createAddressModel.setCountry("Romania");
		this.createAddressModel.setCity("Sibiu");
		this.createAddressModel.setStreet("Brukenthal");
		this.createAddressModel.setStreetNumber(100000);
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateCreateModel(this.createAddressModel));

		assertEquals("Street Number cannot be 0, negativ or higher than 1000!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());
	}
	
	@Test
	void validateCompany_throwsValidatorException_whenStreetNumberIsNegativ() {
		this.createAddressModel.setCountry("Romania");
		this.createAddressModel.setCity("Sibiu");
		this.createAddressModel.setStreet("Brukenthal");
		this.createAddressModel.setStreetNumber(-100000);
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateCreateModel(this.createAddressModel));

		assertEquals("Street Number cannot be 0, negativ or higher than 1000!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());
	}
	
	@Test
	void validateCompany_throwsValidatorException_whenStreetNumberIsZero() {
		this.createAddressModel.setCountry("Romania");
		this.createAddressModel.setCity("Sibiu");
		this.createAddressModel.setStreet("Brukenthal");
		this.createAddressModel.setStreetNumber(0);
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateCreateModel(this.createAddressModel));

		assertEquals("Street Number cannot be 0, negativ or higher than 1000!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());
	}
	
	@Test
	void validateCompany_throwsValidatorException_whenApartamentIsTooBig() {
		this.createAddressModel.setCountry("Romania");
		this.createAddressModel.setCity("Sibiu");
		this.createAddressModel.setStreet("Brukenthal");
		this.createAddressModel.setStreetNumber(10);
		this.createAddressModel.setApartment(100000);
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateCreateModel(this.createAddressModel));

		assertEquals("Street Number cannot be negativ or higher than 1000!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());
	}
	
	@Test
	void validateCompany_throwsValidatorException_whenApartamentIsNegativ() {
		this.createAddressModel.setCountry("Romania");
		this.createAddressModel.setCity("Sibiu");
		this.createAddressModel.setStreet("Brukenthal");
		this.createAddressModel.setStreetNumber(10);
		this.createAddressModel.setApartment(-100000);
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.addressValidator.validateCreateModel(this.createAddressModel));

		assertEquals("Street Number cannot be negativ or higher than 1000!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());
	}
	
	@Test
	void validateCompany_throwsNothing_whenAddressIsValid() throws ValidatorException {
		this.createAddressModel.setCountry("Romania");
		this.createAddressModel.setCity("Sibiu");
		this.createAddressModel.setStreet("Brukenthal");
		this.createAddressModel.setStreetNumber(10);
		this.addressValidator.validateCreateModel(this.createAddressModel);
	}
	

}
