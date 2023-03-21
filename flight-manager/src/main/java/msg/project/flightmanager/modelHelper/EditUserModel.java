package msg.project.flightmanager.modelHelper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EditUserModel {

	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
}
