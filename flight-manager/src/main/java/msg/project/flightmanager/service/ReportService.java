package msg.project.flightmanager.service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import msg.project.flightmanager.converter.ReportConverter;
import msg.project.flightmanager.dto.ReportDto;
import msg.project.flightmanager.enums.ReportTypeEnum;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.Report;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.repository.FlightRepository;
import msg.project.flightmanager.repository.ReportRepository;
import msg.project.flightmanager.repository.UserRepository;
import msg.project.flightmanager.service.interfaces.IReportService;

@Service
public class ReportService implements IReportService{
	
	@Autowired
	private ReportRepository reportRepository;
	@Autowired
	private ReportConverter reportConverter;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FlightRepository flightRepository;
	
	@Transactional
	@Override
	public ReportDto generateReport(ReportDto dtoToCreate) {
		validateContent(dtoToCreate.getContent());
		
		ReportTypeEnum reportType = ReportTypeEnum.fromLabel(dtoToCreate.getReportType());
		
		String reportCode = generateReportCode(reportType);
		
		User generatedBy = this.userRepository.findByUsername(dtoToCreate.getReporteByUsername())
				.orElseThrow(() -> new FlightManagerException(
						HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not generate report. User {0} not found", dtoToCreate.getReporteByUsername())));
		
		Flight flight = this.flightRepository.findById(dtoToCreate.getFlightDto().getIdFlight())
				.orElseThrow(() -> new FlightManagerException(
						HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not generate report. Flihgt with id {0} not found", dtoToCreate.getFlightDto().getIdFlight())));
		
		Report report = this.reportConverter.convertToEntity(dtoToCreate);
		report.setReportType(reportType);
		report.setReportCode(reportCode);
		report.setReportedBy(generatedBy);
		report.setFlight(flight);
		
		this.reportRepository.save(report);
		
		return this.reportConverter.convertToDTO(report);
	}
	
	@Transactional
	@Override
	public boolean deleteReport(String reportCode) {
		Report report = this.reportRepository.findByReportCode(reportCode)
				.orElseThrow(() -> new FlightManagerException(
						HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not delete report. Report with code {0}, not found", reportCode)));
		
		this.reportRepository.delete(report);
		return true;
	}
	
	private String generateReportCode(ReportTypeEnum typeEnum) {
		LocalDate generatedAt = LocalDate.now(); // yyyy-mm-dd
		String date = generatedAt.toString().replace("-", ""); // yyyymmdd
		
		Optional<Long> lastIdOfType = this.reportRepository.findLastIdByType(typeEnum);
		
		Long newId = lastIdOfType.isPresent() ? lastIdOfType.get() +1 : 0;
		
		String reportCode = String.format("%s-%s-%03d", typeEnum.toString(), date, newId);
		
		return reportCode;
	}
	
	public void validateContent(String content) {
		if(content == null) {
			throw new FlightManagerException(
					HttpStatus.EXPECTATION_FAILED,
					"Content can not be null");
		}
		if(content.length() < 75) {
			throw new FlightManagerException(
					HttpStatus.LENGTH_REQUIRED,
					"Content must have a minimum length of 75 characters");
		}
	}
}
