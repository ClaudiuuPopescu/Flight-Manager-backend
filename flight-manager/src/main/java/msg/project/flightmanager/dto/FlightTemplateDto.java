package msg.project.flightmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class FlightTemplateDto {

	private Long idFlightTemplate;

	@Builder.Default
	private boolean flightName = false;

	@Builder.Default
	private boolean from = false;

	@Builder.Default
	private boolean plane = false;

	@Builder.Default
	private boolean boardingTime = false;

	@Builder.Default
	private boolean to = false;

	@Builder.Default
	private boolean date = false;

	@Builder.Default
	private boolean gate = false;

	@Builder.Default
	private boolean duration = false;
	
	@Builder.Default
	private boolean company = false;
}
