package msg.project.flightmanager.dto;

import java.time.LocalDateTime;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
	
	private String reportCode;
	private String reportType;
	@Nullable
	private LocalDateTime generatedAt;
	private String content;
	private String reporteByUsername; // no need of user dto at all in front + doesnt need to be sent from front, getting it from token
	private FlightDto flightDto; // need it to display info in front
	// when creating only need its ID

}
