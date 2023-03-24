package msg.project.flightmanager.servicies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import msg.project.flightmanager.converter.FlightConverter;
import msg.project.flightmanager.dto.FlightDto;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.FlightException;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.FlightTemplate;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.repository.AirportRepository;
import msg.project.flightmanager.repository.FlightRepository;
import msg.project.flightmanager.repository.FlightTemplateRepository;
import msg.project.flightmanager.repository.PlaneRepository;
import msg.project.flightmanager.service.FlightService;
import msg.project.flightmanager.validator.FlightValidator;

@ExtendWith(MockitoExtension.class)
public class FlightTest {

	@Mock
	private FlightRepository flightRepository;

	@Mock
	private FlightValidator flightValidator;

	@Mock
	private FlightConverter flightConverter;

	@Mock
	private AirportRepository airportRepository;

	@Mock
	private FlightTemplateRepository flightTemplateRepository;

	@Mock
	private PlaneRepository planeRepository;

	@Mock
	private FlightTemplate flightTemplate;

	@InjectMocks
	private FlightService flightService;

	private List<Flight> flights;

	@BeforeEach
	public void init() {
		flights = new ArrayList<>();
		Flight flight = createFlight(LocalDate.of(2023, 10, 10), true);
		Flight flight2 = createFlight(LocalDate.of(2022, 10, 10), false);
		Flight flight3 = createFlight(LocalDate.of(2024, 10, 10), false);
		Flight flight4 = createFlight(LocalDate.of(2021, 10, 10), true);
		Collections.addAll(this.flights, flight, flight2, flight3, flight4);
	}

	@Test
	void getAllActiv_returnsEmptyList_whenThereAreNoActivFlights() {

		List<Flight> flightsNonActiv = new ArrayList<>();
		Flight flight = new Flight();
		flight.setDate(LocalDate.of(2022, 10, 10));
		flight.setActiv(true);
		flight.setCanceled(false);
		flightsNonActiv.add(flight);

		Mockito.when(this.flightRepository.findAll()).thenReturn(flightsNonActiv);
		assertEquals(this.flightService.getAllActiv().size(), 0);

	}

	@Test
	void getAllActiv_returnsNotEmptyList_whenThereAreActivFlights() {

		Mockito.when(this.flightRepository.findAll()).thenReturn(this.flights);
		assertEquals(this.flightService.getAllActiv().size(), 1);

	}

	@Test
	void getCanceledAndNotActivFlights_returnsNotEmptyList_whenThereaAreNonactivOrCanceledFlightsInTheDB() {

		Mockito.when(this.flightRepository.findAll()).thenReturn(this.flights);
		assertEquals(this.flightService.getCanceledAndNotActivFlights().size(), 3);
	}

	@Test
	void getCanceledAndNotActivFlights_returnsEmptyList_whenThereAreNoFlightsThatAreNotActivOrCanceledInTheDB() {

		List<Flight> flights = new ArrayList<>();
		Flight flight = new Flight();
		flight.setDate(LocalDate.of(2024, 10, 10));
		flight.setActiv(true);
		flight.setCanceled(false);
		flights.add(flight);

		Mockito.when(this.flightRepository.findAll()).thenReturn(flights);
		assertEquals(0, this.flightService.getCanceledAndNotActivFlights().size());
	}

	@Test
	void getFlightById_returnsFlight_whenThereIsAFlightWithTheIdInTheDB() {
		Flight flight = new Flight();
		flight.setIdFlight(10L);
		Mockito.when(this.flightRepository.findById(flight.getIdFlight())).thenReturn(Optional.of(flight));
		assertEquals(flight, this.flightService.getFlightById(flight.getIdFlight()).get());
	}

	@Test
	void getFlightById_returnsNull_whenThereIsNoFlightWithTheIdInTheDB() {

		Mockito.when(this.flightRepository.findById(10L)).thenReturn(Optional.ofNullable(null));
		assertEquals(Optional.ofNullable(null), this.flightService.getFlightById(10L));
	}

	@Test
	void getFlightsByFlightTemplate_returnsEmptyList_whenThereIsNoFlightCreatedWithAFlightTemplate() {
		FlightTemplate flightTemplateNotUsed = new FlightTemplate();
		Mockito.when(this.flightRepository.findAll()).thenReturn(this.flights);
		List<Flight> flightsWithTemplate = this.flightService.getFlightsByFlightTemplate(flightTemplateNotUsed);
		assertEquals(0, flightsWithTemplate.size());
	}

