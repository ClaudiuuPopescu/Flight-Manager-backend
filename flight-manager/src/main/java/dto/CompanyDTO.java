package dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {


	private Long idCompany;

	private String name;
    
	private String phoneNumber;
    
	private String email;

	private Date foundedIn;

}
