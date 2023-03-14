package msg.project.flightmanager.controller;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import msg.project.flightmanager.modelHelper.AddPermissionToRoleModel;
import msg.project.flightmanager.service.RoleService;

@RestController
@RequestMapping("/api/role")
public class RoleController {
	@Autowired
	private RoleService roleService;
	
	private static final String ADD_ROLE = "/add/{roleTitle}";
	private static final String ADD_PERMISSION_TO_ROLE = "/add-permission-to-role";
	
	@PostMapping(ADD_ROLE)
	public ResponseEntity<String> addRole(@PathVariable("roleTitle") String roleTitle) {

		if(!this.roleService.addRole(roleTitle)) {
			return new ResponseEntity<>("Something went wrong when trying to add a new role",
					HttpStatus.EXPECTATION_FAILED);
		}

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(MessageFormat.format("Role [{0}] added successfully", roleTitle));
	}
	
	@PostMapping(ADD_PERMISSION_TO_ROLE)
	public ResponseEntity<String> addPermissionToRole (@RequestBody AddPermissionToRoleModel addPermissionToRoleModel){
		
		if(!this.roleService.addPermissionToRole(addPermissionToRoleModel)) {
			return new ResponseEntity<>("Something went wrong when trying to add permission to role",
					HttpStatus.EXPECTATION_FAILED);
		}
		
		return ResponseEntity
				.status(HttpStatus.ACCEPTED)
				.body(MessageFormat.format("Role [{0}] assigned with permission [{1}] successfully",
						addPermissionToRoleModel.getRoleTitle(), addPermissionToRoleModel.getPermissionTitle()));
		
	}

}
