package msg.project.flightmanager.servicies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
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

import msg.project.flightmanager.converter.AddressConverter;
import msg.project.flightmanager.converter.CompanyConverter;
import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.dto.CompanyDto;
import msg.project.flightmanager.exceptions.CompanyException;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Address;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.repository.CompanyRepository;
import msg.project.flightmanager.service.CompanyService;
import msg.project.flightmanager.service.PlaneService;
import msg.project.flightmanager.validator.AddressValidator;
import msg.project.flightmanager.validator.CompanyValidator;
import msg.project.flightmanager.service.utils.StringUtils;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

	private List<Company> companies;
	private Company company1;
	private Company company2;

	@Mock
	private CompanyConverter companyConverter;

	@Mock
	private AddressConverter addressConverter;

	@Mock
	private CompanyRepository companyRepository;

	@Mock
	private CompanyValidator companyValidator;

	@Mock
	private PlaneService planeService;

	@Mock
	private Address address;

	@Mock
	private AddressDto addressDto;

	@Mock
	private AddressValidator addressValidator;

	@InjectMocks
	private CompanyService companyService;

	@BeforeEach
	public void init() {
		this.companies = new ArrayList<Company>();
		LocalDate date = LocalDate.of(1999, 10, 10);
		Address address1 = createAddress(1L, "Hungary", "Budapest", "Budapest Street", 11);
		company1 = createCompany("WizzAir", "0776162711", "wizz@gmail.com", date, address1);

		LocalDate date2 = LocalDate.of(1980, 03, 31);
		Address address2 = createAddress(2L, "Irland", "Dublin", "St.Patrick Street", 13);
		company2 = createCompany("RyanAir", "0776162112", "ryanair@gmail.com", date2, address2);
		Collections.addAll(this.companies, company1, company2);
	}

	@Test
	void findAll_returnsNotEmptyList_whenCompaniesExistsInTheDatabase() {
		Mockito.when(this.companyRepository.findAll()).thenReturn(companies);
		assertEquals(this.companyService.findAll().size(), 2);
	}

	@Test
	void findAll_returnsEmptyList_whenThereIsNoCompanyInTheDatabase() {
		Mockito.when(this.companyRepository.findAll()).thenReturn(new ArrayList<>());

		assertEquals(this.companyService.findAll().size(), 0);
	}

	@Test
	void addCompany_returnsCompanyException_whenACompanyWithTheGivenNameExists() {

		LocalDate date = LocalDate.of(1999, 10, 10);
		CompanyDto companyToAdd = new CompanyDto("WizzAir", "0773842602", "yahoo@yahoo.com", date, new AddressDto());
		Optional<Company> companyOptional = Optional.of(company1);
		Mockito.when(this.companyRepository.findCompanyByName("WizzAir")).thenReturn(companyOptional);
		CompanyException exception = assertThrows(CompanyException.class,
				() -> this.companyService.addCompany(companyToAdd));

		assertEquals("A company with this name does exist!", exception.getMessage());
		assertEquals(ErrorCode.EXISTING_ATTRIBUTE, exception.getErrorCode());
	}

	@Test
	void addCompany_returnsCompanyException_whenACompanyWithTheGivenPhoneNumberExists() {

		LocalDate date = LocalDate.of(1999, 10, 10);
		CompanyDto companyToAdd = new CompanyDto("Goodname", "0776162711", "yahoo@yahoo.com", date, new AddressDto());
		Optional<Company> companyOptional = Optional.of(company1);
		Mockito.when(this.companyRepository.findCompanyByPhoneNumber("0776162711")).thenReturn(companyOptional);
		CompanyException exception = assertThrows(CompanyException.class,
				() -> this.companyService.addCompany(companyToAdd));

		assertEquals("A company with this phoneNumber does exist!", exception.getMessage());
		assertEquals(ErrorCode.EXISTING_ATTRIBUTE, exception.getErrorCode());
	}

	// TODO
	@Test
	void addCompany_returnsCompanyException_whenACompanyWithTheGivenAddressExists() throws ValidatorException {

		Company company = new Company();
		CompanyDto companyToAdd = new CompanyDto();
		companyToAdd.setName("goodName");
		companyToAdd.setAddress(addressDto);
		Company companyWithSameAddress = new Company();
		companyWithSameAddress.setAddress(address);

		Mockito.when(this.companyRepository.findCompanyByName(companyToAdd.getName()))
				.thenReturn(Optional.ofNullable(null));
		Mockito.lenient().when(this.companyConverter.convertToEntity(companyToAdd)).thenReturn(company);
		Mockito.when(this.companyRepository.findCompanyByPhoneNumber(companyToAdd.getPhoneNumber()))
				.thenReturn(Optional.ofNullable(null));
		Mockito.when(this.companyRepository.findCompanyByEmail(companyToAdd.getEmail()))
				.thenReturn(Optional.ofNullable(null));
		Mockito.when(this.addressConverter.convertToEntity(companyToAdd.getAddress())).thenReturn(address);
		Mockito.when(this.companyRepository.findCompanyByAddress(address))
				.thenReturn(Optional.of(companyWithSameAddress));
		Mockito.lenient().doNothing().when(this.companyValidator).validateCompany(companyToAdd);
		CompanyException thrown = assertThrows(CompanyException.class,
				() -> this.companyService.addCompany(companyToAdd));

		assertEquals("A company with this address does exist!", thrown.getMessage());
		assertEquals(ErrorCode.EXISTING_ATTRIBUTE, thrown.getErrorCode());
	}

	@Test
	void addCompany_returnsCompanyException_whenACompanyWithTheGivenEmailExists() {

		LocalDate date = LocalDate.of(1999, 10, 10);
		CompanyDto companyToAdd = new CompanyDto("Goodname", "0776162700", "wizz@gmail.com", date, new AddressDto());
		Optional<Company> companyOptional = Optional.of(company1);
		Mockito.when(this.companyRepository.findCompanyByEmail("wizz@gmail.com")).thenReturn(companyOptional);
		CompanyException exception = assertThrows(CompanyException.class,
				() -> this.companyService.addCompany(companyToAdd));

		assertEquals("A company with this email does exist!", exception.getMessage());
		assertEquals(ErrorCode.EXISTING_ATTRIBUTE, exception.getErrorCode());
	}

	@Test
	void addCompany_returnsValidatorException_whenTheNameIsNOtOutOfLetters() throws ValidatorException {

		LocalDate date = LocalDate.of(1999, 10, 10);
		CompanyDto companyToAdd = new CompanyDto("Goodname", "0776162700", "goddEmail@gmail.com", date,
				new AddressDto());

		try (final MockedStatic<StringUtils> mocked = Mockito.mockStatic(StringUtils.class)) {

			mocked.when(() -> StringUtils.isAsciiPrintable(companyToAdd.getName())).thenReturn(false);
			doThrow(new ValidatorException("Name can contain only letters!", ErrorCode.IS_NOT_OUT_OF_LETTERS))
					.when(this.companyValidator).validateCompany(companyToAdd);
			ValidatorException exception = assertThrows(ValidatorException.class,
					() -> this.companyValidator.validateCompany(companyToAdd));

			assertEquals("Name can contain only letters!", exception.getMessage());
			assertEquals(ErrorCode.IS_NOT_OUT_OF_LETTERS, exception.getErrorCode());

			ValidatorException exceptionForService = assertThrows(ValidatorException.class,
					() -> this.companyService.addCompany(companyToAdd));

			assertEquals(exception, exceptionForService);

		}

	}

	@Test
	void addCompany_returnsTrue_whenTheTheNewCompanyIsSaved() throws CompanyException, ValidatorException {

		LocalDate date = LocalDate.of(1999, 10, 10);
		CompanyDto companyToAdd = new CompanyDto("Goodname", "0776162700", "goddEmail@gmail.com", date, addressDto);
		assertTrue(this.companyService.addCompany(companyToAdd));

	}

	@Test
	void addCompany_returnsCompanyException_whenTheNameIsEmpty() {

		CompanyDto companyToAdd = new CompanyDto();

		CompanyException exception = assertThrows(CompanyException.class,
				() -> this.companyService.addCompany(companyToAdd));

		assertEquals("A company should have a name!", exception.getMessage());
		assertEquals(ErrorCode.EMPTY_FIELD, exception.getErrorCode());

	}

	@Test
	void updateCompany_throwsCompanyException_whenTheNameIsEmpty() {
		CompanyDto companyToUpdate = new CompanyDto();

		CompanyException exception = assertThrows(CompanyException.class,
				() -> this.companyService.updateCompany(companyToUpdate));

		assertEquals("A company should have a name!", exception.getMessage());
		assertEquals(ErrorCode.EMPTY_FIELD, exception.getErrorCode());
	}

	@Test
	void updateCompany_throwsValidatorException_whenTheNameIsNotValid() throws ValidatorException {
		CompanyDto companyToUpdate = new CompanyDto();
		companyToUpdate.setName("wrongName");
		Company company = new Company();
		company.setName("wrongName");
		Mockito.when(this.companyRepository.findCompanyByName(companyToUpdate.getName()))
				.thenReturn(Optional.of(company));
		Mockito.doThrow(new ValidatorException("Name can contain only letters!", ErrorCode.IS_NOT_OUT_OF_LETTERS))
				.when(this.companyValidator).validateCompany(companyToUpdate);
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.companyService.updateCompany(companyToUpdate));

		assertEquals("Name can contain only letters!", thrown.getMessage());
		assertEquals(ErrorCode.IS_NOT_OUT_OF_LETTERS, thrown.getErrorCode());
	}

	@Test
	void updateCompany_throwsValidatorException_whenTheNameIsTooLong() throws ValidatorException {
		CompanyDto companyToUpdate = new CompanyDto();
		companyToUpdate.setName("thisNameIsVeryVeryLongAndIsNotGoodForTheCompany");
		Company company = new Company();
		company.setName(companyToUpdate.getName());
		Mockito.when(this.companyRepository.findCompanyByName(companyToUpdate.getName()))
				.thenReturn(Optional.of(company));
		Mockito.doThrow(
				new ValidatorException("Name can can have the lenght between 1 and 30!", ErrorCode.WRONG_INTERVAL))
				.when(this.companyValidator).validateCompany(companyToUpdate);
		ValidatorException thrown = assertThrows(ValidatorException.class,
				() -> this.companyService.updateCompany(companyToUpdate));

		assertEquals("Name can can have the lenght between 1 and 30!", thrown.getMessage());
		assertEquals(ErrorCode.WRONG_INTERVAL, thrown.getErrorCode());
	}

	@Test
	void updateCompany_throwsCompanyException_whenThePhoneNumberIsAlreadyUsed() throws ValidatorException {
		CompanyDto companyToUpdate = new CompanyDto();
		companyToUpdate.setName("goodName");
		companyToUpdate.setPhoneNumber("0772382900");
		Company company = new Company();
		Company companyWithSamePhoneNumber = new Company();
		companyWithSamePhoneNumber.setPhoneNumber("0772382900");
		Mockito.when(this.companyRepository.findCompanyByName(companyToUpdate.getName()))
				.thenReturn(Optional.of(company));
		Mockito.when(this.companyConverter.convertToEntity(companyToUpdate)).thenReturn(company);
		Mockito.lenient().when(this.companyRepository.findCompanyByPhoneNumber(companyToUpdate.getPhoneNumber()))
				.thenReturn(Optional.of(companyWithSamePhoneNumber));
		Mockito.doNothing().when(this.companyValidator).validateCompany(companyToUpdate);
		CompanyException thrown = assertThrows(CompanyException.class,
				() -> this.companyService.updateCompany(companyToUpdate));

		assertEquals("A company with this phoneNumber does exist!", thrown.getMessage());
		assertEquals(ErrorCode.EXISTING_ATTRIBUTE, thrown.getErrorCode());
	}

	@Test
	void updateCompany_throwsCompanyException_whenTheEmailIsAlreadyUsed() throws ValidatorException {
		CompanyDto companyToUpdate = new CompanyDto();
		companyToUpdate.setName("goodName");
		companyToUpdate.setEmail("usedEmail@yahoo.com");
		Company company = new Company();
		Company companyWithSameEmail = new Company();
		companyWithSameEmail.setEmail("usedEmail@yahoo.com");
		Mockito.when(this.companyRepository.findCompanyByName(companyToUpdate.getName()))
				.thenReturn(Optional.of(company));
		Mockito.when(this.companyConverter.convertToEntity(companyToUpdate)).thenReturn(company);
		Mockito.when(this.companyRepository.findCompanyByPhoneNumber(companyToUpdate.getPhoneNumber()))
				.thenReturn(Optional.ofNullable(null));
		Mockito.when(this.companyRepository.findCompanyByEmail(companyToUpdate.getEmail()))
				.thenReturn(Optional.of(companyWithSameEmail));
		Mockito.doNothing().when(this.companyValidator).validateCompany(companyToUpdate);
		CompanyException thrown = assertThrows(CompanyException.class,
				() -> this.companyService.updateCompany(companyToUpdate));

		assertEquals("A company with this email does exist!", thrown.getMessage());
		assertEquals(ErrorCode.EXISTING_ATTRIBUTE, thrown.getErrorCode());
	}

	@Test
	void updateCompany_throwsCompanyException_whenACompanyWithThatAdressAlreadyExists() throws ValidatorException {
		Address address2 = mock(Address.class);
		CompanyDto companyToUpdate = new CompanyDto();
		companyToUpdate.setName("goodName");
		companyToUpdate.setAddress(addressDto);

		Company oldCompany = new Company();
		oldCompany.setAddress(address2);

		Company updateCompany = new Company();
		updateCompany.setName("goodName");
		updateCompany.setAddress(address);

		Company companyWithSameAddress = new Company();
		companyWithSameAddress.setAddress(address);

		Mockito.when(this.companyRepository.findCompanyByName(companyToUpdate.getName()))
				.thenReturn(Optional.of(oldCompany));
		Mockito.when(this.companyConverter.convertToEntity(companyToUpdate)).thenReturn(updateCompany);
		Mockito.when(this.companyRepository.findCompanyByPhoneNumber(companyToUpdate.getPhoneNumber()))
				.thenReturn(Optional.ofNullable(null));
		Mockito.when(this.companyRepository.findCompanyByEmail(companyToUpdate.getEmail()))
				.thenReturn(Optional.ofNullable(null));
		Mockito.when(this.addressConverter.convertToEntity(companyToUpdate.getAddress())).thenReturn(address);
		Mockito.when(this.companyRepository.findCompanyByAddress(address))
				.thenReturn(Optional.of(companyWithSameAddress));
		Mockito.doNothing().when(this.companyValidator).validateCompany(companyToUpdate);
		CompanyException thrown = assertThrows(CompanyException.class,
				() -> this.companyService.updateCompany(companyToUpdate));

		assertEquals("A company with this address already exists!", thrown.getMessage());
		assertEquals(ErrorCode.EXISTING_ATTRIBUTE, thrown.getErrorCode());
	}

	@Test
	void updateCompany_throwsNothing_whenTheCompanyIsUpdated() throws ValidatorException, CompanyException {

		CompanyDto companyToUpdate = new CompanyDto();
		companyToUpdate.setName("goodName");
		Company oldCompany = new Company();
		Company updateCompany = new Company();

		Mockito.when(this.companyRepository.findCompanyByName(companyToUpdate.getName()))
				.thenReturn(Optional.of(oldCompany));
		Mockito.when(this.companyConverter.convertToEntity(companyToUpdate)).thenReturn(updateCompany);
		Mockito.when(this.companyRepository.findCompanyByPhoneNumber(companyToUpdate.getPhoneNumber()))
				.thenReturn(Optional.ofNullable(null));
		Mockito.when(this.companyRepository.findCompanyByEmail(companyToUpdate.getEmail()))
				.thenReturn(Optional.ofNullable(null));
		Mockito.when(this.addressConverter.convertToEntity(companyToUpdate.getAddress())).thenReturn(address);
		Mockito.when(this.companyRepository.findCompanyByAddress(address)).thenReturn(Optional.ofNullable(null));
		Mockito.doNothing().when(this.companyValidator).validateCompany(companyToUpdate);

		this.companyService.updateCompany(companyToUpdate);
	}

	@Test
	void updateCompany_throwsCompanyException_whenACompanyWithThatNameDoesNotExist() {
		CompanyDto companyToUpadte = new CompanyDto();
		companyToUpadte.setName("wrongName");

		Mockito.when(this.companyRepository.findCompanyByName(companyToUpadte.getName()))
				.thenReturn(Optional.ofNullable(null));
		CompanyException exception = assertThrows(CompanyException.class,
				() -> this.companyService.updateCompany(companyToUpadte));
		assertEquals("A company with this name does not exist!", exception.getMessage());
		assertEquals(ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB, exception.getErrorCode());
	}
	
	@Test
	void deleteCompany_throwsCompanyException_whenACompanyWithTheGivenNameDoesNotExists() {
		Mockito.when(this.companyRepository.findCompanyByName("wrongName")).thenReturn(Optional.ofNullable(null));
		CompanyException exception = assertThrows(CompanyException.class,
				() -> this.companyService.deleteCompany("wrongName"));
		assertEquals("A company with this name does not exist", exception.getMessage());
		assertEquals(ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB, exception.getErrorCode());
	}
	
	@Test
	void deleteCompany_throwsNothing_whenTheCompanyIsDeleted() throws CompanyException {
		Company company = mock(Company.class);
		Mockito.when(this.companyRepository.findCompanyByName("goodName")).thenReturn(Optional.of(company));
		this.companyService.deleteCompany("goodName");
	}

	private Company createCompany(String name, String phoneNumber, String email, LocalDate foundedIn, Address address) {

		Company company = new Company();
		company.setEmail(email);
		company.setPhoneNumber(phoneNumber);
		company.setName(name);
		company.setFoundedIn(foundedIn);
		company.setAddress(address);

		return company;
	}

	private Address createAddress(Long id, String country, String city, String street, int streetNumber) {

		Address addressToCreate = new Address();
		addressToCreate.setIdAddress(id);
		addressToCreate.setCountry(country);
		addressToCreate.setCity(city);
		addressToCreate.setStreet(street);
		addressToCreate.setStreetNumber(streetNumber);

		return addressToCreate;

	}

}
