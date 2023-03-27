package msg.project.flightmanager.servicies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.supercsv.io.CsvBeanWriter;

import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.repository.AirportRepository;
import msg.project.flightmanager.repository.CompanyRepository;
import msg.project.flightmanager.repository.PlaneRepository;
import msg.project.flightmanager.repository.UserRepository;
import msg.project.flightmanager.service.CSVExporterService;
import msg.project.flightmanager.service.utils.CsvBeanWriterUtils;

@ExtendWith(MockitoExtension.class)
public class CSVExporterServiceTest {
	@InjectMocks
	private CSVExporterService service;
	@Mock
	private UserRepository userRepository;
	@Mock
	private PlaneRepository planeRepository;
	@Mock
	private AirportRepository airportRepository;
	@Mock
	private CompanyRepository companyRepository;
	@Mock
	private CsvBeanWriterUtils beanWriterUtils;

	
	@Test
	void exportToCSV_throwsFlightManagerException_whenNoUsersFoundToExport() throws IOException {
		PrintWriter writer = Mockito.mock(PrintWriter.class);
	      
		CsvBeanWriter csvWriter = Mockito.mock(CsvBeanWriter.class);
		
		Mockito.when(this.beanWriterUtils.getCsvBeanWriter(writer)).thenReturn(csvWriter);
		Mockito.when(this.userRepository.findAll()).thenReturn(Collections.emptyList());
			
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.exportToCSV(User.class, writer));
			
		assertEquals("No users found to export", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.exportToCSV(User.class, writer));
	}
	
	@Test
	void exportToCSV_throwsFlightManagerException_whenNoPlanesFoundToExport() throws IOException {
		PrintWriter writer = Mockito.mock(PrintWriter.class);
	      
		CsvBeanWriter csvWriter = Mockito.mock(CsvBeanWriter.class);
		
		Mockito.when(this.beanWriterUtils.getCsvBeanWriter(writer)).thenReturn(csvWriter);
		Mockito.when(this.planeRepository.findAll()).thenReturn(Collections.emptyList());
	
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.exportToCSV(Plane.class, writer));
			
		assertEquals("No planes found to export", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.exportToCSV(Plane.class, writer));
	}
	
	@Test
	void exportToCSV_throwsFlightManagerException_whenNoAirportsFoundToExport() throws IOException {
		PrintWriter writer = Mockito.mock(PrintWriter.class);
	      
		CsvBeanWriter csvWriter = Mockito.mock(CsvBeanWriter.class);
		
		Mockito.when(this.beanWriterUtils.getCsvBeanWriter(writer)).thenReturn(csvWriter);
		Mockito.when(this.airportRepository.findAll()).thenReturn(Collections.emptyList());
	
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.exportToCSV(Airport.class, writer));
			
		assertEquals("No airports found to export", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.exportToCSV(Airport.class, writer));
	}
	
	@Test
	void exportToCSV_throwsFlightManagerException_whenNoCompaniesFoundToExport() throws IOException {
		PrintWriter writer = Mockito.mock(PrintWriter.class);
		
		CsvBeanWriter csvWriter = Mockito.mock(CsvBeanWriter.class);
		
		Mockito.when(this.beanWriterUtils.getCsvBeanWriter(writer)).thenReturn(csvWriter);
		Mockito.when(this.companyRepository.findAll()).thenReturn(Collections.emptyList());
	
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.exportToCSV(Company.class, writer));
			
		assertEquals("No companies found to export", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.exportToCSV(Company.class, writer));
	}
	
	@Test
	void exportToCSV_throwsFlightManagerException_whenCanNotWirteCsvForUser() throws IOException {
		String[] fieldMappingUser = {"id", "username", "firstName", "lastName", "email", "phoneNumber", "birthDate", "isActive"};

		User user = Mockito.mock(User.class);
		PrintWriter writer = Mockito.mock(PrintWriter.class);
		
		Mockito.when(this.userRepository.findAll()).thenReturn(Arrays.asList(user));

		CsvBeanWriter csvWriter = Mockito.mock(CsvBeanWriter.class);
		
		Mockito.when(this.beanWriterUtils.getCsvBeanWriter(writer)).thenReturn(csvWriter);
		
		Mockito.doThrow(IOException.class)
		.when(csvWriter).write(user, fieldMappingUser);
			
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.exportToCSV(User.class, writer));
			
		assertEquals("Error while writing CSV for users", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.exportToCSV(User.class, writer));
	}
	
	@Test
	void exportToCSV_throwsFlightManagerException_whenCanNotWirteCsvForPlane() throws IOException {
		String[] fieldMappingPlane = {"idPlane", "capacity", "fuelTankCapacity", "manufacturingDate", "firstFlight", "lastRevistion", "size", "model", "tailNumber"};

		Plane plane = Mockito.mock(Plane.class);
		PrintWriter writer = Mockito.mock(PrintWriter.class);
		
		Mockito.when(this.planeRepository.findAll()).thenReturn(Arrays.asList(plane));

		CsvBeanWriter csvWriter = Mockito.mock(CsvBeanWriter.class);
		
		Mockito.when(this.beanWriterUtils.getCsvBeanWriter(writer)).thenReturn(csvWriter);
		
		Mockito.doThrow(IOException.class)
		.when(csvWriter).write(plane, fieldMappingPlane);
			
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.exportToCSV(Plane.class, writer));
			
		assertEquals("Error while writing CSV for planes", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.exportToCSV(Plane.class, writer));
	}
	
	@Test
	void exportToCSV_throwsFlightManagerException_whenCanNotWirteCsvForAirport() throws IOException {
		String[] fieldMappingAirport = {"id_airport", "airportName", "codeIdentifier", "runWays", "gateWays"};

		Airport airport = Mockito.mock(Airport.class);
		PrintWriter writer = Mockito.mock(PrintWriter.class);
		
		Mockito.when(this.airportRepository.findAll()).thenReturn(Arrays.asList(airport));

		CsvBeanWriter csvWriter = Mockito.mock(CsvBeanWriter.class);
		
		Mockito.when(this.beanWriterUtils.getCsvBeanWriter(writer)).thenReturn(csvWriter);
		
		Mockito.doThrow(IOException.class)
		.when(csvWriter).write(airport, fieldMappingAirport);
			
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.exportToCSV(Airport.class, writer));
			
		assertEquals("Error while writing CSV for airports", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.exportToCSV(Airport.class, writer));
	}
	
	@Test
	void exportToCSV_throwsFlightManagerException_whenCanNotWirteCsvForCompany() throws IOException {
		String[] fieldMappingCompany ={"idCompany", "name", "phoneNumber", "email", "foundedIn", "isActive"};

		Company company = Mockito.mock(Company.class);
		PrintWriter writer = Mockito.mock(PrintWriter.class);
		
		Mockito.when(this.companyRepository.findAll()).thenReturn(Arrays.asList(company));

		CsvBeanWriter csvWriter = Mockito.mock(CsvBeanWriter.class);
		
		Mockito.when(this.beanWriterUtils.getCsvBeanWriter(writer)).thenReturn(csvWriter);
		
		Mockito.doThrow(IOException.class)
		.when(csvWriter).write(company, fieldMappingCompany);
			
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.exportToCSV(Company.class, writer));
			
		assertEquals("Error while writing CSV for companies", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.exportToCSV(Company.class, writer));
	}

	@Test
	void exportToCSV_throwsFlightManagerException_whenCanNotExport () throws IOException {
		User user = Mockito.mock(User.class);
		PrintWriter writer = Mockito.mock(PrintWriter.class);
		
		Mockito.when(this.userRepository.findAll()).thenReturn(Arrays.asList(user));

		CsvBeanWriter csvWriter = Mockito.mock(CsvBeanWriter.class);
		
		Mockito.when(this.beanWriterUtils.getCsvBeanWriter(writer)).thenReturn(csvWriter);
		
		Mockito.doThrow(IOException.class)
		.when(csvWriter).close();
			
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.exportToCSV(User.class, writer));
			
		assertEquals("Error while writing CSV", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.exportToCSV(User.class, writer));
	}
	
	@Test
	void exportToCSV_returnsVoid_whenCSVCreated01UserClassCase() throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		User user = Mockito.mock(User.class);
		
		PrintWriter writer = Mockito.mock(PrintWriter.class);
	      
		Mockito.when(this.userRepository.findAll()).thenReturn(Arrays.asList(user));
		
		CsvBeanWriter csvWriter = Mockito.mock(CsvBeanWriter.class);
		
		Mockito.when(this.beanWriterUtils.getCsvBeanWriter(writer)).thenReturn(csvWriter);
		
		this.service.exportToCSV(User.class, writer);
		verify(csvWriter).close();
		
