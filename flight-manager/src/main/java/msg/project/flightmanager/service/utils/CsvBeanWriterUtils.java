package msg.project.flightmanager.service.utils;

import java.io.PrintWriter;

import org.springframework.stereotype.Component;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

@Component
public class CsvBeanWriterUtils {

	public CsvBeanWriter getCsvBeanWriter(PrintWriter writer) {
		return new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
	}
}
