package modelHelper;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditUserModel {

	private String usernameToEdit;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
}
