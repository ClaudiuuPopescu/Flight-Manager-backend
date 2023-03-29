package msg.project.flightmanager.service.interfaces;

import java.io.PrintWriter;

public interface ICSVExporterService {
	
	void exportToCSV(Class<?> clazz, PrintWriter writer);
}
