package msg.project.flightmanager.modelHelper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserRole {
	
	private String usernameToChange;
	private String newRoleTitle;

}
