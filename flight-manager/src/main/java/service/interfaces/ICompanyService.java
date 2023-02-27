package service.interfaces;

import java.util.List;

import dto.CompanyDto;

public interface ICompanyService {

	void addCompany(CompanyDto companyDto);

	void updateCompany(CompanyDto companyDto);

	void dezactivateCompany(Long idCompany);

	List<CompanyDto> findAll();

	CompanyDto findByCompanyID(Long idCompany);

}
