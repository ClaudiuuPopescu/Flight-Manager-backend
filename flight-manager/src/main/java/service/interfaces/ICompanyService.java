package service.interfaces;

import java.util.List;

import dto.CompanyDto;
import exceptions.CompanyException;
import exceptions.ValidatorException;
import msg.project.flightmanager.model.Company;

public interface ICompanyService {

	void addCompany(CompanyDto companyDTO) throws CompanyException, ValidatorException;

	void updateCompany(CompanyDto companyDTO) throws CompanyException, ValidatorException;

	void deleteCompany(String companyName) throws CompanyException;

	List<CompanyDto> findAll();

	Company findByCompanyName(String companyName) throws CompanyException;

}
