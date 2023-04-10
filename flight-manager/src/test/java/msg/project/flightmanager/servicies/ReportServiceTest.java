package msg.project.flightmanager.servicies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.text.MessageFormat;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import msg.project.flightmanager.converter.ReportConverter;
import msg.project.flightmanager.dto.FlightDto;
import msg.project.flightmanager.dto.ReportDto;
import msg.project.flightmanager.enums.ReportTypeEnum;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.Report;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.repository.FlightRepository;
import msg.project.flightmanager.repository.ReportRepository;
import msg.project.flightmanager.repository.UserRepository;
import msg.project.flightmanager.service.ReportService;

@ExtendWith(MockitoExtension.class)
@Transactional
public class ReportServiceTest {
	
	@InjectMocks
	private ReportService reportService;
	@Mock
	private ReportRepository reportRepository;
	@Mock
	private ReportConverter reportConverter;
	@Mock
	private UserRepository userRepository;
	@Mock
	private FlightRepository flightRepository;
	
	
	@Test
	void generateReport_throwsFlightManagerException_whenContentNull() {
		String content = null;
		
		ReportDto reportDto = new ReportDto();
		reportDto.setContent(content);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.reportService.generateReport(reportDto));

		assertEquals("Content can not be null", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.reportService.generateReport(reportDto));
	}
	
	@Test
	void generateReport_throwsFlightManagerException_whenContentTooShort() {
		String content = "tooShortContent";
		
		ReportDto reportDto = new ReportDto();
		reportDto.setContent(content);
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.reportService.generateReport(reportDto));

		assertEquals("Content must have a minimum length of 75 characters", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.reportService.generateReport(reportDto));
	}
	
	@Test
	void generateReport_throwsFlightManagerException_whenNoUserFound() {
		String content = "over75CharsStringgggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg";
		String reportType = "functional";
		String username = "invalidUsername";
		
		ReportDto reportDto = new ReportDto();
		reportDto.setContent(content);
		reportDto.setReportType(reportType);
		reportDto.setReporteByUsername(username);
		
		Mockito.when(this.userRepository.findByUsername(username)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.reportService.generateReport(reportDto));

		assertEquals(MessageFormat.format("Can not generate report. User {0} not found", reportDto.getReporteByUsername()),
				thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.reportService.generateReport(reportDto));
	}
	
	@Test
	void generateReport_throwsFlightManagerException_whenNoFlightFound() {
		String content = "over75CharsStringgggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg";
		String reportType = "functional";
		String username = "validUsername";
		
		ReportDto reportDto = new ReportDto();
		reportDto.setContent(content);
		reportDto.setReportType(reportType);
		reportDto.setReporteByUsername(username);
		
		FlightDto flightDto = new FlightDto();
		flightDto.setIdFlight(1L);
		
		reportDto.setFlightDto(flightDto);
		
		Mockito.when(this.userRepository.findByUsername(username)).thenReturn(Optional.of(Mockito.mock(User.class)));
		Mockito.when(this.flightRepository.findById(reportDto.getFlightDto().getIdFlight()))
		.thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.reportService.generateReport(reportDto));

		assertEquals(MessageFormat.format("Can not generate report. Flihgt with id {0} not found", reportDto.getFlightDto().getIdFlight()),
				thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.reportService.generateReport(reportDto));
	}
	
	@Test
	void generateReport_returnsReportDto_whenReportCreated() {
		String content = "over75CharsStringgggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg";
		String reportType = "functional";
		String username = "validUsername";
		
		FlightDto flightDto = new FlightDto();
		flightDto.setIdFlight(1L);
		
		ReportDto reportDto = new ReportDto();
		reportDto.setContent(content);
		reportDto.setReportType(reportType);
		reportDto.setReporteByUsername(username);
		
		reportDto.setFlightDto(flightDto);
		
		User reportedBy = Mockito.mock(User.class);
		Flight flight = Mockito.mock(Flight.class);
		
		Mockito.when(this.userRepository.findByUsername(username)).thenReturn(Optional.of(reportedBy));
		Mockito.when(this.flightRepository.findById(reportDto.getFlightDto().getIdFlight()))
		.thenReturn(Optional.of(flight));
		
		Report report = new Report();
		report.setContent(content);
		
		Mockito.when(this.reportConverter.convertToEntity(reportDto)).thenReturn(report);
		
		Mockito.when(this.reportRepository.findLastIdByType(ReportTypeEnum.fromLabel(reportType))).thenReturn(Optional.of(1L));
		
		this.reportService.generateReport(reportDto);
		
		verify(this.reportConverter).convertToDTO(report);
	}
	
	@Test
	void deleteReport_throwsFlightManagerException_whenReportNotFound() {
		String reportCode = "FR-20230410-003";
		
		Mockito.when(this.reportRepository.findByReportCode(reportCode)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.reportService.deleteReport(reportCode));

		assertEquals(MessageFormat.format("Can not delete report. Report with code {0}, not found", reportCode),
				thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.reportService.deleteReport(reportCode));
	}
	
	@Test
	void deleteReport_returnsTrue_whenReportDeleted() {
		String reportCode = "FR-20230410-003";
		
		Report report = Mockito.mock(Report.class);
		
		Mockito.when(this.reportRepository.findByReportCode(reportCode)).thenReturn(Optional.of(report));
		
		assertTrue(this.reportService.deleteReport(reportCode));
	}
}
