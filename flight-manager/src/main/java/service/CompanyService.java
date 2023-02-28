package service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import converter.CompanyConverter;
import dto.CompanyDto;
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

	@Override
	public void addCompany(CompanyDto companyDTO) throws Exception {

		if (companyDTO.getName() != null) {
			if (findByCompanyName(companyDTO.getName()) == null) {
				this.companyValidator.validateCompany(companyDTO);

				Company companyToSave = companyConverter.convertToEntity(companyDTO);
				this.companyRepository.save(companyToSave);
			} else
				throw new IllegalArgumentException("A company with this name does exist!");
		} else
			throw new IllegalArgumentException("A company shoul have a name!");

	}

	@Override
	public void updateCompany(CompanyDto companyDTO) throws Exception {

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
				throw new IllegalArgumentException("A company with this name does exist!");
		} else
			throw new IllegalArgumentException("A company shoul have a name!");
	}

	@Override
	public void dezactivateCompany(String companyName) {

		Company companyToDezactivate = findByCompanyName(companyName);
		companyToDezactivate.setActiv(false);
		this.companyRepository.save(companyToDezactivate);
	}

	@Override
	public List<CompanyDto> findAll() {
		return this.companyRepository.findAll().stream().map(companyConverter::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public Company findByCompanyName(String companyName) {

		Optional<Company> companyOptional = this.companyRepository.findCompanyByName(companyName);
		if (companyOptional.isEmpty()) {
			throw new IllegalArgumentException("A company with this name does not exist!");
		}
		return companyOptional.get();
	}

}
