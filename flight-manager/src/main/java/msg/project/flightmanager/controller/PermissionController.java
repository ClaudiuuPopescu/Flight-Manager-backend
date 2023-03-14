package msg.project.flightmanager.controller;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import msg.project.flightmanager.service.PermissionService;

@RestController
@RequestMapping("/api/permission")
public class PermissionController {
	@Autowired
	private PermissionService permissionService;
	
	private static final String ADD_PERMISSION = "/add/{permissionName}";

	@PostMapping(ADD_PERMISSION)
	public ResponseEntity<String> addPermission(@PathVariable("permissionName") String permissionName) {

		if(!this.permissionService.addPermission(permissionName)) {
			return new ResponseEntity<>("Something went wrong when trying to add a new permission",
					HttpStatus.EXPECTATION_FAILED);
		}

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(MessageFormat.format("Permission [{0}] added successfully", permissionName));
	}
}
