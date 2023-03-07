package service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import converter.FlightConverter;
import dto.AirportDto;
import dto.FlightDto;
import dto.PlaneDto;
import exceptions.AirportException;
import exceptions.ErrorCode;
import exceptions.FlightException;
import exceptions.PlaneException;
import exceptions.ValidatorException;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.FlightTemplate;
import msg.project.flightmanager.model.Plane;
import repository.AirportRepository;
import repository.FlightRepository;
import repository.FlightTemplateRepository;
import repository.PlaneRepository;
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
	private AirportRepository airportRepository;

	@Autowired
	private FlightTemplateRepository flightTemplateRepository;

	@Autowired
	private PlaneRepository planeRepository;

	@Override
	public void addFlight(FlightDto flightDto)
			throws FlightException, ValidatorException, AirportException, PlaneException {

		if (!this.flightTemplateRepository.getTemplatesIds().contains(flightDto.getFlightTemplateID()))
			throw new FlightException("The flight cannot be made out of an unexisting flight template",
					ErrorCode.UNEXISTING_TEMPLATE);

		if (getFlightById(flightDto.getIdFlight()) != null)
			throw new FlightException("An flight with this ID is in the DB!",
					ErrorCode.OBJECT_WITH_THIS_ID_EXISTS_IN_THE_DB);

		this.flightValidator.validateFlight(flightDto);

		// verific sa existe aeropoartele si avionul
		Airport from = this.verifyAirport(flightDto.getFrom());
		Airport to = this.verifyAirport(flightDto.getTo());
		Plane plane = this.verifyPlane(flightDto.getPlane());

		Flight flight = this.flightConverter.convertToEntity(flightDto);

		this.flightRepository.save(flight);

		// bag in liste si salvez
		if (flight.getFrom() != null) {
			from.addFlightStart(flight);
			this.airportRepository.save(from);
		}

		if (flight.getTo() != null) {
			to.addFlightTo(flight);
			this.airportRepository.save(to);
		}

		if (flight.getPlane() != null && flight.getPlane().getFirstFlight() == null) {

			if (flight.getDate() != null) {

				plane.setFirstFlight(flight.getDate());

			} else {

				plane.setFirstFlight(java.time.LocalDate.now());

			}

			plane.addFlight(flight);
			this.planeRepository.save(plane);

		}

	}

	@Override
	public void updateFlight(FlightDto flightDto) throws FlightException, ValidatorException, PlaneException, AirportException {

		if (getFlightById(flightDto.getIdFlight()) == null) 
			throw new FlightException("An flight with this ID is NOT in the DB!",
					ErrorCode.NOT_AN_EXISTING_FLIGHT);
		else {
			this.flightValidator.validateFlight(flightDto);
			Flight oldFlight = getFlightById(flightDto.getIdFlight());
			
			Airport from = this.verifyAirport(flightDto.getFrom());
			Airport to = this.verifyAirport(flightDto.getTo());
			Plane plane = this.verifyPlane(flightDto.getPlane());
			
			Airport oldFrom = oldFlight.getFrom();
			Airport oldTo = oldFlight.getTo();
			Plane oldPlane = oldFlight.getPlane();
			LocalDate oldDate = oldFlight.getDate();
			
			Flight newFlight = this.flightConverter.convertToEntity(flightDto);

			this.flightRepository.save(newFlight);

			//verificare la from, to si plane daca sunt altele scot din liste alea veci si pun pe alea noi
			if(!from.equals(oldFrom)) {
				//fac scoatere si adaugare
				oldFrom.removeFlightStart(oldFlight);
				from.addFlightStart(newFlight);

				this.airportRepository.save(oldFrom);
				this.airportRepository.save(from);
			}
			
			if(!to.equals(oldTo)) {
				oldTo.removeFlightTo(oldFlight);
				to.addFlightTo(newFlight);
				this.airportRepository.save(oldTo);
				this.airportRepository.save(to);
			}
			
			if(!plane.equals(oldPlane)) {
				oldPlane.removeFlight(oldFlight);
				oldPlane.addFlight(newFlight);
				//verific firstFlight
				if(!oldDate.equals(newFlight.getDate()) && plane.getFirstFlight().equals(oldDate)) 
					plane.setFirstFlight(newFlight.getDate());
	
				this.planeRepository.save(oldPlane);
				this.planeRepository.save(plane);
			}
			
		}
	}

	private Plane verifyPlane(PlaneDto planeDto) throws PlaneException {

		Plane planeToReturn;
		if (planeDto != null) {
			planeToReturn = this.planeRepository.findByTailNumber(planeDto.getTailNumber()).get();
			if (planeToReturn == null)
				throw new PlaneException(
						String.format("A plane with this tail number %d does not exist", planeDto.getTailNumber()),
						ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);
			else
				return planeToReturn;
		} else
			return null;
	}

	private Airport verifyAirport(AirportDto airportDto) throws AirportException {

		Airport airportToSend;
		if (airportDto != null) {
			airportToSend = this.airportRepository.findByCodeIdentifier(airportDto.getCodeIdentifier()).get();
			if (airportToSend == null)
				throw new AirportException(
						String.format("An airport with the given code %s for End does not exist in the DB",
								airportDto.getCodeIdentifier()),
						ErrorCode.NOT_AN_EXISTING_ID_IN_THE_DB);
			else
				return airportToSend;
		} else
			return null;
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
