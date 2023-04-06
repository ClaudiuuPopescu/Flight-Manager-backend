package msg.project.flightmanager.dto;

import java.time.LocalDate;

import io.micrometer.common.lang.Nullable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportDto {
	
	private String reportCode;
	private String reportType;
	private LocalDate generateAt;
	private String content;
	private String reporteByUsername; // no need of user dto at all in front
	@Nullable
	private LocalDate generatedAt;
	// need it to display info in front
	// when creating only need its ID
	private FlightDto flightDto;

}
