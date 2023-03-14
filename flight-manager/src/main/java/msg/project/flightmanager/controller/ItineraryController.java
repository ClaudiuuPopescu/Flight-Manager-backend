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
import msg.project.flightmanager.exceptions.RoleException;
import msg.project.flightmanager.exceptions.UserException;
import msg.project.flightmanager.modelHelper.EditItineraryModel;
import msg.project.flightmanager.modelHelper.ItineraryHelperModel;
import msg.project.flightmanager.service.interfaces.IItineraryService;
import msg.project.flightmanager.service.interfaces.IUserService;

@RestController
@RequestMapping("/api/itinerary")
public class ItineraryController {
	
	public static final String GET_ALL = "/all";
	public static final String CREATE_ITINERARY = "/create";
	public static final String UPDATE_ITINERARY = "/update";
	public static final String DELETE_ITINERARY = "/delete/{delte-idItinerary}";

	@Autowired
	private IItineraryService itineraryService;
	@Autowired
	private IUserService userService;
	
	@GetMapping(GET_ALL)
	public ResponseEntity<?> getAll(){
		
		return ResponseEntity
				.ok()
				.body(this.itineraryService.getAll());
	}
	
	@PostMapping(CREATE_ITINERARY)
	public ResponseEntity<?> createItinerary(@RequestHeader(name = "Authorization") String token, @RequestBody ItineraryHelperModel itineraryHelperModel){
	
		try {
			this.userService.checkPermission(token, PermissionEnum.ITINERARY_MANAGEMENT);
		
			this.itineraryService.createItinerary(itineraryHelperModel);
				
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body("Itinerary created successfully!");
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@PutMapping(UPDATE_ITINERARY)
	public ResponseEntity<?> updateItinerary(@RequestHeader(name = "Authorization") String token, @RequestBody EditItineraryModel editItineraryModel){
	
		try {
			this.userService.checkPermission(token, PermissionEnum.ITINERARY_MANAGEMENT);
		
			this.itineraryService.editItinerary(editItineraryModel);
				
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body("Itinerary updated successfully!");
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@DeleteMapping(DELETE_ITINERARY)
	public ResponseEntity<?> deleteItinerary(@RequestHeader(name = "Authorization") String token, @PathVariable("delete-idItinerary") Long idItinerary){
	
		try {
			this.userService.checkPermission(token, PermissionEnum.ITINERARY_MANAGEMENT);
		
			this.itineraryService.removeItinerary(idItinerary);
				
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body("Itinerary removed successfully!");
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
}
