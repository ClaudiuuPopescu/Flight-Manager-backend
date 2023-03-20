package msg.project.flightmanager.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import msg.project.flightmanager.converter.CompanyConverter;
import msg.project.flightmanager.dto.CompanyDto;
import msg.project.flightmanager.exceptions.CompanyException;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.repository.CompanyRepository;
import msg.project.flightmanager.service.interfaces.ICompanyService;
import msg.project.flightmanager.validator.CompanyValidator;

@Service
public class CompanyService implements ICompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CompanyConverter companyConverter;

	@Autowired
	private CompanyValidator companyValidator;

	@Autowired
	private PlaneService planeService;

	@Override
	public void addCompany(CompanyDto companyDTO) throws CompanyException, ValidatorException {

		if (companyDTO.getName() != null) {
			if (validateCompanyByNamePhoneNumberAndEmail(companyDTO)) {

				this.companyValidator.validateCompany(companyDTO);
				Company companyToSave = companyConverter.convertToEntity(companyDTO);
				this.companyRepository.save(companyToSave);

			}
		} else
			throw new CompanyException("A company should have a name!", ErrorCode.EMPTY_FIELD);

	}

	@Override
	public void updateCompany(CompanyDto companyDTO) throws CompanyException, ValidatorException {

		if (validateCompanyByNamePhoneNumberAndEmail(companyDTO)) {
			Company oldCompany = findByCompanyName(companyDTO.getName()).get();
			if (oldCompany != null) {
				this.companyValidator.validateCompany(companyDTO);

				Company companyToUpdate = companyConverter.convertToEntity(companyDTO);

				companyToUpdate.setIdCompany(oldCompany.getIdCompany());

				if (oldCompany.getAddress() != null) {
					companyToUpdate.setAddress(oldCompany.getAddress());
				}

				companyToUpdate.setEmployees(oldCompany.getEmployees());
				companyToUpdate.setPlanes(oldCompany.getPlanes());

				this.companyRepository.save(companyToUpdate);
			} else
				throw new CompanyException("A company with this name does exist!", ErrorCode.EXISTING_NAME);
		} else
			throw new CompanyException("A company should have a name!", ErrorCode.EMPTY_FIELD);
	}

	// aici anulez zborul si sterg avioanele
	@Override
	public void deleteCompany(String companyName) throws CompanyException {

		Optional<Company> companyToDezactivate = findByCompanyName(companyName);

		if (companyToDezactivate.isPresent()) {

			this.companyRepository.delete(companyToDezactivate.get());

//			// sterg avioane
//			companyToDezactivate.getPlanes().stream()
//					.forEach(plane -> this.planeService.removePlane(plane.getTailNumber()));
//
//			// pun la angajati pe null
//			companyToDezactivate.getEmployees().stream().forEach(employee -> employee.setCompany(null));
//
//			// scot colaborarea cu aeroportul
//			companyToDezactivate.getAirportsCollab().stream()
//					.forEach(airport -> airport.removeCollab(companyToDezactivate));

		} else
			throw new CompanyException("A company with this name does not exist",
					ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);

	}

	@Override
	public List<CompanyDto> findAll() {
		return this.companyRepository.findAll().stream().map(company -> this.companyConverter.convertToDTO(company))
				.collect(Collectors.toList());
	}

	private Optional<Company> findByCompanyName(String companyName) throws CompanyException {

		Optional<Company> companyOptional = this.companyRepository.findCompanyByName(companyName);
		return companyOptional;
	}

	private Optional<Company> findByPhoneNumber(String phoneNumber) throws CompanyException {

		Optional<Company> companyOptional = this.companyRepository.findCompanyByPhoneNumber(phoneNumber);
		return companyOptional;
	}

	private Optional<Company> findByEmail(String email) throws CompanyException {

		Optional<Company> companyOptional = this.companyRepository.findCompanyByEmail(email);
		return companyOptional;
	}

	private boolean validateCompanyByNamePhoneNumberAndEmail(CompanyDto companyDto) throws CompanyException {

		if (findByCompanyName(companyDto.getName()).isPresent())
			throw new CompanyException("A company with this name does exist!",
					ErrorCode.EXISTING_ATTRIBUTE);

		if (findByEmail(companyDto.getEmail()).isPresent())
			throw new CompanyException("A company with this email does exist!",
					ErrorCode.EXISTING_ATTRIBUTE);

		if (findByPhoneNumber(companyDto.getPhoneNumber()).isPresent())
			throw new CompanyException("A company with this phoneNumber does exist!",
					ErrorCode.EXISTING_ATTRIBUTE);

		return true;
	}

}
