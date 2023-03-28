package msg.project.flightmanager.modelHelper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EditUserModel {

	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
}
