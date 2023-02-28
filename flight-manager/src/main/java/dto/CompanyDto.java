package dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyDto {

	private String name;
    
	private String phoneNumber;
    
	private String email;

	private LocalDate foundedIn;
	
	private AddressDto address;
}
