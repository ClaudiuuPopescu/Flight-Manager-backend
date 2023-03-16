package msg.project.flightmanager.servicies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import msg.project.flightmanager.converter.ItineraryConverter;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Address;
import msg.project.flightmanager.model.Airport;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.Itinerary;
import msg.project.flightmanager.model.Plane;
import msg.project.flightmanager.modelHelper.EditItineraryModel;
import msg.project.flightmanager.modelHelper.ItineraryHelperModel;
import msg.project.flightmanager.repository.FlightRepository;
import msg.project.flightmanager.repository.ItineraryRepository;
import msg.project.flightmanager.service.ItineraryService;

@ExtendWith(MockitoExtension.class)
public class ItineraryServiceTest {
	@InjectMocks
	private ItineraryService service;
	@Mock
	private ItineraryRepository repository;
	@Mock
	private ItineraryConverter converter;
	@Mock
	private FlightRepository flightRepository;
	
	@Test
	void getAll_throwsFlightManagerException_whenNoItineraryFound() {
		
		Mockito.when(this.repository.findAll()).thenReturn(Collections.emptyList());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.getAll());
		
		assertEquals("No itineraries found", thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.getAll());
	}
	
	@Test
	void getAll_returnsListOfItineraries_whenItinerariesExistInDataBase() {
		Itinerary first_itinerary = new Itinerary();
		Itinerary second_itinerary = new Itinerary();
		
		Mockito.when(this.repository.findAll()).thenReturn(Arrays.asList(first_itinerary, second_itinerary));
		
		assertEquals(2,this.service.getAll().size());
	}
	
	@Test
	void createItinerary_throwsFlightManagerException_whenCanNotFindFlightById() {
		Long flightId = 5L;		
		ItineraryHelperModel model = new ItineraryHelperModel(20, "notes", flightId);
		
		Mockito.when(this.flightRepository.findById(flightId)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createItinerary(model));
		
		assertEquals(MessageFormat.format("Can not create itinerary. Flight with id [{0}] not found", flightId), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createItinerary(model));
	}
	
	@Test
	void createItinerary_throwsFlightManagerException_whenSeatsReservedGraterThanPlaneCapacity() {
		Long flightId = 5L;
		int seatsReserved = 100;	
		ItineraryHelperModel model = new ItineraryHelperModel(seatsReserved, "notes", flightId);
		
		Plane plane = new Plane();
		plane.setCapacity(50);
		
		Flight flight = new Flight();
		flight.setPlane(plane);
		
		Mockito.when(this.flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.createItinerary(model));
		
		assertEquals(MessageFormat.format(
				"Can not create itinerary. The number of seats reserved [{0}] can not be higher than the plane's capacity [{1}]",
				model.getSeatsReserved(), flight.getPlane().getCapacity()), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.createItinerary(model));
	}
	
	@Test
	void createItinerary_returnsTrue_whenAllConditionsGood01_notesEmpty() {
		Long flightId = 5L;		
		ItineraryHelperModel model = new ItineraryHelperModel(20, "", flightId);
		
		Plane plane = new Plane();
		plane.setCapacity(50);
		
		Flight flight = new Flight();
		flight.setPlane(plane);
		
		Itinerary itinerary = new Itinerary();
		
		Mockito.when(this.flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
		Mockito.when(this.converter.convertCreateModelToItinerary(model, flight)).thenReturn(itinerary);
		
		assertTrue(this.service.createItinerary(model));
		assertEquals("No notes yet", itinerary.getNotes());
	}
	
	@Test
	void createItinerary_returnsTrue_whenAllConditionsGood02_notesNull() {
		Long flightId = 5L;		
		ItineraryHelperModel model = new ItineraryHelperModel(20, null, flightId);
		
		Plane plane = new Plane();
		plane.setCapacity(50);
		
		Flight flight = new Flight();
		flight.setPlane(plane);
		
		Itinerary itinerary = new Itinerary();
		
		Mockito.when(this.flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
		Mockito.when(this.converter.convertCreateModelToItinerary(model, flight)).thenReturn(itinerary);
		
		assertTrue(this.service.createItinerary(model));
		assertEquals("No notes yet", itinerary.getNotes());
	}
	
	@Test
	void createItinerary_returnsTrue_whenAllConditionsGood03_notesExist() {
		Long flightId = 5L;
		ItineraryHelperModel model = new ItineraryHelperModel(20, "someNotes", flightId);
		
		Plane plane = new Plane();
		plane.setCapacity(50);
		
		Flight flight = new Flight();
		flight.setPlane(plane);
		
		Itinerary itinerary = new Itinerary();
		
		Mockito.when(this.flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
		Mockito.when(this.converter.convertCreateModelToItinerary(model, flight)).thenReturn(itinerary);
		
		assertTrue(this.service.createItinerary(model));
		assertEquals(model.getNotes(), itinerary.getNotes());
	}
	
	@Test
	void editItinerary_throwsFlightManagerException_whenCantFindItineraryById() {
		Long itineraryId = 5L;
		EditItineraryModel model = new EditItineraryModel(itineraryId, 20, "notes", 1L, 2L);
		
		Mockito.when(this.repository.findById(itineraryId)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.editItinerary(model));
		
		assertEquals(MessageFormat.format("Can not edit itinerary. Itinerary with id [{0}] not found",
				model.getItineraryId()), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.editItinerary(model));
	}
	
	@Test
	void editItinerary_throwsFlightManagerException_whenCantFindItineraryFlightById() {
		Long itineraryId = 5L;
		Long currentFlightId = 10L;
		EditItineraryModel model = new EditItineraryModel(itineraryId, 20, "notes", currentFlightId, 2L);
		
		Itinerary itinerary = new Itinerary();
		
		Mockito.when(this.repository.findById(itineraryId)).thenReturn(Optional.of(itinerary));
		Mockito.when(this.flightRepository.findById(currentFlightId)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.editItinerary(model));
		
		assertEquals(MessageFormat.format("Can not edit itinerary. Flight with id [{0}] not found",
				model.getCurrentFlightId()), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.editItinerary(model));
	}
	
	@Test
	void editItinerary_throwsFlightManagerException_whenNewFlightIdNullAndSeatesReservedGraterThanCapacity() {
		Long itineraryId = 5L;
		Long currentFlightId = 10L;
		EditItineraryModel model = new EditItineraryModel(itineraryId, 20, "notes", currentFlightId, null);
		
		Itinerary itinerary = new Itinerary();
		
		Plane plane = new Plane();
		plane.setCapacity(10);
		
		Flight currentFlight = new Flight();
		currentFlight.setPlane(plane);
		
		Mockito.when(this.repository.findById(itineraryId)).thenReturn(Optional.of(itinerary));
		Mockito.when(this.flightRepository.findById(currentFlightId)).thenReturn(Optional.of(currentFlight));
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.editItinerary(model));
		
		assertEquals(MessageFormat.format(
				"Can not update itinerary. The number of seats reserved [{0}] can not be higher than the plane's capacity [{1}]",
				model.getSeatsReserved(), currentFlight.getPlane().getCapacity()), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.editItinerary(model));
	}
	
	@Test
	void editItinerary_returnsTrue_whenAllConditionsGood01withCurrentFlightAndNewFlightNull() {
		Long itineraryId = 5L;
		Long currentFlightId = 10L;
		EditItineraryModel model = new EditItineraryModel(itineraryId, 20, "notes", currentFlightId, null);
		
		Itinerary itinerary = new Itinerary();
		
		Plane plane = new Plane();
		plane.setCapacity(100);
		
		Flight currentFlight = new Flight();
		currentFlight.setPlane(plane);
		
		Mockito.when(this.repository.findById(itineraryId)).thenReturn(Optional.of(itinerary));
		Mockito.when(this.flightRepository.findById(currentFlightId)).thenReturn(Optional.of(currentFlight));
		
		assertTrue(this.service.editItinerary(model));
	}
	
	@Test
	void editItinerary_returnsTrue_whenAllConditionsGood02withSameFlight() {
		Long itineraryId = 5L;
		Long currentFlightId = 10L;
		Long newFlightId = 10L;
		EditItineraryModel model = new EditItineraryModel(itineraryId, 20, "notes", currentFlightId, newFlightId);
		
		Itinerary itinerary = new Itinerary();
		
		Plane plane = new Plane();
		plane.setCapacity(100);
		
		Flight currentFlight = new Flight();
		currentFlight.setPlane(plane);
		
		Mockito.when(this.repository.findById(itineraryId)).thenReturn(Optional.of(itinerary));
		Mockito.when(this.flightRepository.findById(currentFlightId)).thenReturn(Optional.of(currentFlight));
		
		assertTrue(this.service.editItinerary(model));
	}
	
	@Test
	void editItinerary_throwsFlightManagerException_whenCantFindNewFlightById() {
		Long itineraryId = 5L;
		Long currentFlightId = 10L;
		Long newFlightId = 15L;
		EditItineraryModel model = new EditItineraryModel(itineraryId, 20, "notes", currentFlightId, newFlightId);
		
		Itinerary itinerary = new Itinerary();
		
		Flight currentFlight = new Flight();
		
		Mockito.when(this.repository.findById(itineraryId)).thenReturn(Optional.of(itinerary));
		Mockito.when(this.flightRepository.findById(currentFlightId)).thenReturn(Optional.of(currentFlight));
		Mockito.when(this.flightRepository.findById(newFlightId)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.editItinerary(model));
		
		assertEquals(MessageFormat.format("Can not edit itinerary. The new flight with id [{0}] not found",
				model.getNewFlightId()), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.editItinerary(model));
	}
	
	@Test
	void editItinerary_throwsFlightManagerException_whenSeatesReservedGraterThanCapacityNewFlightPlane() {
		Long itineraryId = 5L;
		Long currentFlightId = 10L;
		Long newFlightId = 15L;
		EditItineraryModel model = new EditItineraryModel(itineraryId, 20, "notes", currentFlightId, newFlightId);
		
		Itinerary itinerary = new Itinerary();
		
		Plane plane = new Plane();
		plane.setCapacity(10);
		
		Flight newFlight = new Flight();
		newFlight.setPlane(plane);
		
		Mockito.when(this.repository.findById(itineraryId)).thenReturn(Optional.of(itinerary));
		Mockito.when(this.flightRepository.findById(currentFlightId)).thenReturn(Optional.of(new Flight()));
		Mockito.when(this.flightRepository.findById(newFlightId)).thenReturn(Optional.of(newFlight));
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.editItinerary(model));
		
		assertEquals(MessageFormat.format(
				"Can not update itinerary. The number of seats reserved [{0}] can not be higher than the plane's capacity [{1}]",
				model.getSeatsReserved(), newFlight.getPlane().getCapacity()), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.editItinerary(model));
	}
	
	@Test
	void editItinerary_returnsTrue_whenAllConditionsGood03withNewFlight() {
		Long itineraryId = 5L;
		Long currentFlightId = 10L;
		Long newFlightId = 15L;
		EditItineraryModel model = new EditItineraryModel(itineraryId, 20, "notes", currentFlightId, newFlightId);
		
		Itinerary itinerary = new Itinerary();
		
		Plane plane = new Plane();
		plane.setCapacity(100);
		
		Address addressFrom = new Address();
		addressFrom.setCountry("countryFrom");
		addressFrom.setCity("cityFrom");
		
		Address addressTo = new Address();
		addressTo.setCountry("countryTo");
		addressTo.setCity("cityTo");
		
		Airport from = new Airport();
		from.setAddress(addressFrom);
		
		Airport to = new Airport();
		to.setAddress(addressTo);
		
		Flight newFlight = new Flight();
		newFlight.setPlane(plane);
		newFlight.setFrom(from);
		newFlight.setTo(to);
		
		Mockito.when(this.repository.findById(itineraryId)).thenReturn(Optional.of(itinerary));
		Mockito.when(this.flightRepository.findById(currentFlightId)).thenReturn(Optional.of(new Flight()));
		Mockito.when(this.flightRepository.findById(newFlightId)).thenReturn(Optional.of(newFlight));
		
		assertTrue(this.service.editItinerary(model));
	}
	
	@Test
	void removeItinerary_throwsFlightManagerException_whenCantFindItineraryById() {
		Long itineraryId = 5L;
		
		Mockito.when(this.repository.findById(itineraryId)).thenReturn(Optional.empty());
		
		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.service.removeItinerary(itineraryId));
		
		assertEquals(MessageFormat.format("Can not delete itinerary. Itinerary with id [{0}] not found", itineraryId), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.service.removeItinerary(itineraryId));
		
	}
	
	@Test
	void removeItinerary_returnsTrue_allConditionsGood() {
		Long itineraryId = 5L;
		
		Itinerary itinerary = new Itinerary();
		
		Mockito.when(this.repository.findById(itineraryId)).thenReturn(Optional.of(itinerary));
		
		assertTrue(this.service.removeItinerary(itineraryId));
	}

}
