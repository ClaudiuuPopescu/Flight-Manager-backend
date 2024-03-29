package msg.project.flightmanager.servicies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanReader;

import msg.project.flightmanager.converter.AddressConverter;
import msg.project.flightmanager.dto.CompanyDto;
import msg.project.flightmanager.exceptions.CompanyException;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.modelHelper.CreateAirportModel;
import msg.project.flightmanager.modelHelper.CreatePlaneModel;
import msg.project.flightmanager.modelHelper.CreateUserModel;
import msg.project.flightmanager.service.AddressService;
import msg.project.flightmanager.service.AirportService;
import msg.project.flightmanager.service.CSVImporterService;
import msg.project.flightmanager.service.CompanyService;
import msg.project.flightmanager.service.PlaneService;
import msg.project.flightmanager.service.UserService;
import msg.project.flightmanager.service.utils.CsvBeanManagementService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CSVImporterServiceTest {
	@InjectMocks
	private CSVImporterService service;
	@Mock
	CsvBeanManagementService beanManagementService;
	@Mock
	private UserService userService;
	@Mock
	private PlaneService planeService;
	@Mock
	private AirportService airportService;
	@Mock
	private CompanyService companyService;
	@Mock
	private AddressService addressService;
	@Mock
	private AddressConverter addressConverter;
	
	public final String[] csvHeaderUser = {"Password","FirstName","LastName","Email","PhoneNumber","BirthDate","RoleTitle",
			"Country","City","Street","StreetNumber","Apartment"};
	public final String[] csvHeaderPlane = {"model","tailNumber","capacity","fuelTankCapacity","manufacturingDate","size"};
	public final String[] csvHeaderAirport = {"airportName","runWays","gateWays","Country","City","Street","StreetNumber","Apartment"};
	public final String[] csvHeaderCompany = {"Name","PhoneNumber","Email","FoundedIn","Country","City","Street","StreetNumber","Apartment"};

	@Test
	void hasCSVFormat_returnFalse_whenFileIsNotCsv() throws IOException {
		MultipartFile file = Mockito.mock(MultipartFile.class);
		
		Mockito.when(file.getContentType()).thenReturn("text/txt");
		
		assertFalse(this.service.hasCSVFormat(file));
	}
	
	@Test
	void hasCSVFormat_returnFalse_whenFileIsCsv() {
		MultipartFile file = Mockito.mock(MultipartFile.class);
		
		Mockito.when(file.getContentType()).thenReturn("text/csv");
		
		assertTrue(this.service.hasCSVFormat(file));
	}
	
	@Test
	void csvToEntity_returnsVoid_whenImportedFileCreatedUser() throws IOException, ValidatorException, CompanyException {
	    File csvFile = File.createTempFile("test", ".csv");
	    Writer writer = new FileWriter(csvFile);
	    writer.write("Password,FirstName,LastName,Email,PhoneNumber,BirthDate,RoleTitle"
	    		+ "Country,City,Street,StreetNumber,Apartment\n");
	    writer.write("password123,randomFn,randomLn,email@airline.com,0753247035,2000-05-13,crew,"
	    		+ "Romania,Targu Mures,Insulei,1,null");
	    writer.close();
	    
	    MultipartFile file = Mockito.mock(MultipartFile.class);
	    
		CsvBeanReader reader = Mockito.mock(CsvBeanReader.class);
		
		Mockito.when(this.beanManagementService.getCsvBeanReader(file)).thenReturn(reader);
		Mockito.when(reader.getHeader(true)).thenReturn(this.csvHeaderUser);
		Mockito.when(this.beanManagementService.getHeaderSimplify(this.csvHeaderUser)).thenReturn(this.csvHeaderUser);

		CreateUserModel createUserModel = Mockito.mock(CreateUserModel.class);
		
		Mockito.when(reader.read(CreateUserModel.class, this.csvHeaderUser, this.beanManagementService.getUserProcessor())).thenReturn(createUserModel).thenReturn(null);
		
		this.service.csvToEntity(User.class, file);
		
		verify(this.userService).createUser(createUserModel);
	}
	
	@Test
	void csvToEntity_returnsVoid_whenImportedFileCreatedPlane() throws IOException, ValidatorException, CompanyException {
	    File csvFile = File.createTempFile("test", ".csv");
	    Writer writer = new FileWriter(csvFile);
	    writer.write("model,tailNumber,capacity,fuelTankCapacity,manufacturingDate,size\n");
	    writer.write("modelPlane,47,200,4500,2022-01-01,small");
	    writer.close();
	    
	    MultipartFile file = Mockito.mock(MultipartFile.class);
	    
		CsvBeanReader reader = Mockito.mock(CsvBeanReader.class);
		
		Mockito.when(this.beanManagementService.getCsvBeanReader(file)).thenReturn(reader);
		Mockito.when(reader.getHeader(true)).thenReturn(this.csvHeaderPlane);
		Mockito.when(this.beanManagementService.getHeaderSimplify(this.csvHeaderPlane)).thenReturn(this.csvHeaderPlane);
		
		CreatePlaneModel createPlaneModel= Mockito.mock(CreatePlaneModel.class);
		
		Mockito.when(reader.read(CreatePlaneModel.class, this.csvHeaderPlane, this.beanManagementService.getPlaneProcessor())).thenReturn(createPlaneModel).thenReturn(null);
		
		this.service.csvToEntity(Plane.class, file);
		
		verify(this.planeService).createPlane(createPlaneModel);
	}
	
	@Test
	void csvToEntity_returnsVoid_whenImportedFileCreatedAirport() throws IOException, ValidatorException, CompanyException {
	    File csvFile = File.createTempFile("test", ".csv");
	    Writer writer = new FileWriter(csvFile);
	    writer.write("airportName,runWays,gateWays,Country,City,Street,StreetNumber,Apartment\n");
	    writer.write("airportName,5,50,Romania,Cluj-Napoca,strada,1,");
	    writer.close();
	    
	    MultipartFile file = Mockito.mock(MultipartFile.class);
	    
		CsvBeanReader reader = Mockito.mock(CsvBeanReader.class);
		
		Mockito.when(this.beanManagementService.getCsvBeanReader(file)).thenReturn(reader);
		Mockito.when(reader.getHeader(true)).thenReturn(this.csvHeaderAirport);
		Mockito.when(this.beanManagementService.getHeaderSimplify(this.csvHeaderAirport)).thenReturn(this.csvHeaderAirport);
		
		CreateAirportModel createAirportModel = Mockito.mock(CreateAirportModel.class);
		
		Mockito.when(reader.read(CreateAirportModel.class, this.csvHeaderAirport, this.beanManagementService.getAirportProcessor())).thenReturn(createAirportModel).thenReturn(null);
		
		this.service.csvToEntity(Airport.class, file);
		
		verify(this.airportService).createAirport(createAirportModel);
	}
	
	@Test
	void csvToEntity_returnsVoid_whenImportedFileCreatedCompany() throws IOException, ValidatorException, CompanyException {
	    File csvFile = File.createTempFile("test", ".csv");
	    Writer writer = new FileWriter(csvFile);
	    writer.write("Name,PhoneNumber,Email,FoundedIn,Country,City,Street,StreetNumber,Apartment\n");
	    writer.write("Company,0753215607,company@airline.com,2000-05-13,Romania,Timisoara,Strada,1,");
	    writer.close();
	    
	    MultipartFile file = Mockito.mock(MultipartFile.class);
	    
		CsvBeanReader reader = Mockito.mock(CsvBeanReader.class);
		
		Mockito.when(this.beanManagementService.getCsvBeanReader(file)).thenReturn(reader);
		Mockito.when(reader.getHeader(true)).thenReturn(this.csvHeaderCompany);
		Mockito.when(this.beanManagementService.getHeaderSimplify(this.csvHeaderCompany)).thenReturn(this.csvHeaderCompany);

		CompanyDto companyDto = Mockito.mock(CompanyDto.class);
		
		Mockito.when(reader.read(CompanyDto.class, this.csvHeaderCompany, this.beanManagementService.getAirportProcessor())).thenReturn(companyDto).thenReturn(null);
		
		this.service.csvToEntity(Company.class, file);
		
		verify(this.companyService).addCompany(companyDto);
	}
	
	@Test
	void csvToEntity_throwsFlightManagerException_whenCanNotGetHeader() throws IOException {
		MultipartFile file = Mockito.mock(MultipartFile.class);
		
		CsvBeanReader reader = Mockito.mock(CsvBeanReader.class);
		
		Mockito.when(this.beanManagementService.getCsvBeanReader(file)).thenReturn(reader);
		Mockito.when(reader.getHeader(true)).thenThrow(IOException.class);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.csvToEntity(User.class, file));

		assertEquals(MessageFormat.format("Couldn't read the header of file {0}", file.getOriginalFilename()), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.csvToEntity(User.class, file));

	}
	
	@Test
	void csvToEntity_throwsFlightManagerException_whenCanNotCloseReader() throws IOException {
		MultipartFile file = Mockito.mock(MultipartFile.class);
		
		CsvBeanReader reader = Mockito.mock(CsvBeanReader.class);
		
		Mockito.when(this.beanManagementService.getCsvBeanReader(file)).thenReturn(reader);
		Mockito.when(reader.getHeader(true)).thenReturn(this.csvHeaderPlane);
		
		Mockito.doThrow(IOException.class)
		.when(reader).close();
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.csvToEntity(User.class, file));

		assertEquals("Error closing the reader", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.csvToEntity(User.class, file));
	}
	
	@Test
	void csvToEntity_throwsFlightManagerException_whenCanNotCreateEntity() throws IOException, ValidatorException {
	    File csvFile = File.createTempFile("test", ".csv");
		Writer writer = new FileWriter(csvFile);
	    writer.write("Password,FirstName,LastName,Email,PhoneNumber,BirthDate,RoleTitle"
	    		+ "Country,City,Street,StreetNumber,Apartment\n");
	    writer.write("password123,randomFn,randomLn,email@airline.com,0753247035,2000-05-13,crew,"
	    		+ "Romania,Targu Mures,Insulei,1,null");
	    writer.close();
	    
	    MultipartFile file = Mockito.mock(MultipartFile.class);
	    
		CsvBeanReader reader = Mockito.mock(CsvBeanReader.class);
		
		Mockito.when(this.beanManagementService.getCsvBeanReader(file)).thenReturn(reader);
		Mockito.when(reader.getHeader(true)).thenReturn(this.csvHeaderUser);
		Mockito.when(this.beanManagementService.getHeaderSimplify(this.csvHeaderUser)).thenReturn(this.csvHeaderUser);

		CreateUserModel createUserModel = Mockito.mock(CreateUserModel.class);
		
		Mockito.when(reader.read(CreateUserModel.class, this.csvHeaderUser, this.beanManagementService.getUserProcessor())).thenReturn(createUserModel).thenReturn(null);
		Mockito.when(this.beanManagementService.getCsvBeanReader(file)).thenReturn(reader);
		Mockito.when(reader.getHeader(true)).thenReturn(this.csvHeaderUser);
		Mockito.doThrow(ValidatorException.class).when(this.userService).createUser(createUserModel);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.csvToEntity(User.class, file));

		assertEquals(MessageFormat.format("Error while creating {0} from import", User.class.getSimpleName().toLowerCase()), thrown.getMessage());
	}
}


