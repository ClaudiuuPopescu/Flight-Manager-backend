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

import msg.project.flightmanager.dto.FlightTemplateDto;
import msg.project.flightmanager.enums.PermissionEnum;
import msg.project.flightmanager.exceptions.ErrorCodeException;
import msg.project.flightmanager.exceptions.FlightTemplateException;
import msg.project.flightmanager.exceptions.RoleException;
import msg.project.flightmanager.exceptions.UserException;
import msg.project.flightmanager.service.interfaces.IFlightTemplateService;
import msg.project.flightmanager.service.interfaces.IUserService;

@RestController
public class FlightTemplateController {

	private static final String FLIGHT_TEMPLATE_PATH = "/templates";
	private static final String FLIGHT_TEMPLATE_PATH_UPDATE = "/templates/update";
	private static final String FLIGHT_TEMPLATE_PATH_DELETE = "/templates/{flightTemplateId}";
	
	private IUserService userService;
	private IFlightTemplateService flightTemplateService;
	
	@Autowired
	public FlightTemplateController(IUserService userService, IFlightTemplateService flightTemplateService) {
		this.flightTemplateService = flightTemplateService;
		this.userService = userService;
	}
	
	@GetMapping(FLIGHT_TEMPLATE_PATH)
	public ResponseEntity<?> findAll(@RequestHeader(name = "Authorization") String token) {
		try {
			this.userService.checkPermission(token, PermissionEnum.FLIGHT_TEMPLATE_MANAGEMENT);
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
					.body(this.flightTemplateService.getAllFlightTemplates());

		} catch (ErrorCodeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
					.body(e);
		}
	}
	
	@PostMapping(FLIGHT_TEMPLATE_PATH)
	public ResponseEntity<?> save(@RequestBody FlightTemplateDto flightTemplateDto,
			@RequestHeader(name = "Authorization") String token) throws FlightTemplateException{

		try {
			this.userService.checkPermission(token, PermissionEnum.FLIGHT_TEMPLATE_MANAGEMENT);
			this.flightTemplateService.addFlightTemplate(flightTemplateDto);
			return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(null);
		} catch (RoleException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		} catch (UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@PostMapping(FLIGHT_TEMPLATE_PATH_UPDATE)
	public ResponseEntity<?> update(@RequestBody FlightTemplateDto flightTemplateDto,
			@RequestHeader(name = "Authorization") String token) throws FlightTemplateException{

		try {
			this.userService.checkPermission(token, PermissionEnum.FLIGHT_TEMPLATE_MANAGEMENT);
			this.flightTemplateService.updateFlightTemplate(flightTemplateDto);
			return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(null);
		} catch (RoleException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		} catch (UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@DeleteMapping(FLIGHT_TEMPLATE_PATH_DELETE)
	public ResponseEntity<?> delete(@PathVariable Long flightTemplateId,
			@RequestHeader(name = "Authorization") String token) throws FlightTemplateException{

		try {
			this.userService.checkPermission(token, PermissionEnum.COMPANY_MANAGEMENT);
			this.flightTemplateService.deleteFlightTemplate(flightTemplateId);
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(null);
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).contentType(MediaType.APPLICATION_JSON).body(e);

		}

	}
	
	
}
