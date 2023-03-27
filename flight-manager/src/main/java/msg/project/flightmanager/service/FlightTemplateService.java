package msg.project.flightmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import msg.project.flightmanager.converter.FlightTemplateConverter;
import msg.project.flightmanager.dto.FlightTemplateDto;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.FlightTemplateException;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.FlightTemplate;
import msg.project.flightmanager.repository.FlightTemplateRepository;
import msg.project.flightmanager.service.interfaces.IFlightTemplateService;

@Service
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

		Optional<FlightTemplate> optionalFlightTemplate = findFlightTemplateBYID(
				flightTemplateDto.getIdFlightTemplate());

		if (optionalFlightTemplate.isEmpty())
			throw new FlightTemplateException("A Flight Template with this ID does not exists!",
					ErrorCode.NOT_AN_EXISTING_ID_IN_THE_DB);
		else {

			FlightTemplate newFlightTemplate = this.flightTemplateConverter.convertToEntity(flightTemplateDto);
			if (verifyEqualExistingTemplate(newFlightTemplate))
				throw new FlightTemplateException("A Flight Template like the given one already exists!",
						ErrorCode.EXISTING_TEMPLATE_LIKE_THE_GIVEN_ONE);
			else {

				this.flightTemplateRepository.save(newFlightTemplate);
			}

		}

	}

	@Override
	public void deleteFlightTemplate(Long flightTemplateId) throws FlightTemplateException {

		Optional<FlightTemplate> optionalFlightTemplate = findFlightTemplateBYID(flightTemplateId);

		if (optionalFlightTemplate.isEmpty())
			throw new FlightTemplateException("A Flight Template with this ID does not exists!",
					ErrorCode.NOT_AN_EXISTING_ID_IN_THE_DB);
		else {
			FlightTemplate flightTemplate = optionalFlightTemplate.get();
			List<Flight> flightsWithGivenTemplate = this.flightService.getFlightsByFlightTemplate(flightTemplate);
			flightsWithGivenTemplate.stream().filter(flight -> flight.getFlightTemplate().equals(flightTemplate))
					.forEach(flight -> {
						flight.setActiv(false);
						flight.setCanceled(true);
					});
		}
	}

	@Override
	public List<FlightTemplate> getAllFlightTemplates() {

		return this.flightTemplateRepository.findAll();
	}

	@Override
	public Optional<FlightTemplate> findFlightTemplateBYID(Long flightTemplateId) {

		return this.flightTemplateRepository.findById(flightTemplateId);
	}

	private boolean verifyEqualExistingTemplate(FlightTemplate flightTemplate) {

		List<FlightTemplate> flightTemplats = getAllFlightTemplates();

		return flightTemplats.stream().anyMatch(flightTemp -> flightTemplate.equals(flightTemp));

	}

}
