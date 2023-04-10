package msg.project.flightmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import msg.project.flightmanager.dto.ReportDto;
import msg.project.flightmanager.enums.PermissionEnum;
import msg.project.flightmanager.exceptions.RoleException;
import msg.project.flightmanager.exceptions.UserException;
import msg.project.flightmanager.service.TokenService;
import msg.project.flightmanager.service.interfaces.IReportService;
import msg.project.flightmanager.service.interfaces.IUserService;

@RestController
@RequestMapping("/api/report")
public class ReportController {

	public static final String GENERATE_REPORT = "/generate";
	public static final String DELETE_REPORT = "/delete/{reportCode}";
	
	@Autowired
	private IReportService reportService;
	@Autowired
	private IUserService userService;
	@Autowired
	private TokenService tokenService;
	
	@PostMapping(GENERATE_REPORT)
	public ResponseEntity<String> generateReport(@RequestHeader(name = "Authorization") String token, @RequestBody ReportDto reportDto){
		try {
			this.userService.checkPermission(token, PermissionEnum.GENERATE_REPORT);	
			
			String currentUsername = this.tokenService.getCurrentUserUsername(token);
			
			reportDto.setReporteByUsername(currentUsername);
			this.reportService.generateReport(reportDto);
			
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body("Report generated successfully!");
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@DeleteMapping(DELETE_REPORT)
	public ResponseEntity<String> deleteReport(@RequestHeader(name = "Authorization") String token, @PathVariable("reportCode") String reportCode){
		try {
			this.userService.checkPermission(token, PermissionEnum.GENERATE_REPORT);
			
			this.reportService.deleteReport(reportCode);
			
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("Report deleted successfully!");
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
