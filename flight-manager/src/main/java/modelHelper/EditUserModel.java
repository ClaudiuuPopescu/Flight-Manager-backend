package modelHelper;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditUserModel {

	// nu vad rost sa schimb sa schimibi prenumele
	// numele de familie se poate
	// nici bDay
	private String usernameToEdit;
	private String lastName;
	private String email;
	private String phoneNumber;
}
