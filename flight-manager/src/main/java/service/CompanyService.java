package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dto.CompanyDto;
import repository.CompanyRepository;
import service.interfaces.ICompanyService;

@Service
public class CompanyService implements ICompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public void addCompany(CompanyDto companyDto) {

	}

	@Override
	public void updateCompany(CompanyDto companyDto) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dezactivateCompany(Long idCompany) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<CompanyDto> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompanyDto findByCompanyID(Long idCompany) {
		// TODO Auto-generated method stub
		return null;
	}

}
