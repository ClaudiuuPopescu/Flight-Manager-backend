package msg.project.flightmanager.service;

import java.io.IOException;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanReader;

import msg.project.flightmanager.dto.CompanyDto;
import msg.project.flightmanager.exceptions.CompanyException;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.modelHelper.CreateAirportModel;
import msg.project.flightmanager.modelHelper.CreatePlaneModel;
import msg.project.flightmanager.modelHelper.CreateUserModel;
import msg.project.flightmanager.service.interfaces.IAirportService;
import msg.project.flightmanager.service.interfaces.ICSVImporterService;
import msg.project.flightmanager.service.interfaces.ICompanyService;
import msg.project.flightmanager.service.interfaces.IPlaneService;
import msg.project.flightmanager.service.interfaces.IUserService;
import msg.project.flightmanager.service.utils.CsvBeanManagementService;

@Service
public class CSVImporterService implements ICSVImporterService{
	@Autowired
	CsvBeanManagementService beanManagementService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IPlaneService planeService;
	@Autowired
	private IAirportService airportService;
	@Autowired
	private ICompanyService companyService;
	
	private static final Logger logger = LoggerFactory.getLogger(CSVExporterService.class);

	private static String TYPE = "text/csv";
	  
	@Override
	public boolean hasCSVFormat(MultipartFile file) {
		return TYPE.equals(file.getContentType());
	}
	
	@Override
	public void csvToEntity(Class<?> clazz, MultipartFile file) throws CompanyException {
		CsvBeanReader reader = this.beanManagementService.getCsvBeanReader(file);
			
			try {
				String[] header = reader.getHeader(true);
				
				if(clazz == User.class) {
					readForUser(reader, header);
				}
				if(clazz == Plane.class) {
					readForPlane(reader, header);
				}
				if(clazz == Airport.class) {
					readForAirport(reader, header);
				}
				if(clazz == Company.class) {
					readForCompany(reader, header);
				}
				
			} catch (IOException e) {
				logger.error("Couldn't read the header of file  " + file.getOriginalFilename(), e.getMessage());
				throw new FlightManagerException(
						HttpStatus.EXPECTATION_FAILED,
						MessageFormat.format("Couldn't read the header of file {0}", file.getOriginalFilename()));
				
			} catch (ValidatorException e) {
				logger.error("Error while creating import for: " + clazz.getSimpleName(), e.getMessage());
				throw new FlightManagerException(
						HttpStatus.EXPECTATION_FAILED,
						MessageFormat.format("Error while creating {0} from import",clazz.getSimpleName().toLowerCase()));
			}finally {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("Error closing the reader: ", e.getMessage());

					throw new FlightManagerException(
							HttpStatus.EXPECTATION_FAILED,
							"Error closing the reader");
				}
			}
	}
	
	private void readForUser(CsvBeanReader reader, String[] header) throws ValidatorException, IOException {
		CreateUserModel createUserModel;
		while((createUserModel = reader.read(CreateUserModel.class, header, this.beanManagementService.getUserProcessor())) != null) {
			this.userService.createUser(createUserModel);
		}
	}
	
	private void readForPlane(CsvBeanReader reader, String[] header) throws ValidatorException, IOException {
		CreatePlaneModel createPlaneModel;
		while((createPlaneModel = reader.read(CreatePlaneModel.class, header, this.beanManagementService.getPlaneProcessor())) != null) {
			this.planeService.createPlane(createPlaneModel);
		}
	}
	
	private void readForAirport(CsvBeanReader reader, String[] header) throws ValidatorException, IOException {
		CreateAirportModel createAirportModel;
		while((createAirportModel = reader.read(CreateAirportModel.class, header, this.beanManagementService.getAirportProcessor())) != null) {
			this.airportService.createAirport(createAirportModel);
		}
	}
	
	private void readForCompany(CsvBeanReader reader, String[] header) throws ValidatorException, IOException, CompanyException {
		CompanyDto companyDto;
		while((companyDto = reader.read(CompanyDto.class, header, this.beanManagementService.getCompanyProcessor())) != null) {
			this.companyService.addCompany(companyDto);
		}
	}
}