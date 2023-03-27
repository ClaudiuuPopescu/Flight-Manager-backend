package msg.project.flightmanager.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.dto.CompanyDto;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Address;
import msg.project.flightmanager.service.utils.StringUtils;
import msg.project.flightmanager.validator.AddressValidator;
import msg.project.flightmanager.validator.CompanyValidator;

@ExtendWith(MockitoExtension.class)
public class CompanyValidatorTest {

	@Mock
	private AddressValidator addressValidator;

	@InjectMocks
	private CompanyValidator companyValidator;

	private CompanyDto companyDto;

	@BeforeEach
	void init() {
		this.companyDto = new CompanyDto();
	}

	@Test
	void validateCompany_throwsValidatorException_whenTheNameIsNotPrintable() throws ValidatorException {
		this.companyDto.setName("wrongName");
		try (final MockedStatic<StringUtils> mocked = Mockito.mockStatic(StringUtils.class)) {

			mocked.when(() -> StringUtils.isAsciiPrintable(companyDto.getName())).thenReturn(false);
			ValidatorException exception = assertThrows(ValidatorException.class,
					() -> this.companyValidator.validateCompany(this.companyDto));

			assertEquals("Name can contain only letters!", exception.getMessage());
			assertEquals(ErrorCode.IS_NOT_OUT_OF_LETTERS, exception.getErrorCode());

		}
	}

	@Test
	void validateCompany_throwsValidatorException_whenTheNameIsTooLong(){
		this.companyDto.setName("wrongNameBecauseIsMadeOutOfTooManyLetters");
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.companyValidator.validateCompany(this.companyDto));
		assertEquals("Name can can have the lenght between 1 and 30!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());

	}
	
	@Test
	void validateCompany_throwsValidatorException_whenTheNameIsTooShort(){
		this.companyDto.setName("");
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.companyValidator.validateCompany(this.companyDto));
		assertEquals("Name can can have the lenght between 1 and 30!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());

	}
	
	@Test
	void validateCompany_throwsValidatorException_whenTheEmailIsEmpty(){
		this.companyDto.setName("goodName");
		this.companyDto.setEmail("");
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.companyValidator.validateCompany(this.companyDto));
		assertEquals("Email cannot be empty!", exception.getMessage());
		assertEquals(ErrorCode.EMPTY_FIELD, exception.getErrorCode());

	}
	
	@Test
	void validateCompany_throwsValidatorException_whenTheEmailIsNotInTheRightFormat(){
		this.companyDto.setName("goodName");
		this.companyDto.setEmail("wrong@mail.de");
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.companyValidator.validateCompany(this.companyDto));
		assertEquals("The email should look like wizzair@company.com!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_FROMAT, exception.getErrorCode());

	}
	
	@Test
	void validateCompany_throwsValidatorException_whenTheEmailIsTooShort(){
		this.companyDto.setName("goodName");
		this.companyDto.setEmail("a@company.com");
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.companyValidator.validateCompany(this.companyDto));
		assertEquals("The email is not long enought!", exception.getMessage());
		assertEquals(ErrorCode.IS_TOO_SHORT, exception.getErrorCode());

	}
	
	@Test
	void validateCompany_throwsValidatorException_whenPhoneNumberIsEmpty(){
		this.companyDto.setName("goodName");
		this.companyDto.setEmail("wizzair@company.com");
		this.companyDto.setPhoneNumber("");
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.companyValidator.validateCompany(this.companyDto));
		assertEquals("PhoneNumber cannot be empty!", exception.getMessage());
		assertEquals(ErrorCode.EMPTY_FIELD, exception.getErrorCode());

	}
	
	@Test
	void validateCompany_throwsValidatorException_whenPhoneNumberIsTooShort(){
		this.companyDto.setName("goodName");
		this.companyDto.setEmail("wizzair@company.com");
		this.companyDto.setPhoneNumber("077");
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.companyValidator.validateCompany(this.companyDto));
		assertEquals("The phone number should have 10 numbers!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());

	}
	
	@Test
	void validateCompany_throwsValidatorException_whenPhoneNumberHasTheWrongFormat(){
		this.companyDto.setName("goodName");
		this.companyDto.setEmail("wizzair@company.com");
		this.companyDto.setPhoneNumber("43333333333");
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.companyValidator.validateCompany(this.companyDto));
		assertEquals("Wrong phone number!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_FROMAT, exception.getErrorCode());

	}
	
	@Test
	void validateCompany_throwsValidatorException_whenFoundedInYearIsWrong(){
		this.companyDto.setName("goodName");
		this.companyDto.setEmail("wizzair@company.com");
		this.companyDto.setPhoneNumber("40773842601");
		this.companyDto.setFoundedIn(LocalDate.of(2025, 8, 20));
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.companyValidator.validateCompany(this.companyDto));
		assertEquals("The year should be lower than the current year!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());

	}
	
	@Test
	void validateCompany_throwsValidatorException_whenFoundedInMonthIsWrong(){
		this.companyDto.setName("goodName");
		this.companyDto.setEmail("wizzair@company.com");
		this.companyDto.setPhoneNumber("40773842601");
		this.companyDto.setFoundedIn(LocalDate.of(2023, 8, 20));
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.companyValidator.validateCompany(this.companyDto));
		assertEquals("The month should be lower than the current month!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());
	}
	
	@Test
	void validateCompany_throwsValidatorException_whenFoundedInDayIsWrong(){
		LocalDate localdate = java.time.LocalDate.now();
		this.companyDto.setName("goodName");
		this.companyDto.setEmail("wizzair@company.com");
		this.companyDto.setPhoneNumber("40773842601");
		this.companyDto.setFoundedIn(localdate.plusDays(1));
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.companyValidator.validateCompany(this.companyDto));
		assertEquals("The day should be lower than the current day!", exception.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, exception.getErrorCode());
	}
	
	@Test
	void validateCompany_throwsValidatorException_whenAddressIsWrong() throws ValidatorException{
		this.companyDto.setName("goodName");
		this.companyDto.setEmail("wizzair@company.com");
		this.companyDto.setPhoneNumber("40773842601");
		this.companyDto.setFoundedIn(LocalDate.of(2001, 8, 20));
		AddressDto addressDto = mock(AddressDto.class);
		this.companyDto.setAddress(addressDto);
		doThrow(new ValidatorException("Country cannot be empty!", ErrorCode.EMPTY_FIELD)).when(this.addressValidator).validateAddressDto(this.companyDto.getAddress());
		ValidatorException exception = assertThrows(ValidatorException.class,
				() -> this.companyValidator.validateCompany(this.companyDto));
		assertEquals("Country cannot be empty!", exception.getMessage());
		assertEquals(ErrorCode.EMPTY_FIELD, exception.getErrorCode());
	}
	
	@Test
	void validateCompany_throwsNothing_whenTheCompanyIsValid() throws ValidatorException{
		this.companyDto.setName("goodName");
		this.companyDto.setEmail("wizzair@company.com");
		this.companyDto.setPhoneNumber("40773842601");
		this.companyDto.setFoundedIn(LocalDate.of(2001, 8, 20));
		AddressDto addressDto = mock(AddressDto.class);
		this.companyDto.setAddress(addressDto);
		Mockito.lenient().doNothing().when(this.addressValidator).validateAddressDto(this.companyDto.getAddress());
		this.companyValidator.validateCompany(this.companyDto);
	}
}
