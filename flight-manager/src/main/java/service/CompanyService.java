package service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import converter.CompanyConverter;
import dto.CompanyDto;
import exceptions.CompanyException;
import exceptions.ErrorCode;
import exceptions.ValidatorException;
import msg.project.flightmanager.model.Company;
import repository.CompanyRepository;
import service.interfaces.ICompanyService;
import validator.CompanyValidator;

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
			if (findByCompanyName(companyDTO.getName()) == null) {
				this.companyValidator.validateCompany(companyDTO);

				Company companyToSave = companyConverter.convertToEntity(companyDTO);
				this.companyRepository.save(companyToSave);
			} else
				throw new CompanyException("A company with this name does exist!", ErrorCode.EXISTING_NAME);
		} else
			throw new CompanyException("A company shoul have a name!", ErrorCode.EMPTY_FIELD);

	}

	@Override
	public void updateCompany(CompanyDto companyDTO) throws CompanyException, ValidatorException {

		if (companyDTO.getName() != null) {
			Company oldCompany = findByCompanyName(companyDTO.getName());
			if (oldCompany != null) {
				this.companyValidator.validateCompany(companyDTO);

				Company companyToUpdate = companyConverter.convertToEntity(companyDTO);

				companyToUpdate.setIdCompany(oldCompany.getIdCompany());
				companyToUpdate.setActiv(oldCompany.isActiv());

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
	public void dezactivateCompany(String companyName) throws CompanyException {

		Company companyToDezactivate = findByCompanyName(companyName);

		if (companyToDezactivate != null) {

			companyToDezactivate.setActiv(false);
			this.companyRepository.save(companyToDezactivate);

			// sterg avioane
			companyToDezactivate.getPlanes().stream()
					.forEach(plane -> this.planeService.removePlane(plane.getTailNumber()));

			// pun la angajati pe null
			companyToDezactivate.getEmployees().stream().forEach(employee -> employee.setCompany(null));

			// scot colaborarea cu aeroportul
			companyToDezactivate.getAirportsCollab().stream()
					.forEach(airport -> airport.removeCollab(companyToDezactivate));

		} else
			throw new CompanyException("A company with this name does not exist",
					ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);

	}

	@Override
	public List<CompanyDto> findAll() {
		return this.companyRepository.findAll().stream().map(companyConverter::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public Company findByCompanyName(String companyName) throws CompanyException {

		Optional<Company> companyOptional = this.companyRepository.findCompanyByName(companyName);
		if (companyOptional.isEmpty()) {
			throw new CompanyException("A company with this name does not exist!",
					ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);
		}
		return companyOptional.get();
	}

}
