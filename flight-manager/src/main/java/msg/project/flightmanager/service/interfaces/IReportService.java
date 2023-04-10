package msg.project.flightmanager.service.interfaces;

import msg.project.flightmanager.dto.ReportDto;

public interface IReportService {
	
	ReportDto generateReport(ReportDto dtoToCreate);
	boolean deleteReport(String reportCode);

}
