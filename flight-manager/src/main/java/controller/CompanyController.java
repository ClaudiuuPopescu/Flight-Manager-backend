package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import service.interfaces.ICompanyService;
import service.interfaces.IUserService;

@RestController
public class CompanyController {
	private final ICompanyService companyService;
	
	private final IUserService userService;
	
    @Autowired
    public CompanyController(ICompanyService companyService, IUserService userService) {
        this.companyService = companyService;
		this.userService = userService;
    }
    
    

}
