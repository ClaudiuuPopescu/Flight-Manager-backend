package msg.project.flightmanager.modelHelper;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import msg.project.flightmanager.dto.AddressDto;

@Data
@Setter
@Builder
@AllArgsConstructor
public class CreateUserModel {

	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private LocalDate birthDate;
	private String roleTitle;

	private AddressDto address;
}
