package service.interfaces;

import java.util.List;

import dto.FlightTemplateDto;
import msg.project.flightmanager.model.FlightTemplate;


public interface IFlightTemplateService {

	void addFlightTemplate(FlightTemplateDto flightTemplateDto);
	
	void updateFlightTemplate(FlightTemplateDto flightTemplateDto);
	
	void deleteFlightTemplate(Long flightTemplateId);
	
	List<FlightTemplate> getAllFlightTemplates();
	
	FlightTemplate findFlightTemplateBYID(Long flightTemplateId);
	
}
