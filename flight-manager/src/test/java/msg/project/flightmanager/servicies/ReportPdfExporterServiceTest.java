package msg.project.flightmanager.servicies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.itextpdf.text.Document;

import msg.project.flightmanager.enums.ReportTypeEnum;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Address;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.model.Report;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.service.ReportPdfExporterService;
import msg.project.flightmanager.service.utils.PdfBeanManagementService;

@ExtendWith(MockitoExtension.class)
public class ReportPdfExporterServiceTest {
	@InjectMocks
	private ReportPdfExporterService service;
	@Mock
	private PdfBeanManagementService beanManagement;
	
	private Report report;
	private User reportedBy;
	private Flight flight;
	private Airport airportFrom;
	private Address addressFrom; 
	private Airport airportTo;
	private Address addressTo;
	private Plane plane;
	private Company company;
	
//	try (MockedStatic<PdfWriter> pdfWriter = Mockito.mockStatic(PdfWriter.class)) {
//	pdfWriter.when(() -> PdfWriter.getInstance(document, stream));
//}
	
	@Test
	void exportToPdf_throwsFlightManagerException_whenFileNotFound() throws FileNotFoundException {
			String systemPath = System.getProperty("user.home");
			String reportIdentifier =  this.report.getFlight().getIdFlight() + "-" + this.report.getReportCode();
			String path = systemPath + "/Downloads/report-" + reportIdentifier + ".pdf";
			
			Document document = Mockito.mock(Document.class);
			
			Mockito.when(this.beanManagement.getSystemPropertyHome()).thenReturn(systemPath);
			Mockito.when(this.beanManagement.getDocument()).thenReturn(document);
			
			Mockito.when(this.beanManagement.getFileOutputStream(path)).thenThrow(FileNotFoundException.class);
			
			FlightManagerException thrown = assertThrows(FlightManagerException.class,
					() -> this.service.exportToPdf(this.report));
				
			assertEquals("File not found", thrown.getMessage());
			assertThrows(FlightManagerException.class, () -> this.service.exportToPdf(this.report));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@BeforeEach
	void init() {		
		this.addressFrom = Address.builder()
				.country("countryFrom")
				.city("cityFrom")
				.street("streetFrom").build();
		
		this.airportFrom = Airport.builder()
				.airportName("airportFromName")
				.address(this.addressFrom).build();
		
		this.addressTo = Address.builder()
				.country("countryTo")
				.city("cityTo")
				.street("streetTo").build();
		
		this.airportTo = Airport.builder()
				.airportName("airportToName")
				.address(this.addressTo).build();
		
		this.company = Company.builder()
				.name("companyName").build();
		
		this.plane = Plane.builder()
				.company(this.company)
				.tailNumber(47)
				.model("planeModel")
				.capacity(100)
				.manufacturingDate(LocalDate.of(2017, 05, 13))
				.firstFlight(LocalDate.of(2017, 07, 16))
				.lastRevision(LocalDate.of(2022, 07, 22)).build();
	
		this.flight = Flight.builder()
				.idFlight(13L)
				.flightName("flightName")
				.plane(this.plane)
				.from(this.airportFrom)
				.to(this.airportTo)
				.duration(5.2)
				.build();
		
		this.reportedBy = User.builder()
				.firstName("firstname")
				.lastName("lastName")
				.username("username").build();
		
		this.report = Report.builder()
				.reportCode("FR-" + LocalDate.now().toString().replace("-", "") + "-002")
				.reportType(ReportTypeEnum.FR)
				.content("content")
				.generatedAt(LocalDate.now())
				.reportedBy(this.reportedBy)
				.flight(this.flight).build();
	}
}
