package msg.project.flightmanager.service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import msg.project.flightmanager.converter.ItineraryConverter;
import msg.project.flightmanager.dto.ItineraryDto;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.Itinerary;
import msg.project.flightmanager.modelHelper.EditItineraryModel;
import msg.project.flightmanager.modelHelper.ItineraryHelperModel;
import msg.project.flightmanager.repository.FlightRepository;
import msg.project.flightmanager.repository.ItineraryRepository;
import msg.project.flightmanager.service.interfaces.IItineraryService;

@Service
public class ItineraryService implements IItineraryService {
	@Autowired
	private ItineraryRepository itineraryRepository;
	@Autowired
	private ItineraryConverter itineraryConverter;
	@Autowired
	private FlightRepository flightRepository;

	@Override
	public List<ItineraryDto> getAll() {
		List<Itinerary> itineraries = StreamSupport.stream(this.itineraryRepository.findAll().spliterator(), false)
				.toList();
		
		if(itineraries.isEmpty()) {
			throw new FlightManagerException(HttpStatus.NO_CONTENT, "No itineraries found"); 
		}

		return itineraries.stream().map(this.itineraryConverter::convertToDTO).toList();
	}

	@Transactional
	@Override
	public boolean createItinerary(ItineraryHelperModel itineraryHelperModel) {

		Flight flight = this.flightRepository.findById(itineraryHelperModel.getFlightId())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not create itinerary. Flight with id [{0}] not found",
								itineraryHelperModel.getFlightId())));

		if (itineraryHelperModel.getSeatsReserved() > flight.getPlane().getCapacity()) {
			throw new FlightManagerException(HttpStatus.CONFLICT,
					MessageFormat.format(
							"Can not create itinerary. The number of seats reserved [{0}] can not be higher than the plane's capacity [{1}]",
							itineraryHelperModel.getSeatsReserved(), flight.getPlane().getCapacity()));
		}

		Itinerary itinerary = this.itineraryConverter.convertCreateModelToItinerary(itineraryHelperModel, flight);

		if (itineraryHelperModel.getNotes() == "" || itineraryHelperModel.getNotes() == null) {
			itinerary.setNotes("No notes yet");
		}else{
			itinerary.setNotes(itineraryHelperModel.getNotes());
		}
		
		this.itineraryRepository.save(itinerary);
		return true;
	}

	@Transactional
	@Override
	public boolean editItinerary(EditItineraryModel editItineraryModel) {

		Itinerary itinerary = this.itineraryRepository.findById(editItineraryModel.getItineraryId())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not edit itinerary. Itinerary with id [{0}] not found",
								editItineraryModel.getItineraryId())));

		Flight currentFlight = this.flightRepository.findById(editItineraryModel.getCurrentFlightId())
					.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
							MessageFormat.format("Can not edit itinerary. Flight with id [{0}] not found",
								editItineraryModel.getCurrentFlightId())));


		if (editItineraryModel.getNewFlightId() == null || editItineraryModel.getCurrentFlightId() == editItineraryModel.getNewFlightId()) {		

			validateEditItineraryReservedSeats(editItineraryModel, currentFlight.getPlane().getCapacity());

			itinerary.setSeatsReserved(editItineraryModel.getSeatsReserved());
			this.itineraryRepository.save(itinerary);
			return true;
			
		} else {
		
			Flight newFlight = this.flightRepository.findById(editItineraryModel.getNewFlightId())
					.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
							MessageFormat.format("Can not edit itinerary. The new flight with id [{0}] not found",
									editItineraryModel.getNewFlightId())));

			validateEditItineraryReservedSeats(editItineraryModel, newFlight.getPlane().getCapacity());

			itinerary.setSeatsReserved(editItineraryModel.getSeatsReserved());
			itinerary.setFlight(newFlight);
			setItineraryNewFLight(itinerary, newFlight);
			this.itineraryRepository.save(itinerary);
			return true;
		}
	}

	@Transactional
	@Override
	public boolean removeItinerary(Long idItinerary) {

		Itinerary itinerary = this.itineraryRepository.findById(idItinerary)
				.orElseThrow(() -> new FlightManagerException(
						HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not delete itinerary. Itinerary with id [{0}] not found", idItinerary)));
		
		this.itineraryRepository.delete(itinerary);
		return true;
	}

	private void validateEditItineraryReservedSeats(EditItineraryModel editItineraryModel, int capacity) {

		if (editItineraryModel.getSeatsReserved() > capacity) {
			throw new FlightManagerException(HttpStatus.CONFLICT, MessageFormat.format(
					"Can not update itinerary. The number of seats reserved [{0}] can not be higher than the plane's capacity [{1}]",
					editItineraryModel.getSeatsReserved(), capacity));
		}
	}

	private void setItineraryNewFLight(Itinerary itinerary, Flight newFlight) {
		itinerary.setBoardingTime(newFlight.getBoardingTime());
		itinerary.setDuration(newFlight.getDuration());
		itinerary.setSeatsTotal(newFlight.getPlane().getCapacity());
		itinerary.setFromCountry(newFlight.getFrom().getAddress().getCountry());
		itinerary.setFromCity(newFlight.getFrom().getAddress().getCity());
		itinerary.setToCountry(newFlight.getTo().getAddress().getCountry());
		itinerary.setToCity(newFlight.getTo().getAddress().getCity());
		itinerary.setDate(newFlight.getDate());
	}
}
