package msg.project.flightmanager.servicies;

import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanReader;

import msg.project.flightmanager.exceptions.CompanyException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.modelHelper.CreateUserModel;
import msg.project.flightmanager.repository.UserRepository;
import msg.project.flightmanager.service.CSVImporterService;
import msg.project.flightmanager.service.UserService;
import msg.project.flightmanager.service.utils.CsvBeanManagementService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CSVImporterServiceTest {
	@InjectMocks
	private CSVImporterService service;
	@Mock
	CsvBeanManagementService beanManagementService;
	@Mock
	private UserService userService;
	@Mock
	private UserRepository userRepository;
	
	public final String[] csvHeaderUser = {"Password","FirstName","LastName","Email","PhoneNumber","BirthDate","RoleTitle",
			"Country","City","Street","StreetNumber","Apartment"};

	@Test
	void csvToEntity_returnsVoid_whenImportedFileCreatedEntities() throws IOException, ValidatorException, CompanyException {
	    File csvFile = File.createTempFile("test", ".csv");
	    Writer writer = new FileWriter(csvFile);
	    writer.write("Password, FirstName, LastName, Email, PhoneNumberR, BirthDate, RoleTitle"
	    		+ "Country, City, Street, StreetNumber, Apartment\n");
	    writer.write("password123, randomFn, randomLn, email@airline.com, 0753247035, 2000/05/13, crew, "
	    		+ "Romania, Targu Mures, Insulei, 1, null");
	    writer.close();
	    
	    MultipartFile file = Mockito.mock(MultipartFile.class);
	    
		CsvBeanReader reader = Mockito.mock(CsvBeanReader.class);
		
		Mockito.when(this.beanManagementService.getCsvBeanReader(file)).thenReturn(reader);
		Mockito.when(reader.getHeader(true)).thenReturn(this.csvHeaderUser);

		CreateUserModel createUserModel = Mockito.mock(CreateUserModel.class);
		
		Mockito.when(reader.read(CreateUserModel.class, this.csvHeaderUser, this.beanManagementService.getUserProcessor())).thenReturn(createUserModel).thenReturn(null);
		
		this.service.csvToEntity(User.class, file);
		
		verify(this.userService).createUser(createUserModel);
	}
}


