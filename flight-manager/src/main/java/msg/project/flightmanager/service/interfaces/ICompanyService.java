package msg.project.flightmanager.service.interfaces;

import java.util.List;

import msg.project.flightmanager.dto.CompanyDto;
import msg.project.flightmanager.exceptions.CompanyException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Company;

public interface ICompanyService {

	boolean addCompany(CompanyDto companyDTO) throws CompanyException, ValidatorException;

	boolean updateCompany(CompanyDto companyDTO) throws CompanyException, ValidatorException;

	boolean deleteCompany(String companyName) throws CompanyException;

	List<CompanyDto> findAll();

}
