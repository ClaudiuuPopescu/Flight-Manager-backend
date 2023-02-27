package dto;

import java.sql.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private Date birthDate;
	private boolean isActive;
	private AddressDto address;
	private RoleDto role;
	private CompanyDto company;
}
