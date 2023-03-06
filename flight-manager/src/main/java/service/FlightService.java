package service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import converter.FlightConverter;
import dto.FlightDto;
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

		Airport from = null;
		Airport to = null;
		Plane plane = null;

		// verific sa existe aeropoartele si avionul
		if (flightDto.getFrom() != null) {
			from = this.airportRepository.findByCodeIdentifier(flightDto.getFrom().getCodeIdentifier()).get();
			if (from == null)
				throw new AirportException(
						String.format("An airport with the given code %s for Start does not exist in the DB",
								flightDto.getFrom().getCodeIdentifier()),
						ErrorCode.NOT_AN_EXISTING_ID_IN_THE_DB);
		}

		if (flightDto.getTo() != null) {
			to = this.airportRepository.findByCodeIdentifier(flightDto.getTo().getCodeIdentifier()).get();
			if (to == null)
				throw new AirportException(
						String.format("An airport with the given code %s for End does not exist in the DB",
								flightDto.getTo().getCodeIdentifier()),
						ErrorCode.NOT_AN_EXISTING_ID_IN_THE_DB);
		}

		if (flightDto.getPlane() != null) {
			plane = this.planeRepository.findByTailNumber(flightDto.getPlane().getTailNumber()).get();
			if (plane == null)
				throw new PlaneException(String.format("A plane with this tail number %d does not exist",
						flightDto.getPlane().getTailNumber()), ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);
		}

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
