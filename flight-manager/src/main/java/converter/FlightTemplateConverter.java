package converter;

import org.springframework.stereotype.Component;

import dto.FlightTemplateDto;
import msg.project.flightmanager.model.FlightTemplate;

@Component
public class FlightTemplateConverter implements IConverter<FlightTemplate, FlightTemplateDto>{

	@Override
	public FlightTemplateDto convertToDTO(FlightTemplate flightTemplate) {
	
		return FlightTemplateDto.builder()
				.boardingTime(flightTemplate.isBoardingTime())
				.date(flightTemplate.isDate())
				.flightName(flightTemplate.isFlightName())
				.from(flightTemplate.isFrom())
				.to(flightTemplate.isTo())
				.plane(flightTemplate.isPlane())
				.gate(flightTemplate.isGate())
				.duration(flightTemplate.isDuration())
				.build();
	}

	@Override
	public FlightTemplate convertToEntity(FlightTemplateDto flightTemplateDto) {

		return FlightTemplate.builder()
				.boardingTime(flightTemplateDto.isBoardingTime())
				.date(flightTemplateDto.isDate())
				.flightName(flightTemplateDto.isFlightName())
				.from(flightTemplateDto.isFrom())
				.to(flightTemplateDto.isTo())
				.plane(flightTemplateDto.isPlane())
				.gate(flightTemplateDto.isGate())
				.duration(flightTemplateDto.isDuration())
				.build();
	}
	

}
