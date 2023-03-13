package msg.project.flightmanager.service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import msg.project.flightmanager.converter.PlaneConverter;
import msg.project.flightmanager.dto.PlaneDto;
import msg.project.flightmanager.exceptions.CompanyException;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.modelHelper.CreatePlaneModel;
import msg.project.flightmanager.modelHelper.EditLastRevisionPlaneModel;
import msg.project.flightmanager.repository.CompanyRepository;
import msg.project.flightmanager.repository.PlaneRepository;
import msg.project.flightmanager.service.interfaces.IPlaneService;
import msg.project.flightmanager.validator.PlaneValidator;

@Service
public class PlaneService implements IPlaneService {
	@Autowired
	private PlaneRepository planeRepository;
	@Autowired
	private PlaneValidator planeValidator;
	@Autowired
	private PlaneConverter planeConverter;
	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public List<PlaneDto> getAll() {
		List<Plane> planes = StreamSupport.stream(this.planeRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());

		if (planes.isEmpty()) {
			throw new FlightManagerException(HttpStatus.NO_CONTENT, "No planes found");
		}

		List<PlaneDto> planeDto = planes.stream().map(this.planeConverter::convertToDTO).collect(Collectors.toList());
		return planeDto;
	}

	@Override
	public boolean createPlane(CreatePlaneModel createPlaneModel) throws ValidatorException {

		// TODO verficare rol current user
		this.planeValidator.validateCreatePlaneModel(createPlaneModel);

		Plane plane = this.planeConverter.createModelToEntity(createPlaneModel);

		this.planeRepository.save(plane);
		return true;
	}

	@Override
	public boolean editLastRevisionPlane(EditLastRevisionPlaneModel editLastRevisionPlaneModel) {
		// TODO verificare rol current user
		// nu vad ce field poate fi modificat in afara de lastRevision
		Plane plane = this.planeRepository.findByTailNumber(editLastRevisionPlaneModel.getTailNumber())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND, MessageFormat
						.format("Plane with tail number [{0}] not found", editLastRevisionPlaneModel.getTailNumber())));

		this.planeValidator.valiateNewRevision(plane.getLastRevision(), editLastRevisionPlaneModel.getNewRevision());

		this.planeRepository.save(plane);
		return true;
	}

	@Override
	public boolean removePlane(int tailNumber) {
		// TODO verificare rol current user

		Plane plane = this.planeRepository.findByTailNumber(tailNumber)
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Plane with tail number [{0}] not found", tailNumber)));

		// TODO set field false in flight template.
		// TODO delete flight

		this.planeRepository.delete(plane);
		return true;
	}

	@Override
	public boolean movePlaneToAnotherCompany(int tailNumber, String to_companyName) throws CompanyException, FlightManagerException {
		// TODO verificare rol current user

		Plane plane = this.planeRepository.findByTailNumber(tailNumber)
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Plane with tail number [{0}] not found", tailNumber)));

		Company to_company = findByCompanyName(to_companyName);

		if (plane.getCompany().equals(to_company)) {
			throw new FlightManagerException(HttpStatus.EXPECTATION_FAILED,
					MessageFormat.format("Company [{0}] already has this plane", to_company.getName()));
		}

		plane.setCompany(to_company);
		this.planeRepository.save(plane);
		return true;
	}
	
	private Company findByCompanyName(String companyName) throws CompanyException {

		Optional<Company> companyOptional = this.companyRepository.findCompanyByName(companyName);
		if (companyOptional.isEmpty()) {
			throw new CompanyException("A company with this name does not exist!",
					ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);
		}
		return companyOptional.get();
	}
}
