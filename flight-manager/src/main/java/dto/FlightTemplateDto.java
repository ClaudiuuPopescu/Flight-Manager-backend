package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
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
}
