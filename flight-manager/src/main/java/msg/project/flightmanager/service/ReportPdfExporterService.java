package msg.project.flightmanager.service;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;

import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.model.Report;
import msg.project.flightmanager.service.interfaces.IReportPdfExporterService;
import msg.project.flightmanager.service.utils.PdfBeanManagementService;

@Service
public class ReportPdfExporterService implements IReportPdfExporterService{
	@Autowired
	PdfBeanManagementService beanManagement;
	
	@Override
	public void exportToPdf(Report report) {
		String reportIdentifier =  report.getFlight().getIdFlight() + "-" + report.getReportCode();
		String path = System.getProperty("user.home") + "/Downloads/report-" + reportIdentifier + ".pdf";
		
		Document document = this.beanManagement.getDocument();
		document.setPageSize(PageSize.A4);
		
		try {
			PdfWriter.getInstance(document, this.beanManagement.getFileOutputStream(path));
			
			document.open();
            addMetaData(document, report.getReportCode(), report.getReportedBy().getUsername());
            addTitlePage(document, reportIdentifier);
            addContent(document, report);
            document.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			new FlightManagerException(
					HttpStatus.NOT_FOUND,
					"File not found");
		} catch (DocumentException e) {
			e.printStackTrace();
			new FlightManagerException(
					HttpStatus.EXPECTATION_FAILED,
					"Error occured when creating the pdf document");
		}
	}

	
	private void addMetaData(Document document, String reportCode, String username) {
		document.addTitle("Report flight");
        document.addSubject(reportCode);
        document.addKeywords(reportCode);
        document.addAuthor(username);
        document.addCreator(username);
	}
	
	private void addTitlePage(Document document, String reportIdentifier) throws DocumentException {
		Paragraph emptyParagraph = this.beanManagement.getParagraph();
		addCustomLine(emptyParagraph, 1, " ");
		document.add(emptyParagraph);
		
		Paragraph title = this.beanManagement.getParagraph("Report-" + reportIdentifier);
		
		PdfPCell cellTitle = this.beanManagement.getPdfPCell();
		cellTitle.setFixedHeight(25f);
		cellTitle.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		
		cellTitle.addElement(emptyParagraph);
		cellTitle.addElement(title);
		
		document.add(cellTitle);
	}
	
	private void addContent(Document document, Report report) throws DocumentException {
		Paragraph emptyParagraph = this.beanManagement.getParagraph();
		addCustomLine(emptyParagraph, 2, " ");
		document.add(emptyParagraph);
		
		PdfPCell reportInfo = this.beanManagement.getPdfPCell();
		
		setReportDetails(reportInfo, report);
		setFlightDetails(reportInfo, report.getFlight());
		setPlaneDeatils(reportInfo, report.getFlight().getPlane());
		setReportContentMessage(reportInfo, report.getContent());
	}
	
	private void setReportDetails(PdfPCell reportInfo, Report report) {
		Paragraph generatedBy = this.beanManagement.getParagraph(
				"Generated by: " + report.getReportedBy().getFirstName() +
				" " + report.getReportedBy().getLastName() + ", " + report.getReportedBy().getUsername());
		Paragraph generatedAt = this.beanManagement.getParagraph("Generated at: " + report.getGeneratedAt());
		Paragraph reportCode = this.beanManagement.getParagraph("Report code: " + report.getReportCode());
		Paragraph reportType = this.beanManagement.getParagraph(report.getReportType().getLabel().toUpperCase());
		
		Paragraph linedParagraph = this.beanManagement.getParagraph();
		addCustomLine(linedParagraph, 1, "-".repeat(135));
		
		reportInfo.addElement(generatedBy);
		reportInfo.addElement(generatedAt);
		reportInfo.addElement(reportCode);
		reportInfo.addElement(reportType);
		reportInfo.addElement(linedParagraph);
	}
	
	private void setFlightDetails(PdfPCell reportInfo, Flight flight) {
		String countryDepart = flight.getFrom().getAddress().getCountry();
		String cityDepart = flight.getFrom().getAddress().getCity();
		String streetDepart = flight.getFrom().getAddress().getStreet();
		String countryArrival = flight.getTo().getAddress().getCountry();
		String cityArrival = flight.getTo().getAddress().getCity();
		String streetArrival = flight.getTo().getAddress().getStreet();
		
		Paragraph flightDetails = this.beanManagement.getParagraph("Flight details:");
		Paragraph flightName = this.beanManagement.getParagraph("Flight name: " + flight.getFlightName());
		Paragraph airportDepart = this.beanManagement.getParagraph("Airport deparature: " + countryDepart + ", " + ", " + cityDepart + ", " + streetDepart);
		Paragraph deparatureTime = this.beanManagement.getParagraph("Deparature time: " + flight.getBoardingTime());
		Paragraph airportArrival = this.beanManagement.getParagraph("Airport arrival: " + countryArrival + ", " + ", " + cityArrival + ", " + streetArrival);
		Paragraph duration = this.beanManagement.getParagraph("Duration: " + flight.getDuration());
		Paragraph linedParagraph = this.beanManagement.getParagraph();
		addCustomLine(linedParagraph, 1, "-".repeat(135));
		
		reportInfo.addElement(flightDetails);
		reportInfo.addElement(flightName);
		reportInfo.addElement(airportDepart);
		reportInfo.addElement(deparatureTime);
		reportInfo.addElement(airportArrival);
		reportInfo.addElement(duration);
		reportInfo.addElement(linedParagraph);
	}
	
	private void setPlaneDeatils(PdfPCell reportInfo, Plane plane) {
		Paragraph planeDetails = this.beanManagement.getParagraph("Plane details: ");
		Paragraph companyName = this.beanManagement.getParagraph("From company: " + plane.getCompany().getName());
		Paragraph tailNumber = this.beanManagement.getParagraph("Tail number: " + plane.getTailNumber());
		Paragraph model = this.beanManagement.getParagraph("Model: " + plane.getModel());
		Paragraph capacity = this.beanManagement.getParagraph("Capcity: " + plane.getCapacity());
		Paragraph manufacturingDate = this.beanManagement.getParagraph("Manufactured on: " + plane.getManufacturingDate());
		Paragraph firstFlight = this.beanManagement.getParagraph("First flight on: " + plane.getFirstFlight());
		Paragraph lastRevision = this.beanManagement.getParagraph("Last revision on: " + plane.getLastRevision());
		Paragraph linedParagraph = this.beanManagement.getParagraph();
		addCustomLine(linedParagraph, 1, "-".repeat(135));
		
		reportInfo.addElement(planeDetails);
		reportInfo.addElement(companyName);
		reportInfo.addElement(tailNumber);
		reportInfo.addElement(model);
		reportInfo.addElement(capacity);
		reportInfo.addElement(manufacturingDate);
		reportInfo.addElement(firstFlight);
		reportInfo.addElement(lastRevision);
		reportInfo.addElement(linedParagraph);
	}
	
	private void setReportContentMessage(PdfPCell reportInfo, String reportContent) {
		Paragraph reportContentP = this.beanManagement.getParagraph("This report provides the following information: ");
		Paragraph content = this.beanManagement.getParagraph(reportContent);
		
		reportInfo.addElement(reportContentP);
		reportInfo.addElement(content);
	}
	
    private void addCustomLine(Paragraph paragraph, int number, String content) {
        for (int i = 0; i < number; i++) {
            paragraph.add(this.beanManagement.getParagraph(content));
        }
    }
}
