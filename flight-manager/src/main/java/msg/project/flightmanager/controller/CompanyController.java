package msg.project.flightmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

<<<<<<<HEAD:flight-manager/src/main/java/controller/CompanyController.java
import dto.CompanyDto;
import enums.PermissionEnum;
import exceptions.CompanyException;
import exceptions.ErrorCodeException;
import exceptions.RoleException;
import exceptions.UserException;
import exceptions.ValidatorException;
import service.interfaces.ICompanyService;
import service.interfaces.IUserService;=======
import msg.project.flightmanager.service.interfaces.ICompanyService;
import msg.project.flightmanager.service.interfaces.IUserService;

@RestController
public class CompanyController {

	private static final String COMPANY_PATH = "/companies";
	private static final String COMPANY_PATH_UPDATE = "/companies/update";
	private static final String COMPANY_PATH_DELETE = "/companies/{companyName}";

	private final ICompanyService companyService;

	private final IUserService userService;

	@Autowired
	public CompanyController(ICompanyService companyService, IUserService userService) {
		this.companyService = companyService;
		this.userService = userService;
	}

	@GetMapping(COMPANY_PATH)
	public ResponseEntity<?> findAll(@RequestHeader(name = "Authorization") String token) {
		try {
			this.userService.checkPermission(token, PermissionEnum.COMPANY_MANAGEMENT);
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
					.body(this.companyService.findAll());

		} catch (ErrorCodeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
					.body(e);
		}
	}

	@PostMapping(COMPANY_PATH)
	public ResponseEntity<?> save(@RequestBody CompanyDto companyDto,
			@RequestHeader(name = "Authorization") String token) throws CompanyException, ValidatorException {

		try {
			this.userService.checkPermission(token, PermissionEnum.COMPANY_MANAGEMENT);
			this.companyService.addCompany(companyDto);
			return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(null);
		} catch (RoleException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		} catch (UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}

	@PostMapping(COMPANY_PATH_UPDATE)
	public ResponseEntity<?> update(@RequestBody CompanyDto companyDto,
			@RequestHeader(name = "Authorization") String token) throws CompanyException, ValidatorException {

		try {
			this.userService.checkPermission(token, PermissionEnum.COMPANY_MANAGEMENT);
			this.companyService.updateCompany(companyDto);
			return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(null);
		} catch (RoleException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		} catch (UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}

	@DeleteMapping(COMPANY_PATH_DELETE)
	public ResponseEntity<?> delete(@PathVariable String companyName,
			@RequestHeader(name = "Authorization") String token) throws CompanyException {

		try {
			this.userService.checkPermission(token, PermissionEnum.COMPANY_MANAGEMENT);
			this.companyService.deleteCompany(companyName);
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(null);
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).contentType(MediaType.APPLICATION_JSON).body(e);

		}

	}

}
