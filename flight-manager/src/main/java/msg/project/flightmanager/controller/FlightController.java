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

import msg.project.flightmanager.dto.FlightDto;
import msg.project.flightmanager.enums.PermissionEnum;
import msg.project.flightmanager.exceptions.AirportException;
import msg.project.flightmanager.exceptions.ErrorCodeException;
import msg.project.flightmanager.exceptions.FlightException;
import msg.project.flightmanager.exceptions.PlaneException;
import msg.project.flightmanager.exceptions.RoleException;
import msg.project.flightmanager.exceptions.UserException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.service.interfaces.IFlightService;
import msg.project.flightmanager.service.interfaces.IUserService;



@RestController
public class FlightController {

	private static final String FLIGHT_PATH = "/flights";
	private static final String FLIGHT_PATH_UPDATE = "/flights/update";
	private static final String FLIGHT_PATH_DELETE = "/flights/{flightID}";
	private static final String FLIGHT_PATH_HISTORY = "/flights/history";
	
	private final IUserService userService;
	private final IFlightService flightService;
	
	@Autowired
	public FlightController(IUserService userService, IFlightService flightService) {
		this.flightService = flightService;
		this.userService = userService;
	}
	
	@GetMapping(FLIGHT_PATH)
	public ResponseEntity<?> findAll(@RequestHeader(name = "Authorization") String token) {
		try {
			this.userService.checkPermission(token, PermissionEnum.PERSONAL_DATA_MANAGEMENT);
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
					.body(this.flightService.getAllActiv());

		} catch (ErrorCodeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
					.body(e);
		}
	}
	
	@GetMapping(FLIGHT_PATH_HISTORY)
	public ResponseEntity<?> flightHistory(@RequestHeader(name = "Authorization") String token) {
		try {
			this.userService.checkPermission(token, PermissionEnum.FLIGHT_HISTORY_MANAGEMENT);
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
					.body(this.flightService.getCanceledAndNotActivFlights());

		} catch (ErrorCodeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
					.body(e);
		}
	}
	
	@PostMapping(FLIGHT_PATH)
	public ResponseEntity<?> save(@RequestBody FlightDto flightDto,
			@RequestHeader(name = "Authorization") String token) throws FlightException, ValidatorException, AirportException, PlaneException{

		try {
			this.userService.checkPermission(token, PermissionEnum.FLIGHT_MANAGEMENT);
			this.flightService.addFlight(flightDto);
			return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(null);
		} catch (RoleException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		} catch (UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@PostMapping(FLIGHT_PATH_UPDATE)
	public ResponseEntity<?> update(@RequestBody FlightDto flightDto,
			@RequestHeader(name = "Authorization") String token) throws FlightException, ValidatorException, PlaneException, AirportException{

		try {
			this.userService.checkPermission(token, PermissionEnum.FLIGHT_MANAGEMENT);
			this.flightService.updateFlight(flightDto);
			return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(null);
		} catch (RoleException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		} catch (UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@DeleteMapping(FLIGHT_PATH_DELETE)
	public ResponseEntity<?> delete(@PathVariable Long flightID,
			@RequestHeader(name = "Authorization") String token) throws FlightException {

		try {
			this.userService.checkPermission(token, PermissionEnum.FLIGHT_MANAGEMENT);
			this.flightService.deleteFlight(flightID);
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(null);
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).contentType(MediaType.APPLICATION_JSON).body(e);

		}

	}

}
