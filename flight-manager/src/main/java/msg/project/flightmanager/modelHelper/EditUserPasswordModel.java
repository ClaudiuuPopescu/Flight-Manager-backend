package msg.project.flightmanager.modelHelper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditUserPasswordModel {
	
	private String currentPassword;
	private String newPassword;

}
