package msg.project.flightmanager.service;

import java.time.LocalDate;
import java.util.List;import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import msg.project.flightmanager.converter.FlightConverter;
import msg.project.flightmanager.dto.AirportDto;
import msg.project.flightmanager.dto.FlightDto;
import msg.project.flightmanager.dto.PlaneDto;
import msg.project.flightmanager.exceptions.AirportException;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.FlightException;
import msg.project.flightmanager.exceptions.PlaneException;
import msg.project.flightmanager.exceptions.ValidatorException;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.FlightTemplate;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.repository.AirportRepository;
import msg.project.flightmanager.repository.FlightRepository;
import msg.project.flightmanager.repository.FlightTemplateRepository;
import msg.project.flightmanager.repository.PlaneRepository;
import msg.project.flightmanager.service.interfaces.IFlightService;
import msg.project.flightmanager.validator.FlightValidator;

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
		flight.getFlightTemplate().addFlight(flight);
		this.flightTemplateRepository.save(flight.getFlightTemplate());

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
	public void updateFlight(FlightDto flightDto)
			throws FlightException, ValidatorException, PlaneException, AirportException {

		if (getFlightById(flightDto.getIdFlight()) == null)
			throw new FlightException("An flight with this ID is NOT in the DB!", ErrorCode.NOT_AN_EXISTING_FLIGHT);
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

			// verificare la from, to si plane daca sunt altele scot din liste alea veci si
			// pun pe alea noi
			if (!from.equals(oldFrom)) {
				// fac scoatere si adaugare
				oldFrom.removeFlightStart(oldFlight);
				from.addFlightStart(newFlight);

				this.airportRepository.save(oldFrom);
				this.airportRepository.save(from);
			}

			if (!to.equals(oldTo)) {
				oldTo.removeFlightTo(oldFlight);
				to.addFlightTo(newFlight);
				this.airportRepository.save(oldTo);
				this.airportRepository.save(to);
			}

			if (!plane.equals(oldPlane)) {
				oldPlane.removeFlight(oldFlight);
				oldPlane.addFlight(newFlight);
				// verific firstFlight
				if (!oldDate.equals(newFlight.getDate()) && plane.getFirstFlight().equals(oldDate))
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
	public void deleteFlight(Long flightID) throws FlightException {

		if (getFlightById(flightID) == null)
			throw new FlightException("An flight with this ID is NOT in the DB!", ErrorCode.NOT_AN_EXISTING_FLIGHT);
		else {
			Flight flight = getFlightById(flightID);

			Airport from = flight.getFrom();
			Airport to = flight.getTo();
			Plane plane = flight.getPlane();

			from.removeFlightStart(flight);
			to.removeFlightTo(flight);
			plane.removeFlight(flight);

			if (plane.getFirstFlight().equals(flight.getDate()))
				plane.setFirstFlight(null);

			this.planeRepository.save(plane);
			this.airportRepository.save(to);
			this.airportRepository.save(from);
			FlightTemplate template = flight.getFlightTemplate();
			template.removeFlight(flight);
			this.flightTemplateRepository.save(template);
			this.flightRepository.delete(flight);
		}

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
	
	private void verifyDateOfTheFlights(List<Flight> flights) {
		
		LocalDate currentDate = java.time.LocalDate.now();
		flights.stream().filter(flight -> flight.getDate().isBefore(currentDate))
		.forEach(flight -> {flight.setActiv(false); this.flightRepository.save(flight);});
	}

	@Override
	public List<Flight> getCanceledAndNotActivFlights() {
		
		verifyDateOfTheFlights(getAllFlights());
		return  getAllFlights().stream().filter(flight -> !flight.isActiv() || flight.isCanceled())
				.collect(Collectors.toList());
	}

	@Override
	public List<Flight> getAllActiv() {
		verifyDateOfTheFlights(getAllFlights());
		return  getAllFlights().stream().filter(flight -> flight.isActiv() && !flight.isCanceled())
				.collect(Collectors.toList());
	}

}
