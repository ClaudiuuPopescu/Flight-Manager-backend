package msg.project.flightmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import msg.project.flightmanager.dto.AirportDto;
import msg.project.flightmanager.enums.PermissionEnum;
import msg.project.flightmanager.exceptions.RoleException;
import msg.project.flightmanager.exceptions.UserException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.modelHelper.ActionCompanyAirportCollab;
import msg.project.flightmanager.modelHelper.CreateAirportModel;
import msg.project.flightmanager.modelHelper.EditAirportModel;
import msg.project.flightmanager.service.interfaces.IAirportService;
import msg.project.flightmanager.service.interfaces.IUserService;

@RestController
@RequestMapping("/api/airport")
public class AirportController {

	public static final String GET_ALL = "/all";
	public static final String CREATE_AIRPORT = "/create";
	public static final String UPDATE_AIRPORT = "/update";
	public static final String DELETE_AIRPORT = "/delete/{delete-codeIdentifier}";
	public static final String ADD_COMPANY_COLLABORATION = "/add-collaboration";
	public static final String REMOVE_COMPANY_COLLABORATION = "/remove-collaboration";
	
	@Autowired
	private IAirportService airportService;
	@Autowired
	private IUserService userService;

	@GetMapping(GET_ALL)
	public ResponseEntity<List<AirportDto>> getAll(){
		
		return ResponseEntity
				.ok()
				.body(this.airportService.getAll());
	}
	
	@PostMapping(CREATE_AIRPORT)
	public ResponseEntity<?> createAirport(@RequestHeader(name = "Authorization") String token, @RequestBody CreateAirportModel createAirportModel){
		
		try {
			this.userService.checkPermission(token, PermissionEnum.AIRPORT_MANAGEMENT);
			
			this.airportService.createAirport(createAirportModel);
			
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body("Airport created successfully!");
		} catch (RoleException | UserException | ValidatorException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@PutMapping(UPDATE_AIRPORT)
	public ResponseEntity<?> updateAirport(@RequestHeader(name = "Authorization") String token, @RequestBody EditAirportModel editAirportModel ){
		
		try {
			this.userService.checkPermission(token, PermissionEnum.AIRPORT_MANAGEMENT);
			
			this.airportService.editAirport(editAirportModel);
			
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("Airport updated successfully!");
		} catch (RoleException | UserException | ValidatorException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@DeleteMapping(DELETE_AIRPORT)
	public ResponseEntity<?> deleteAirport(@RequestHeader(name = "Authorization") String token, @PathVariable("delete-codeIdentifier") String codeIdentifier){
		
		try {
			this.userService.checkPermission(token, PermissionEnum.AIRPORT_MANAGEMENT);
			
			this.airportService.removeAirport(codeIdentifier);
			
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("Airport deleted successfully!");
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@PutMapping(ADD_COMPANY_COLLABORATION)
	public ResponseEntity<?> addCollab(@RequestHeader(name = "Authorization") String token, @RequestBody ActionCompanyAirportCollab actionCompanyAirportCollab){
		
		try {
			this.userService.checkPermission(token, PermissionEnum.AIRPORT_MANAGEMENT);
			
			this.airportService.addCompanyCollab(actionCompanyAirportCollab);
			
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("Airport-Company collaboration added successfully!");
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@PutMapping(REMOVE_COMPANY_COLLABORATION)
	public ResponseEntity<?> removeCollab(@RequestHeader(name = "Authorization") String token, @RequestBody ActionCompanyAirportCollab actionCompanyAirportCollab){
		
		try {
			this.userService.checkPermission(token, PermissionEnum.AIRPORT_MANAGEMENT);
			
			this.airportService.removeCompanyCollab(actionCompanyAirportCollab);
			
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("Airport-Company collaboration removed successfully!");
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
}
