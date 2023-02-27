package converter;

import dto.CompanyDTO;
import model.Company;

public class CompanyConverter implements IConverter<Company, CompanyDTO>{

	@Override
	public Company convertToEntity(CompanyDTO companyDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompanyDTO convertToDTO(Company company) {
		
		return CompanyDTO.builder()
		.idCompany(company.getIdCompany())
		.build();
	}

	

}
