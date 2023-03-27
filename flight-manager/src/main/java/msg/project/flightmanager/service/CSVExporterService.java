package msg.project.flightmanager.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;

import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.repository.AirportRepository;
import msg.project.flightmanager.repository.CompanyRepository;
import msg.project.flightmanager.repository.PlaneRepository;
import msg.project.flightmanager.repository.UserRepository;
import msg.project.flightmanager.service.utils.CsvBeanWriterUtils;

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
	@Autowired
	private CsvBeanWriterUtils csvBeanWriterUtils;
	
	private static final Logger logger = LoggerFactory.getLogger(CSVExporterService.class);
	
	private final String[] csvHeaderUser = {"ID", "USERNAME", "FIRST NAME", "LAST NAME", "EMAIL", "PHONE NUMBER", "BIRTH DATE", "ACTIVE"};
	private final String[] fieldMappingUser = {"id", "username", "firstName", "lastName", "email", "phoneNumber", "birthDate", "isActive"};
	
	private final String[] csvHeaderPlane = {"ID", "CAPACITY", "FUEL TANK CAPACITY", "MANUFACTURING DATE", "FIRST FLIGHT", "LAST REVISION", "SIZE", "MODEL", "TAIL NUMBER"};
	private final String[] fieldMappingPlane = {"idPlane", "capacity", "fuelTankCapacity", "manufacturingDate", "firstFlight", "lastRevistion", "size", "model", "tailNumber"};
	
	private final String[] csvHeaderAirport = {"ID", "NAME", "CODE IDENTIFIER", "RUN WAYS", "GATE WAYS"};
	private final String[] fieldMappingAirport = {"id_airport", "airportName", "codeIdentifier", "runWays", "gateWays"};
	
	private final String[] csvHeaderCompany = {"ID", "NAME", "PHONE NUMBER", "EMAIL", "FOUNDED IN", "ACTIVE"};
	private final String[] fieldMappingCompany ={"idCompany", "name", "phoneNumber", "email", "foundedIn", "isActive"};
	
	public CsvBeanWriter csvWriter;
	
	// TODO to add in the controller
//		LocalDate timeStamp = LocalDate.now();
//		String fileName = clazz.getSimpleName().toLowerCase() + "-export-" + timeStamp + ".csv";
//		
//		response.setContentType("text/csv");
//		
//		String headerKey = HttpHeaders.CONTENT_DISPOSITION;
//		String haderValue = "attachment; filename=" + fileName;
//		response.setHeader(headerKey, haderValue);
//		PrintWriter writer = new PrintWriter (new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
	public void exportToCSV(Class<?> clazz, PrintWriter writer) {
		
		try {
			this.csvWriter = this.csvBeanWriterUtils.getCsvBeanWriter(writer);
			
			writeCSVForObject(clazz);
			
			this.csvWriter.close();
		} catch (IOException e) {
			logger.error("Error while writing CSV ", e);
			throw new FlightManagerException(
					HttpStatus.EXPECTATION_FAILED, 
					"Error while writing CSV");
		}
	}
	
	private void writeCSVForObject(Class<?> clazz) throws IOException {
		if(clazz == User.class) {
			
			this.csvWriter.writeHeader(this.csvHeaderUser);
			
			List<User> users = StreamSupport.stream(this.userRepository.findAll().spliterator(), false).toList();
			
			if(users.isEmpty()) {
				throw new FlightManagerException(
						HttpStatus.NOT_FOUND, 
						"No users found to export");
			}
			
			for(User user : users) {
				try {
					this.csvWriter.write(user, this.fieldMappingUser);
				} catch (IOException e) {
					logger.error("Error while writing CSV for users ", e);
					throw new FlightManagerException(
							HttpStatus.EXPECTATION_FAILED, 
							"Error while writing CSV for users");
				}
			}
		}
		
		if(clazz == Plane.class) {
			this.csvWriter.writeHeader(this.csvHeaderPlane);
			
			List<Plane> planes = StreamSupport.stream(this.planeRepository.findAll().spliterator(), false).toList();
			
			if(planes.isEmpty()) {
				throw new FlightManagerException(
						HttpStatus.NOT_FOUND, 
						"No planes found to export");
			}
			
			for(Plane plane : planes) {
				try {
					this.csvWriter.write(plane, this.fieldMappingPlane);
				} catch (IOException e) {
					logger.error("Error while writing CSV for planes ", e);
					throw new FlightManagerException(
							HttpStatus.EXPECTATION_FAILED, 
							"Error while writing CSV for planes");
				}
			}
		}
		
		if(clazz == Airport.class) {
			this.csvWriter.writeHeader(this.csvHeaderAirport);
			
			List<Airport> airports = StreamSupport.stream(this.airportRepository.findAll().spliterator(), false).toList();
			
			if(airports.isEmpty()) {
				throw new FlightManagerException(
						HttpStatus.NOT_FOUND, 
						"No airports found to export");
			}

			for(Airport airport : airports) {
				try {
					this.csvWriter.write(airport, this.fieldMappingAirport);
				} catch (IOException e) {
					logger.error("Error while writing CSV for airports ", e);
					throw new FlightManagerException(
							HttpStatus.EXPECTATION_FAILED, 
							"Error while writing CSV for airports");
				}
			}
		}
		
		if(clazz == Company.class) {
			this.csvWriter.writeHeader(this.csvHeaderCompany);

			List<Company> companies = StreamSupport.stream(this.companyRepository.findAll().spliterator(), false).toList();
			
			if(companies.isEmpty()) {
				throw new FlightManagerException(
						HttpStatus.NOT_FOUND, 
						"No companies found to export");
			}

			for(Company company : companies) {
				try {
					this.csvWriter.write(company, this.fieldMappingCompany);
				} catch (IOException e) {
					logger.error("Error while writing CSV for companies ", e);
					throw new FlightManagerException(
							HttpStatus.EXPECTATION_FAILED, 
							"Error while writing CSV for companies");
				}
			}
		}
	}
}
