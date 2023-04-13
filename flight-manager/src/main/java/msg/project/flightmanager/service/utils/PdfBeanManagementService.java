package msg.project.flightmanager.service.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;

@Component
public class PdfBeanManagementService {
	
	public String getSystemPropertyHome() {
		return System.getProperty("user.home");
	}
	
	public Document getDocument() {
		return new Document();
	}
	
	public FileOutputStream getFileOutputStream(String path) throws FileNotFoundException {
		return new FileOutputStream(path);
	}
	
	public Paragraph getParagraph() {
		return new Paragraph();
	}
	
	public Paragraph getParagraph(String content) {
		return new Paragraph(content);
	}
	
	public PdfPCell getPdfPCell() {
		return new PdfPCell();
	}
	
	public String getTimeFormatter(Time time) {
		return new SimpleDateFormat("HH:mm yyyy/MM/dd")
		.format(time);
	}

}
