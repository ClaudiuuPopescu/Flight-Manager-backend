package msg.project.flightmanager.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import msg.project.flightmanager.enums.PermissionEnum;
import msg.project.flightmanager.exceptions.RoleException;
import msg.project.flightmanager.exceptions.UserException;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.model.User;
import msg.project.flightmanager.service.interfaces.ICSVExporterService;
import msg.project.flightmanager.service.interfaces.IUserService;

@RestController
@RequestMapping("/api/csv-exporter")
public class CSVExporterController {
	
	public static final String EXPORT_USER = "/user";
	public static final String EXPORT_PLANE = "/plane";
	public static final String EXPORT_AIRPORT = "/airport";
	public static final String EXPORT_COMPANY = "/company";
	
	@Autowired
	private ICSVExporterService exporterService;
	@Autowired
	private IUserService userService;
	
	@GetMapping(EXPORT_USER)
	public ResponseEntity<String> exportUser(@RequestHeader(name = "Authorization") String token, HttpServletResponse response) throws IOException{
		try {
			this.userService.checkPermission(token, PermissionEnum.EXPORT_DATA);
			
			LocalDate timeStamp = LocalDate.now();
			String fileName = "users-export-" + timeStamp + ".csv";
			
			response.setContentType("text/csv");
			
			String headerKey = HttpHeaders.CONTENT_DISPOSITION;
			String haderValue = "attachment; filename=" + fileName;
			response.setHeader(headerKey, haderValue);
			
			PrintWriter writer = response.getWriter();
			
			this.exporterService.exportToCSV(User.class, writer);
			
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("Users exported successfully!");

		} catch (RoleException | UserException | IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping(EXPORT_PLANE)
	public ResponseEntity<String> exportPlane(@RequestHeader(name = "Authorization") String token, HttpServletResponse response) throws IOException{	
		try {
			this.userService.checkPermission(token, PermissionEnum.EXPORT_DATA);
			
			LocalDate timeStamp = LocalDate.now();
			String fileName = "planes-export-" + timeStamp + ".csv";
			
			response.setContentType("text/csv");
			
			String headerKey = HttpHeaders.CONTENT_DISPOSITION;
			String haderValue = "attachment; filename=" + fileName;
			response.setHeader(headerKey, haderValue);
			
			PrintWriter writer = response.getWriter();
			
			this.exporterService.exportToCSV(Plane.class, writer);
			
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("Planes exported successfully!");

		} catch (RoleException | UserException | IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping(EXPORT_AIRPORT)
	public ResponseEntity<String> exportAirport(@RequestHeader(name = "Authorization") String token, HttpServletResponse response) throws IOException{
		try {
			this.userService.checkPermission(token, PermissionEnum.EXPORT_DATA);
			
			LocalDate timeStamp = LocalDate.now();
			String fileName = "airports-export-" + timeStamp + ".csv";
			
			response.setContentType("text/csv");
			
			String headerKey = HttpHeaders.CONTENT_DISPOSITION;
			String haderValue = "attachment; filename=" + fileName;
			response.setHeader(headerKey, haderValue);
			
			PrintWriter writer = response.getWriter();
			
			this.exporterService.exportToCSV(Airport.class, writer);
			
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("Airports exported successfully!");

		} catch (RoleException | UserException | IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping(EXPORT_COMPANY)
	public ResponseEntity<String> exportCompany(@RequestHeader(name = "Authorization") String token, HttpServletResponse response) throws IOException{
		try {
			this.userService.checkPermission(token, PermissionEnum.EXPORT_DATA);
			
			LocalDate timeStamp = LocalDate.now();
			String fileName = "companies-export-" + timeStamp + ".csv";
			
			response.setContentType("text/csv");
			
			String headerKey = HttpHeaders.CONTENT_DISPOSITION;
			String haderValue = "attachment; filename=" + fileName;
			response.setHeader(headerKey, haderValue);
			
			PrintWriter writer = response.getWriter();
			
			this.exporterService.exportToCSV(Company.class, writer);
			
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("Companies exported successfully!");

		} catch (RoleException | UserException | IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
