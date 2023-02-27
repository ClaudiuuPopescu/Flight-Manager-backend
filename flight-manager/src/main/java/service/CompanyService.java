package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dto.CompanyDTO;
import lombok.Getter;
import lombok.Setter;
import repository.CompanyRepository;
import service.interfaces.ICompanyService;


@Service
@Getter
@Setter
public class CompanyService implements ICompanyService{
	
	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public void addCompany(CompanyDTO companyDTO) {
		
		
		
	}

	@Override
	public void updateCompany(CompanyDTO companyDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dezactivateCompany(Long idCompany) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<CompanyDTO> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompanyDTO findByCompanyID(Long idCompany) {
		// TODO Auto-generated method stub
		return null;
	}

}
