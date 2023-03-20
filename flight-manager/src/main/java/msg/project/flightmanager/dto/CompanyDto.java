package msg.project.flightmanager.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {

	private String name;

	private String phoneNumber;

	private String email;

	private LocalDate foundedIn;

	private AddressDto address;

}
