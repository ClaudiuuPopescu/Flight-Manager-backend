package msg.project.flightmanager.modelHelper;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserModel {

	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private Date birthDate;

	private CreateAddressModel address;
}
