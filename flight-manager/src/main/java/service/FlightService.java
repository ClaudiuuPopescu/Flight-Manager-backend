package service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import converter.FlightConverter;
import dto.FlightDto;
import exceptions.ErrorCode;
import exceptions.FlightException;
import exceptions.ValidatorException;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.FlightTemplate;
import repository.FlightRepository;
import repository.FlightTemplateRepository;
import service.interfaces.IFlightService;
import validator.FlightValidator;

public class FlightService implements IFlightService {

	@Autowired
	private FlightRepository flightRepository;
	
	@Autowired
	private FlightValidator flightValidator;
	
	@Autowired
	private FlightConverter flightConverter;
	
	@Autowired
	private FlightTemplateRepository flightTemplateRepository;

	//TODO verific existente + bag in liste
	@Override
	public void addFlight(FlightDto flightDto) throws FlightException, ValidatorException {
		
		if(!this.flightTemplateRepository.getTemplatesIds().contains(flightDto.getFlightTemplateID()))
			throw new FlightException("The flight cannot be made out of an unexisting flight template", ErrorCode.UNEXISTING_TEMPLATE);

		if(getFlightById(flightDto.getIdFlight()) != null)
			throw new FlightException("An flight with this ID is in the DB!", ErrorCode.OBJECT_WITH_THIS_ID_EXISTS_IN_THE_DB);
	
		else {
			this.flightValidator.validateFlight(flightDto);
			Flight flight = this.flightConverter.convertToEntity(flightDto);
			
			this.flightRepository.save(flight);
			
			//sa intreb daca trebuie salvat in db
			if(flight.getFrom() != null)
				flight.addFlightToAirportStart();
			
			if(flight.getTo() != null)
				flight.addFlightToAirportEnd();
			
			if(flight.getPlane().getFirstFlight() == null){
				
				if(flight.getDate() != null){
					
					flight.getPlane().setFirstFlight(flight.getDate());
					//TODO salvez noul plane in db
				}
				else {
					
					flight.getPlane().setFirstFlight(java.time.LocalDate.now());
					//TODO salvez noul plane in db
				}
			}
		}
	}

	@Override
	public void updateFlight(FlightDto flightDto) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFlight(Long flightID) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Flight> getAllFlights() {

		return this.flightRepository.findAll();

	}

	@Override
	public List<Flight> getFlightsByFlightTemplate(FlightTemplate flightTemplate) {

		List<Flight> allFlights = getAllFlights();
		List<Flight> allFlightsWithSpecificTemplate = allFlights.stream()
				.filter(flight -> flight.getFlightTemplate().equals(flightTemplate)).collect(Collectors.toList());

		return allFlightsWithSpecificTemplate;
	}

	@Override
	public Flight getFlightById(Long flightID) {

		return this.flightRepository.findById(flightID).get();
	}

}
