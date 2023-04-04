package msg.project.flightmanager.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import msg.project.flightmanager.converter.AddressConverter;
import msg.project.flightmanager.converter.CompanyConverter;
import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.dto.CompanyDto;
import msg.project.flightmanager.exceptions.CompanyException;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Address;
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
	private AddressConverter addressConverter;

	@Override //TODO save the address before saving company
	public void addCompany(CompanyDto companyDTO) throws CompanyException, ValidatorException {

		if (companyDTO.getName() != null) {
			if (validateCompanyByNamePhoneNumberAddressAndEmail(companyDTO)) {

				this.companyValidator.validateCompany(companyDTO);
				Company companyToSave = this.companyConverter.convertToEntity(companyDTO);
				this.companyRepository.save(companyToSave);
			}
		} else
			throw new CompanyException("A company should have a name!", ErrorCode.EMPTY_FIELD);

	}

	@Override
	public void updateCompany(CompanyDto companyDTO) throws CompanyException, ValidatorException {

		if (companyDTO.getName() != null) {
			Optional<Company> companyOptional = findByCompanyName(companyDTO.getName());
			if (companyOptional.isPresent()) {

				Company oldCompany = companyOptional.get();
				this.companyValidator.validateCompany(companyDTO);

				Company companyToUpdate = this.companyConverter.convertToEntity(companyDTO);

				Optional<Company> companyByPhone = findByPhoneNumber(companyDTO.getPhoneNumber());
				if (companyByPhone.isPresent() && !companyByPhone.get().equals(oldCompany))
					throw new CompanyException("A company with this phoneNumber does exist!",
							ErrorCode.EXISTING_ATTRIBUTE);

				Optional<Company> companyByEmail = findByEmail(companyDTO.getEmail());
				if (companyByEmail.isPresent() && !companyByEmail.get().equals(oldCompany))
					throw new CompanyException("A company with this email does exist!", ErrorCode.EXISTING_ATTRIBUTE);

				Address address = companyToUpdate.getAddress();
				Optional<Company> companyByAddress = findByAddress(companyDTO.getAddress());

				if (companyByAddress.isPresent() && !address.equals(oldCompany.getAddress()))
					throw new CompanyException("A company with this address already exists!",
							ErrorCode.EXISTING_ATTRIBUTE);

				companyToUpdate.setIdCompany(oldCompany.getIdCompany());
				companyToUpdate.setEmployees(oldCompany.getEmployees());
				companyToUpdate.setPlanes(oldCompany.getPlanes());

				this.companyRepository.save(companyToUpdate);
			} else
				throw new CompanyException("A company with this name does not exist!",
						ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);
		} else
			throw new CompanyException("A company should have a name!", ErrorCode.EMPTY_FIELD);

	}

	@Override
	public void deleteCompany(String companyName) throws CompanyException {

		Optional<Company> companyToDezactivate = findByCompanyName(companyName);

		if (companyToDezactivate.isPresent()) {

			this.companyRepository.delete(companyToDezactivate.get());
		} else
			throw new CompanyException("A company with this name does not exist",
					ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);
	}

	@Override
	public List<CompanyDto> findAll() {
		return this.companyRepository.findAll().stream().map(company -> this.companyConverter.convertToDTO(company))
				.collect(Collectors.toList());
	}

	private Optional<Company> findByCompanyName(String companyName) {

		Optional<Company> companyOptional = this.companyRepository.findCompanyByName(companyName);
		return companyOptional;
	}

	private Optional<Company> findByPhoneNumber(String phoneNumber) {

		Optional<Company> companyOptional = this.companyRepository.findCompanyByPhoneNumber(phoneNumber);
		return companyOptional;
	}

	private Optional<Company> findByEmail(String email) {

		Optional<Company> companyOptional = this.companyRepository.findCompanyByEmail(email);
		return companyOptional;
	}

	private Optional<Company> findByAddress(AddressDto addressDto) throws CompanyException {

		Address address = this.addressConverter.convertToEntity(addressDto);
		Optional<Company> companyOptional = this.companyRepository.findCompanyByAddress(address);
		return companyOptional;
	}

	private boolean validateCompanyByNamePhoneNumberAddressAndEmail(CompanyDto companyDto) throws CompanyException {

		if (findByCompanyName(companyDto.getName()).isPresent())
			throw new CompanyException("A company with this name does exist!", ErrorCode.EXISTING_ATTRIBUTE);

		if (findByEmail(companyDto.getEmail()).isPresent())
			throw new CompanyException("A company with this email does exist!", ErrorCode.EXISTING_ATTRIBUTE);

		if (findByPhoneNumber(companyDto.getPhoneNumber()).isPresent())
			throw new CompanyException("A company with this phoneNumber does exist!", ErrorCode.EXISTING_ATTRIBUTE);

		if (findByAddress(companyDto.getAddress()).isPresent())
			throw new CompanyException("A company with this address does exist!", ErrorCode.EXISTING_ATTRIBUTE);

		return true;
	}

}
