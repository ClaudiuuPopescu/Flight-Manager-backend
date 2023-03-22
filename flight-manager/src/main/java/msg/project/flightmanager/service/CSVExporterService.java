package msg.project.flightmanager.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import jakarta.servlet.http.HttpServletResponse;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.repository.AirportRepository;
import msg.project.flightmanager.repository.CompanyRepository;
import msg.project.flightmanager.repository.PlaneRepository;
import msg.project.flightmanager.repository.UserRepository;

@Service
public class CSVExporterService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PlaneRepository planeRepository;
	@Autowired
	private AirportRepository airportRepository;
	@Autowired
	private CompanyRepository companyRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(CSVExporterService.class);
	
	private static String objectInstance = "";
	private static String [] csvHeader;
	private static String [] fieldMapping;
	
	private static ICsvBeanWriter csvWriter;
	
	public void exportToCSV(Object object, HttpServletResponse response) {
		
		DateFormat dateFomat = new SimpleDateFormat("dd-MM-yyyy");
		String timeStamp = dateFomat.format(new Date());
		String fileName = objectInstance + "-export-" + timeStamp + ".csv";
		
		response.setContentType("text/csv");
		
		String headerKey = "Content-Disposition";
		String haderValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, haderValue);
		
		try {
			csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
			
			csvWriter.writeHeader(csvHeader);
			
			writeCSVForObject(object);
			
			csvWriter.close();
		} catch (IOException e) {
			logger.error("Error while writing CSV ", e);
		}
	}
	
	private void writeCSVForObject(Object object) {
		if(object instanceof User) {
			setStaticsForUser();
			
			List<User> users = StreamSupport.stream(this.userRepository.findAll().spliterator(), false).toList();
			
			for(User user : users) {
				try {
					csvWriter.write(user, fieldMapping);
				} catch (IOException e) {
					logger.error("Error while writing CSV for users ", e);
				}
			}
		}
		
		if(object instanceof Plane) {
			setStaticsForPlane();
			
			List<Plane> planes = StreamSupport.stream(this.planeRepository.findAll().spliterator(), false).toList();
			
			for(Plane plane : planes) {
				try {
					csvWriter.write(plane, fieldMapping);
				} catch (IOException e) {
					logger.error("Error while writing CSV for planes ", e);
				}
			}
		}
		
		if(object instanceof Airport) {
			setStaticsForAirport();
			
			List<Airport> airports = StreamSupport.stream(this.airportRepository.findAll().spliterator(), false).toList();

			for(Airport airport : airports) {
				try {
					csvWriter.write(airport, fieldMapping);
				} catch (IOException e) {
					logger.error("Error while writing CSV for airports ", e);
				}
			}
		}
		
		if(object instanceof Company) {
			setStaticsForCompany();

			List<Company> companies = StreamSupport.stream(this.companyRepository.findAll().spliterator(), false).toList();

			for(Company company : companies) {
				try {
					csvWriter.write(company, fieldMapping);
				} catch (IOException e) {
					logger.error("Error while writing CSV for companies ", e);
				}
			}
		}
	}
	
	private void setStaticsForUser() {
		objectInstance = "users";
		csvHeader = new String[] {"ID", "USERNAME", "FIRST NAME", "LAST NAME", "EMAIL", "PHONE NUMBER", "BIRTHDAY", "ACTIVE"};
		fieldMapping = new String[] {"id", "username", "firstName", "lastName", "email", "phoneNumber", "birthDay", "isActive"};
	}
	
	private void setStaticsForPlane() {
		objectInstance = "planes";
		csvHeader = new String[] {"ID", "CAPACITY", "FUEL TANK CAPACITY", "MANUFACTURING DATE", "FIRST FLIGHT", "LAST REVISION", "SIZE", "MODEL", "TAIL NUMBER"};
		fieldMapping = new String[] {"idPlane", "capacity", "fuelTankCapacity", "manufacturingDate", "firstFlight", "lastRevistion", "size", "model", "tailNumber"};
	}
	
	private void setStaticsForAirport() {
		objectInstance = "airports";
		csvHeader = new String[] {"ID", "NAME", "CODE IDENTIFIER", "RUN WAYS", "GATE WAYS"};
		fieldMapping = new String[] {"id_airport", "airportName", "codeIdentifier", "runWays", "gateWays"};
	}
	
	private void setStaticsForCompany() {
		objectInstance = "companies";
		csvHeader = new String[] {"ID", "NAME", "PHONE NUMBER", "EMAIL", "FOUNDED IN", "ACTIVE"};
		fieldMapping = new String[] {"idCompany", "name", "phoneNumber", "email", "foundedIn", "activ"};
	}
}
