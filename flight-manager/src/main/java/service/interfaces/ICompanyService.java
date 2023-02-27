package service.interfaces;

import java.util.List;

import dto.CompanyDTO;

public interface ICompanyService {

	void addCompany(CompanyDTO companyDTO);
	
	void updateCompany(CompanyDTO companyDTO);
	
	void dezactivateCompany(Long idCompany);
	
	List<CompanyDTO> findAll();
	
	CompanyDTO findByCompanyID(Long idCompany);
	
	
	
	
}
