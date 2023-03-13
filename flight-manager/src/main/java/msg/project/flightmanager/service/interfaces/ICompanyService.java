package msg.project.flightmanager.service.interfaces;

import java.util.List;

import msg.project.flightmanager.dto.CompanyDto;
import msg.project.flightmanager.exceptions.CompanyException;
import msg.project.flightmanager.model.Company;

public interface ICompanyService {

	void addCompany(CompanyDto companyDTO) throws Exception;

	void updateCompany(CompanyDto companyDTO) throws Exception;

	void dezactivateCompany(String companyName) throws CompanyException;

	List<CompanyDto> findAll();

	Company findByCompanyName(String companyName) throws CompanyException;

}
