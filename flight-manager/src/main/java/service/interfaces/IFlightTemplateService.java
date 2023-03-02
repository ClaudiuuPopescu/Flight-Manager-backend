package service.interfaces;

import java.util.List;

import dto.FlightTemplateDto;
import exceptions.FlightTemplateException;
import msg.project.flightmanager.model.FlightTemplate;

public interface IFlightTemplateService {

	void addFlightTemplate(FlightTemplateDto flightTemplateDto) throws FlightTemplateException;

	void updateFlightTemplate(FlightTemplateDto flightTemplateDto) throws FlightTemplateException;

	void deleteFlightTemplate(Long flightTemplateId) throws FlightTemplateException;

	List<FlightTemplate> getAllFlightTemplates();

	FlightTemplate findFlightTemplateBYID(Long flightTemplateId) throws FlightTemplateException;

}