//		try (MockedConstruction<CsvBeanWriter> mocked = Mockito.mockConstruction(CsvBeanWriter.class)){
//				
//			this.service.exportToCSV(User.class, writer);
//			verify(mocked.constructed().get(1)).close();
//		}
	}
		
	@Test
	void exportToCSV_returnsVoid_whenCSVCreated02PlaneClassCase() throws IOException {
		Plane plane = Mockito.mock(Plane.class);
		PrintWriter writer = Mockito.mock(PrintWriter.class);
		
		Mockito.when(this.planeRepository.findAll()).thenReturn(Arrays.asList(plane));
			
		CsvBeanWriter csvWriter = Mockito.mock(CsvBeanWriter.class);
		
		Mockito.when(this.beanWriterUtils.getCsvBeanWriter(writer)).thenReturn(csvWriter);
		
		this.service.exportToCSV(Plane.class, writer);
		verify(csvWriter).close();
	}
	
	@Test
	void exportToCSV_returnsVoid_whenCSVCreated03AirportClassCase() throws IOException {
		Airport airport = Mockito.mock(Airport.class);
		PrintWriter writer = Mockito.mock(PrintWriter.class);
		
		Mockito.when(this.airportRepository.findAll()).thenReturn(Arrays.asList(airport));
			
		CsvBeanWriter csvWriter = Mockito.mock(CsvBeanWriter.class);
		
		Mockito.when(this.beanWriterUtils.getCsvBeanWriter(writer)).thenReturn(csvWriter);
		
		this.service.exportToCSV(Airport.class, writer);
		verify(csvWriter).close();
	}
	
	@Test
	void exportToCSV_returnsVoid_whenCSVCreated04CompanyClassCase() throws IOException {
		Company company = Mockito.mock(Company.class);
		PrintWriter writer = Mockito.mock(PrintWriter.class);
		
		Mockito.when(this.companyRepository.findAll()).thenReturn(Arrays.asList(company));
			
		CsvBeanWriter csvWriter = Mockito.mock(CsvBeanWriter.class);
		
		Mockito.when(this.beanWriterUtils.getCsvBeanWriter(writer)).thenReturn(csvWriter);
		
		this.service.exportToCSV(Company.class, writer);
		verify(csvWriter).close();
	}
}
