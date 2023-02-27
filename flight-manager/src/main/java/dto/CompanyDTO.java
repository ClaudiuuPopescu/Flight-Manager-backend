package dto;

import java.sql.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyDto {

	private String name;
	private String phoneNumber;
	private String email;
	private Date foundedIn;
	private AddressDto address;
}
