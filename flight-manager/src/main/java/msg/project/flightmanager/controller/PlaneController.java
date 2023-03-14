package msg.project.flightmanager.controller;

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

import msg.project.flightmanager.enums.PermissionEnum;
import msg.project.flightmanager.exceptions.CompanyException;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.exceptions.RoleException;
import msg.project.flightmanager.exceptions.UserException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.modelHelper.CreatePlaneModel;
import msg.project.flightmanager.modelHelper.EditLastRevisionPlaneModel;
import msg.project.flightmanager.service.interfaces.IPlaneService;
import msg.project.flightmanager.service.interfaces.IUserService;

@RestController
@RequestMapping("/api/plane")
public class PlaneController {
	
	public static final String GET_ALL = "/all";
	public static final String CREATE_PLANE = "/create";
	public static final String UPDATE_LAST_REVISION = "/update/last-revision";
	public static final String DELETE_PLANE = "/delete/{delete-tailNumber}";
	public static final String MOVE_PLANE_TO_COMPANY = "/move/{move-tailNumber}/{to-companyName}";

	@Autowired
	private IPlaneService planeService;
	@Autowired
	private IUserService userService; 
	
	@GetMapping(GET_ALL)
	public ResponseEntity<?> getAll(){
		
		return ResponseEntity
				.ok()
				.body(this.planeService.getAll());
	}
	
	@PostMapping(CREATE_PLANE)
	public ResponseEntity<?> createPlane(@RequestHeader(name = "Authorization") String token, @RequestBody CreatePlaneModel createPlaneModel){
		
		try {
			this.userService.checkPermission(token, PermissionEnum.AIRPLANE_MANAGEMENT);
			
			this.planeService.createPlane(createPlaneModel);
			
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body("Plane created successfully!");
		} catch (RoleException | UserException | ValidatorException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@PutMapping(UPDATE_LAST_REVISION)
	public ResponseEntity<?> updateLastRevision(@RequestHeader(name = "Authorization") String token, @RequestBody EditLastRevisionPlaneModel editLastRevisionPlaneModel){
		
		try {
			this.userService.checkPermission(token, PermissionEnum.AIRPLANE_MANAGEMENT);
			
			this.planeService.editLastRevisionPlane(editLastRevisionPlaneModel);
			
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("Plane's last revision updated successfully!");
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@DeleteMapping(DELETE_PLANE)
	public ResponseEntity<?> deletePlane(@RequestHeader(name = "Authorization") String token, @PathVariable("delete-tailNumber") int tailNumber){
		
		try {
			this.userService.checkPermission(token, PermissionEnum.AIRPLANE_MANAGEMENT);
			
			this.planeService.removePlane(tailNumber);
			
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("Plane deleted successfully!");
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@PutMapping(MOVE_PLANE_TO_COMPANY)
	public ResponseEntity<?> movePlaneToCompany(@RequestHeader(name = "Authorization") String token, @PathVariable("move-tailNumber") int tailNumber, 
			@PathVariable("to-companyName") String companyName){
		
		try {
			this.userService.checkPermission(token, PermissionEnum.AIRPLANE_MANAGEMENT);
			
			this.planeService.movePlaneToAnotherCompany(tailNumber, companyName);
			
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("Plane moved successfully!");
		} catch (RoleException | UserException | CompanyException | FlightManagerException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
}
