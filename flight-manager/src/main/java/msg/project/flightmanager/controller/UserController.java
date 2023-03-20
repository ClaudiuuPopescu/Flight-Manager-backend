package msg.project.flightmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import msg.project.flightmanager.dto.AddressDto;
import msg.project.flightmanager.dto.UserDto;
import msg.project.flightmanager.enums.PermissionEnum;
import msg.project.flightmanager.enums.RoleEnum;
import msg.project.flightmanager.exceptions.RoleException;
import msg.project.flightmanager.exceptions.UserException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.modelHelper.CreateUserModel;
import msg.project.flightmanager.modelHelper.EditUserModel;
import msg.project.flightmanager.modelHelper.UpdateUserRole;
import msg.project.flightmanager.service.TokenService;
import msg.project.flightmanager.service.interfaces.IUserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	public static final String GET_ALL = "/all";
	public static final String CREATE_USER = "/create";
	public static final String UPDATE_PERSONAL_DETAILS = "/update";
	public static final String UPDATE_USER_ROLE = "/update-role";
	public static final String UPDATE_USER_ADDRESS = "/update-address";
	public static final String GET_USER_BY_USERNAME = "/get-by-username/{get-username}";
	public static final String DEACTIVATE_USER = "/deactivate/{deactivate-username}";
	public static final String ACTIVATE_USER = "/activate/{activate-username}";
	
	@Autowired
	private IUserService userService;
	@Autowired
	private TokenService tokenService;
	
	@GetMapping(GET_ALL)
	public ResponseEntity<List<UserDto>> getAll(){
		
		return ResponseEntity
				.ok()
				.body(this.userService.getAll());
	}
	
	@GetMapping(GET_USER_BY_USERNAME)
	public ResponseEntity<UserDto> getByUsername(@PathVariable("get-username") String username){
		
		return ResponseEntity
				.ok()
				.body(this.userService.getByUsername(username));
	}
	
	@PostMapping(CREATE_USER)
	public ResponseEntity<?> createUser(@RequestHeader(name = "Authorization") String token, @RequestBody CreateUserModel createUserModel){
		String role = createUserModel.getRoleTitle().toLowerCase();
		
		try {
			
			if(role == "crew") {
				this.userService.checkPermission(token, PermissionEnum.CREW_MANAGEMENT);
			}
			if(role == "flight_manager") {
				this.userService.checkPermission(token, PermissionEnum.FLIGHT_MANAGER_MANAGEMENT);
			}
			if(role == "company_manager") {
				this.userService.checkPermission(token, PermissionEnum.COMPANY_MANAGER_MANAGEMENT);
			}
			if(role == "administrator") {
				this.userService.checkPermission(token, PermissionEnum.ADMINISTRATOR_MANAGEMENT);
			}
			
			this.userService.createUser(createUserModel);
			
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body("User created successfully!");
		} catch (RoleException | UserException | ValidatorException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@PutMapping(UPDATE_PERSONAL_DETAILS)
	public ResponseEntity<?> updatePersonalDetails(@RequestHeader(name = "Authorization") String token, @RequestBody EditUserModel editUserModel ){
		
		try {
			this.userService.checkPermission(token, PermissionEnum.PERSONAL_DATA_MANAGEMENT);
			
			String currentUsername = this.tokenService.getCurrentUserUsername(token);
						
				this.userService.editPersonalDetails(currentUsername, editUserModel);
				
				return ResponseEntity
						.status(HttpStatus.ACCEPTED)
						.body("Personal details updated successfully!");
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@PutMapping(UPDATE_USER_ROLE)
	public ResponseEntity<?> updateUserRole(@RequestHeader(name = "Authorization") String token, @RequestBody UpdateUserRole updateUserRole){
		
		try {
			this.userService.checkPermission(token, PermissionEnum.ADMINISTRATOR_MANAGEMENT);
			
			String currentUsername = this.tokenService.getCurrentUserUsername(token);
			
			if(currentUsername == updateUserRole.getUsernameToChange()) {
				return new ResponseEntity<>(
						"You can not update your own role. Someone else has to perfom this action for you.",
						HttpStatus.FORBIDDEN);
			}
						
			this.userService.editUserRole(updateUserRole);
				
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("User role updated successfully!");
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@PutMapping(UPDATE_USER_ADDRESS)
	public ResponseEntity<?> updateUserAddress(@RequestHeader(name = "Authorization") String token, @RequestBody AddressDto addressDto){
		
		try {
			this.userService.checkPermission(token, PermissionEnum.PERSONAL_DATA_MANAGEMENT);

			String currentUsername = this.tokenService.getCurrentUserUsername(token);
						
			this.userService.editUserAddress(currentUsername, addressDto);
				
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("User address updated successfully!");
		} catch (RoleException | UserException | ValidatorException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@PostMapping(DEACTIVATE_USER)
	public ResponseEntity<?> deactivateUser(@RequestHeader(name = "Authorization") String token, @PathVariable("deactivate-username") String deactivateUsername){
		
		try {
			UserDto deactivateUserDto = this.userService.getByUsername(deactivateUsername);
			
			if(deactivateUserDto.getRole().getRoleEnum() == RoleEnum.CREW) {
				this.userService.checkPermission(token, PermissionEnum.CREW_MANAGEMENT);
			}
			if(deactivateUserDto.getRole().getRoleEnum() == RoleEnum.FM) {
				this.userService.checkPermission(token, PermissionEnum.FLIGHT_MANAGER_MANAGEMENT);
			}
			if(deactivateUserDto.getRole().getRoleEnum() == RoleEnum.CM) {
				this.userService.checkPermission(token, PermissionEnum.COMPANY_MANAGER_MANAGEMENT);
			}
			if(deactivateUserDto.getRole().getRoleEnum() == RoleEnum.ADM) {
				this.userService.checkPermission(token, PermissionEnum.ADMINISTRATOR_MANAGEMENT);
			}
			
			String currentUsername = this.tokenService.getCurrentUserUsername(token);
			
			if(currentUsername == deactivateUserDto.getUsername()) {
				return new ResponseEntity<>(
						"You can not deactivate your own account. Someone else has to perfom this action for you.",
						HttpStatus.FORBIDDEN);
			}
			
			this.userService.deactivateUser(deactivateUsername);
			
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("User deactivated successfully!");
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
	
	@PostMapping(ACTIVATE_USER)
	public ResponseEntity<?> activateUser(@RequestHeader(name = "Authorization") String token, @PathVariable("activate-username") String activateUsername){
		
		try {
			UserDto activateUserDto = this.userService.getByUsername(activateUsername);
			
			if(activateUserDto.getRole().getRoleEnum() == RoleEnum.CREW) {
				this.userService.checkPermission(token, PermissionEnum.CREW_MANAGEMENT);
			}
			if(activateUserDto.getRole().getRoleEnum() == RoleEnum.FM) {
				this.userService.checkPermission(token, PermissionEnum.FLIGHT_MANAGER_MANAGEMENT);
			}
			if(activateUserDto.getRole().getRoleEnum() == RoleEnum.CM) {
				this.userService.checkPermission(token, PermissionEnum.COMPANY_MANAGER_MANAGEMENT);
			}
			if(activateUserDto.getRole().getRoleEnum() == RoleEnum.ADM) {
				this.userService.checkPermission(token, PermissionEnum.ADMINISTRATOR_MANAGEMENT);
			}
			
			String currentUsername = this.tokenService.getCurrentUserUsername(token);
			
			if(currentUsername == activateUserDto.getUsername()) {
				return new ResponseEntity<>(
						"You can not activate your own account. Someone else has to perfom this action for you.",
						HttpStatus.FORBIDDEN);
			}
			
			this.userService.activateUser(activateUsername);
			
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("User activated successfully!");
		} catch (RoleException | UserException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
	}
}
