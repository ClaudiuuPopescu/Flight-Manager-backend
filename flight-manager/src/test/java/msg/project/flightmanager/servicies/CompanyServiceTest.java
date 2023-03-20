package msg.project.flightmanager.servicies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;

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

	@InjectMocks
	private CompanyService companyService;

	@BeforeEach
	public void init() {

		this.companyConverter = new CompanyConverter();
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

	// TODO nu vede ce trebe
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
		CompanyDto companyToAdd = new CompanyDto("Goodname", "0776162700", "goddEmail@gmail.com", date,
				addressDto);
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
		CompanyDto companyToUpadte = new CompanyDto();

		CompanyException exception = assertThrows(CompanyException.class,
				() -> this.companyService.updateCompany(companyToUpadte));

		assertEquals("A company should have a name!", exception.getMessage());
		assertEquals(ErrorCode.EMPTY_FIELD, exception.getErrorCode());
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