	@Test
	void getFlightsByFlightTemplate_returnsList_whenThereAreFlightsCreatedWithAFlightTemplate() {

		Mockito.when(this.flightRepository.findAll()).thenReturn(this.flights);
		List<Flight> flightsWithTemplate = this.flightService.getFlightsByFlightTemplate(flightTemplate);
		assertEquals(flights.size(), flightsWithTemplate.size());

	}
	
	@Test
	void deleteFlight_throwsFlightException_whenAFlightWithTheGivenIDIsNotInTheDB() {
		
		Mockito.when(this.flightRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
		FlightException exception = assertThrows(FlightException.class, () -> this.flightService.deleteFlight(1L));
		assertEquals("An flight with this ID is NOT in the DB!", exception.getMessage());
		assertEquals(ErrorCode.NOT_AN_EXISTING_FLIGHT, exception.getErrorCode());
	}
	
	@Test
	void deleteFlight_throwsNothing_whenTheFlightIsDeleted() throws FlightException {
		
		Flight flight = new Flight();
		Plane plane = new Plane();
		flight.setIdFlight(1L);
		flight.setPlane(plane);
		Mockito.when(this.flightRepository.findById(1L)).thenReturn(Optional.of(flight));
		this.flightService.deleteFlight(1L);
	}
	
	@Test
	void deleteFlight_throwsNothingAndDoesNotModifiesThePlane_whenTheFlightIsDeleted() throws FlightException {
		
		Flight flight = new Flight();
		Plane plane = new Plane();
		plane.setFirstFlight(LocalDate.of(2001, 8, 20));
		flight.setIdFlight(1L);
		flight.setDate(java.time.LocalDate.now());
		flight.setPlane(plane);
		Mockito.when(this.flightRepository.findById(1L)).thenReturn(Optional.of(flight));
		this.flightService.deleteFlight(1L);
	}
	
	@Test
	void deleteFlight_throwsNothingAndModifiesThePlane_whenTheFlightIsDeleted() throws FlightException {
		
		Flight flight = new Flight();
		Plane plane = new Plane();
		plane.setFirstFlight(java.time.LocalDate.now());
		flight.setIdFlight(1L);
		flight.setDate(java.time.LocalDate.now());
		flight.setPlane(plane);
		Mockito.when(this.flightRepository.findById(1L)).thenReturn(Optional.of(flight));
		this.flightService.deleteFlight(1L);
	}
	
	@Test
	void addFlight_throwsFlightException_whenTheFlightHasAnUnexistingFlightTemplate() {
		FlightDto flightDto = mock(FlightDto.class);
		flightDto.setFlightTemplateID(1L);
		Mockito.when(this.flightTemplateRepository.getTemplatesIds()).thenReturn(new ArrayList<>());
		FlightException exception = assertThrows(FlightException.class, () -> this.flightService.addFlight(flightDto));
		assertEquals("The flight cannot be made out of an unexisting flight template", exception.getMessage());
		assertEquals(ErrorCode.UNEXISTING_TEMPLATE, exception.getErrorCode());
	}
	
	//TODO
	@Test
	void addFlight_throwsFlightException_aFlightWithTheGivenIdExistsInTheDB() {
		FlightDto flightDto = mock(FlightDto.class);
		flightDto.setFlightTemplateID(1L);
		List<Long> templatesIDs = new ArrayList<>();
		templatesIDs.add(1L);
		Flight flight = mock(Flight.class);
		Mockito.when(this.flightTemplateRepository.getTemplatesIds()).thenReturn(templatesIDs);
		Mockito.when(this.flightRepository.findById(flightDto.getIdFlight())).thenReturn(Optional.of(flight));
		FlightException exception = assertThrows(FlightException.class, () -> this.flightService.addFlight(flightDto));
		assertEquals("An flight with this ID is in the DB!", exception.getMessage());
		assertEquals(ErrorCode.OBJECT_WITH_THIS_ID_EXISTS_IN_THE_DB, exception.getErrorCode());
	}

	private Flight createFlight(LocalDate date, Boolean canceled) {
		flights = new ArrayList<>();
		Flight flight = new Flight();
		flight.setDate(date);
		flight.setCanceled(canceled);
		flight.setFlightTemplate(flightTemplate);
		return flight;
	}
}
