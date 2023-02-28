package service.interfaces;

import java.util.List;

import dto.CompanyDto;
import msg.project.flightmanager.model.Company;

public interface ICompanyService {

	void addCompany(CompanyDto companyDTO) throws Exception;

	void updateCompany(CompanyDto companyDTO) throws Exception;

	void dezactivateCompany(String companyName);

	List<CompanyDto> findAll();

	Company findByCompanyName(String companyName);

}
