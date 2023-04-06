package msg.project.flightmanager.service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import msg.project.flightmanager.converter.ReportConverter;
import msg.project.flightmanager.dto.ReportDto;
import msg.project.flightmanager.enums.ReportTypeEnum;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Report;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.repository.ReportRepository;
import msg.project.flightmanager.repository.UserRepository;

@Service
public class ReportService {
	
	@Autowired
	private ReportRepository reportRepository;
	@Autowired
	private ReportConverter reportConverter;
	@Autowired
	private UserRepository userRepository;
	
	public ReportDto generateReport(ReportDto dtoToCreate) {
		if(dtoToCreate.getContent().length() < 50) {
			throw new FlightManagerException(
					HttpStatus.LENGTH_REQUIRED,
					"Content must have a minimum length of 50 characters");
		}
		
		Report report = this.reportConverter.convertToEntity(dtoToCreate);
		report.setContent(dtoToCreate.getContent());
		
		ReportTypeEnum reportType = ReportTypeEnum.fromLabel(dtoToCreate.getReportType());
		report.setReportType(reportType);
		
		String reportCode = generateReportCode(reportType);
		report.setReportCode(reportCode);
		
		User generatedBy = this.userRepository.findByUsername(dtoToCreate.getReporteByUsername())
				.orElseThrow(() -> new FlightManagerException(
						HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not generate report. User {0} not found", dtoToCreate.getReporteByUsername())));
		report.setReportedBy(generatedBy);
		
		this.reportRepository.save(report);
		
		return this.reportConverter.convertToDTO(report);
	}
	
	private String generateReportCode(ReportTypeEnum typeEnum) {
		LocalDate generatedAt = LocalDate.now(); // yyyy-mm-dd
		String date = generatedAt.toString().replace("-", ""); // yyyymmdd
		
		Optional<Long> lastIdOfType = this.reportRepository.findLastIdByType(typeEnum);
		
		Long lastId = lastIdOfType.isPresent() ? lastIdOfType.get() : 0;
		
		String reportCode = String.format("%s-%s-%03d", typeEnum.toString(), date, lastId);
		
		return reportCode;
	}
}
