package msg.project.flightmanager.service.interfaces;

import msg.project.flightmanager.model.Report;

public interface IReportPdfExporterService {
	
	void exportToPdf(Report report);
}
