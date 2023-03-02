package service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import converter.FlightTemplateConverter;
import dto.FlightTemplateDto;
import exceptions.ErrorCode;
import exceptions.FlightTemplateException;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.FlightTemplate;
import repository.FlightTemplateRepository;
import service.interfaces.IFlightTemplateService;

public class FlightTemplateService implements IFlightTemplateService {

	@Autowired
	private FlightTemplateConverter flightTemplateConverter;

	@Autowired
	private FlightTemplateRepository flightTemplateRepository;

	@Autowired
	private FlightService flightService;

	@Override
	public void addFlightTemplate(FlightTemplateDto flightTemplateDto) throws FlightTemplateException {

		if (findFlightTemplateBYID(flightTemplateDto.getIdFlightTemplate()) != null)
			throw new FlightTemplateException("A Flight Template with this ID exists!",
					ErrorCode.OBJECT_WITH_THIS_ID_EXISTS_IN_THE_DB);

		FlightTemplate flightTemplate = this.flightTemplateConverter.convertToEntity(flightTemplateDto);

		if (verifyEqualExistingTemplate(flightTemplate))
			throw new FlightTemplateException("A Flight Template like the given one already exists!",
					ErrorCode.EXISTING_TEMPLATE_LIKE_THE_GIVEN_ONE);
		else
			this.flightTemplateRepository.save(flightTemplate);
	}

	@Override
	public void updateFlightTemplate(FlightTemplateDto flightTemplateDto) throws FlightTemplateException {
		
		if (findFlightTemplateBYID(flightTemplateDto.getIdFlightTemplate()) == null)
			throw new FlightTemplateException("A Flight Template with this ID does not exists!",
					ErrorCode.NOT_AN_EXISTING_ID_IN_THE_DB);

		FlightTemplate newFlightTemplate = this.flightTemplateConverter.convertToEntity(flightTemplateDto);

		if (verifyEqualExistingTemplate(newFlightTemplate))
			throw new FlightTemplateException("A Flight Template like the given one already exists!",
					ErrorCode.EXISTING_TEMPLATE_LIKE_THE_GIVEN_ONE);
		else {
			
			FlightTemplate oldFlightTemplate = findFlightTemplateBYID(flightTemplateDto.getIdFlightTemplate());
			
			//cand dau update la Flight Template fac update si la flight la field-uri
			List<Flight> flights = this.flightService.getFlightsByFlightTemplate(oldFlightTemplate);
			
			for(Flight flight : flights) {
				
				if(!newFlightTemplate.isFlightName() && !flight.getFlightName().isEmpty())
					flight.setFlightName(null);
				
				if(!newFlightTemplate.isFrom() && flight.getFrom() != null)
					flight.setFrom(null);
					
				if(!newFlightTemplate.isPlane() && flight.getPlane() != null)
					flight.setPlane(null);
					
				if(!newFlightTemplate.isBoardingTime() && flight.getBoardingTime() != null)
					flight.setBoardingTime(null);
					
				if(!newFlightTemplate.isTo() && flight.getTo() != null)
					flight.setBoardingTime(null);
				
				if(!newFlightTemplate.isDate() && flight.getDate() != null)
					flight.setDate(null);
					
				if(!newFlightTemplate.isGate() && flight.getGate().isEmpty())
					flight.setGate(null);
					
				if(!newFlightTemplate.isDuration() && flight.getDuration() != 0)
					flight.setDuration(0);
			}
			
			this.flightTemplateRepository.save(newFlightTemplate);
			
		}

	}

	@Override
	public void deleteFlightTemplate(Long flightTemplateId) throws FlightTemplateException {
	
		if (findFlightTemplateBYID(flightTemplateId) == null)
			throw new FlightTemplateException("A Flight Template with this ID does not exists!",
					ErrorCode.NOT_AN_EXISTING_ID_IN_THE_DB);

		FlightTemplate flightTemplate = findFlightTemplateBYID(flightTemplateId);

		//TODO sa dezactivez zboruriile cu tempatul asta
	}

	@Override
	public List<FlightTemplate> getAllFlightTemplates() {

		return this.flightTemplateRepository.findAll();
	}

	@Override
	public FlightTemplate findFlightTemplateBYID(Long flightTemplateId) throws FlightTemplateException {

		Optional<FlightTemplate> optionalFlightTemplate = this.flightTemplateRepository.findById(flightTemplateId);
		if (optionalFlightTemplate.isEmpty())
			throw new FlightTemplateException("There is no flight template with this ID in the DB ",
					ErrorCode.NOT_AN_EXISTING_ID_IN_THE_DB);

		return optionalFlightTemplate.get();
	}

	private boolean verifyEqualExistingTemplate(FlightTemplate flightTemplate) {

		List<FlightTemplate> flightTemplats = getAllFlightTemplates();

		return flightTemplats.stream().anyMatch(flightTemp -> flightTemplate.equals(flightTemp));

//		for (FlightTemplate flightTemp : flightTemplats)
//			if (flightTemplate.equals(flightTemp))
//				return true;
//
//		return false;
	}

}
