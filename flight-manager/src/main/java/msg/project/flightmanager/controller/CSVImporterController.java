package msg.project.flightmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import msg.project.flightmanager.exceptions.CompanyException;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.service.CSVImporterService;

@RestController
@RequestMapping("/api/csv-importer")
public class CSVImporterController {

	public static final String IMPORT_USER = "/user";
	public static final String IMPORT_PLANE = "/plane";
	public static final String IMPORT_AIRPORT = "/airport";
	public static final String IMPORT_COMPANY = "/company";
	
	@Autowired
	private CSVImporterService csvImporterService;
//	@Autowired
//	private IUserService userService;
	
	@PostMapping(IMPORT_USER)
	public ResponseEntity<String> importUser(@RequestParam("file") MultipartFile file) throws CompanyException {
//		try {
//			this.userService.checkPermission(token, PermissionEnum.IMPORT_DATA);
		String message = "";
		
		if (this.csvImporterService.hasCSVFormat(file)) {
				this.csvImporterService.csvToEntity(User.class, file);
				
				message = "File imported successfully: " + file.getOriginalFilename();
				return ResponseEntity
						.status(HttpStatus.OK)
						.body(message);		
		}
		
		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		
//		}catch (RoleException | UserException e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}
	}
	
	@PostMapping(IMPORT_PLANE)
	public ResponseEntity<String> importPlane(@RequestParam("file") MultipartFile file) throws CompanyException {
//		try {
//			this.userService.checkPermission(token, PermissionEnum.IMPORT_DATA);
		String message = "";
		
		if (this.csvImporterService.hasCSVFormat(file)) {
				this.csvImporterService.csvToEntity(Plane.class, file);
				
				message = "File imported successfully: " + file.getOriginalFilename();
				return ResponseEntity
						.status(HttpStatus.OK)
						.body(message);
		}
		
		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		
//		}catch (RoleException | UserException e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}
	}
	
	@PostMapping(IMPORT_AIRPORT)
	public ResponseEntity<String> importAirport(@RequestParam("file") MultipartFile file) throws CompanyException {
//		try {
//			this.userService.checkPermission(token, PermissionEnum.IMPORT_DATA);
		String message = "";
		
		if (this.csvImporterService.hasCSVFormat(file)) {
				this.csvImporterService.csvToEntity(Airport.class, file);
				
				message = "File imported successfully: " + file.getOriginalFilename();
				return ResponseEntity
						.status(HttpStatus.OK)
						.body(message);		
		}
		
		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		
//		}catch (RoleException | UserException e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}
	}
	
	@PostMapping(IMPORT_COMPANY)
	public ResponseEntity<String> importCompany(@RequestParam("file") MultipartFile file) throws CompanyException {
//		try {
//			this.userService.checkPermission(token, PermissionEnum.IMPORT_DATA);
		String message = "";
		
		if (this.csvImporterService.hasCSVFormat(file)) {
				this.csvImporterService.csvToEntity(Company.class, file);
				
				message = "File imported successfully: " + file.getOriginalFilename();
				return ResponseEntity
						.status(HttpStatus.OK)
						.body(message);		
		}
		
		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		
//		}catch (RoleException | UserException e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}
	}
}
