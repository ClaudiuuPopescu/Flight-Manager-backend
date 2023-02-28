package converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dto.CompanyDto;
import msg.project.flightmanager.model.Company;

@Component
public class CompanyConverter implements IConverter<Company, CompanyDto> {

	@Autowired
	private AddressConverter addressConverter;

	@Override
	public Company convertToEntity(CompanyDto companyDTO) {
		return Company.builder()
				.name(companyDTO.getName())
				.phoneNumber(companyDTO.getPhoneNumber())
				.email(companyDTO.getEmail())
				.foundedIn(companyDTO.getFoundedIn())
				.address(addressConverter.convertToEntity(companyDTO.getAddress()))
				.build();
	}

	@Override
	public CompanyDto convertToDTO(Company company) {

		return CompanyDto.builder()
				.name(company.getName())
				.phoneNumber(company.getPhoneNumber())
				.email(company.getEmail())
				.foundedIn(company.getFoundedIn())
				.address(addressConverter.convertToDTO(company.getAddress()))
				.build();
	}

}
