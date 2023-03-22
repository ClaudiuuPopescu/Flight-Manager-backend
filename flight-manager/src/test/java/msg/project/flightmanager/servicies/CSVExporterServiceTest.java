package msg.project.flightmanager.servicies;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import msg.project.flightmanager.repository.AirportRepository;
import msg.project.flightmanager.repository.CompanyRepository;
import msg.project.flightmanager.repository.PlaneRepository;
import msg.project.flightmanager.repository.UserRepository;
import msg.project.flightmanager.service.CSVExporterService;

@ExtendWith(MockitoExtension.class)
public class CSVExporterServiceTest {
	@InjectMocks
	private CSVExporterService service;
	@Mock
	private UserRepository userRepository;
	@Mock
	private PlaneRepository planeRepository;
	@Mock
	private AirportRepository airportRepository;
	@Mock
	private CompanyRepository companyRepository;


}
