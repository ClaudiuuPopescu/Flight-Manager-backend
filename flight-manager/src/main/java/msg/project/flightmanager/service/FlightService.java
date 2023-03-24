package msg.project.flightmanager.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
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

		Optional<Flight> optionalFlight = getFlightById(flightDto.getIdFlight());
		if (optionalFlight.isEmpty())
			throw new FlightException("An flight with this ID is in the DB!",
					ErrorCode.OBJECT_WITH_THIS_ID_EXISTS_IN_THE_DB);
		else {

			this.flightValidator.validateFlight(flightDto);

			this.verifyAirport(flightDto.getFrom());
			this.verifyAirport(flightDto.getTo());
			this.verifyPlane(flightDto.getPlane());

			Flight flight = this.flightConverter.convertToEntity(flightDto);
			this.flightRepository.save(flight);
			Plane plane = flight.getPlane();
			if (plane != null && plane.getFirstFlight() == null) {

				if (flight.getDate() != null) {

					plane.setFirstFlight(flight.getDate());

				} else {

					plane.setFirstFlight(java.time.LocalDate.now());

				}

				plane.addFlight(flight);
				this.planeRepository.save(plane);
			}
		}
	}

	@Override
	public void updateFlight(FlightDto flightDto)
			throws FlightException, ValidatorException, PlaneException, AirportException {

		Optional<Flight> optionalFlight = getFlightById(flightDto.getIdFlight());
		if (optionalFlight.isEmpty())
			throw new FlightException("An flight with this ID is NOT in the DB!", ErrorCode.NOT_AN_EXISTING_FLIGHT);
		else {
			this.flightValidator.validateFlight(flightDto);
			this.verifyAirport(flightDto.getFrom());
			this.verifyAirport(flightDto.getTo());
			this.verifyPlane(flightDto.getPlane());

			Flight newFlight = this.flightConverter.convertToEntity(flightDto);

			this.flightRepository.save(newFlight);

		}
	}

	@Override
	public void deleteFlight(Long flightID) throws FlightException {

		Optional<Flight> optionalFlight = getFlightById(flightID);
		if (optionalFlight.isEmpty())
			throw new FlightException("An flight with this ID is NOT in the DB!", ErrorCode.NOT_AN_EXISTING_FLIGHT);
		else {
			Flight flight = optionalFlight.get();
			Plane plane = flight.getPlane();

			if (plane.getFirstFlight() != null && plane.getFirstFlight().equals(flight.getDate())) {
				plane.setFirstFlight(null);
				this.planeRepository.save(plane);
			}

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
	public Optional<Flight> getFlightById(Long flightID) {

		return this.flightRepository.findById(flightID);
	}

	@Override
	public List<Flight> getCanceledAndNotActivFlights() {

		verifyDateOfTheFlights(getAllFlights());
		return getAllFlights().stream().filter(flight -> !flight.isActiv() || flight.isCanceled())
				.collect(Collectors.toList());
	}

	@Override
	public List<Flight> getAllActiv() {
		verifyDateOfTheFlights(getAllFlights());
		return getAllFlights().stream().filter(flight -> flight.isActiv() && !flight.isCanceled())
				.collect(Collectors.toList());
	}

	private void verifyPlane(PlaneDto planeDto) throws PlaneException {

		if (planeDto != null) {
			Optional<Plane> optionalPlane = this.planeRepository.findByTailNumber(planeDto.getTailNumber());
			if (optionalPlane.isEmpty())
				throw new PlaneException(
						String.format("A plane with this tail number %d does not exist", planeDto.getTailNumber()),
						ErrorCode.NOT_AN_EXISTING_NAME_IN_THE_DB);
		}
	}

	private void verifyAirport(AirportDto airportDto) throws AirportException {

		if (airportDto != null) {
			Optional<Airport> optionalAirport = this.airportRepository
					.findByCodeIdentifier(airportDto.getCodeIdentifier());
			if (optionalAirport.isEmpty())
				throw new AirportException(
						String.format("An airport with the given code %s for End does not exist in the DB",
								airportDto.getCodeIdentifier()),
						ErrorCode.NOT_AN_EXISTING_ID_IN_THE_DB);
		}
	}

	private void verifyDateOfTheFlights(List<Flight> flights) {

		LocalDate currentDate = java.time.LocalDate.now();
		flights.stream().filter(flight -> flight.getDate().isBefore(currentDate)).forEach(flight -> {
			flight.setActiv(false);
			this.flightRepository.save(flight);
		});
	}
}
