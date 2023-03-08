package service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import converter.ItineraryConverter;
import dto.ItineraryDto;
import exceptions.FlightManagerException;
import modelHelper.EditItineraryModel;
import modelHelper.ItineraryHelperModel;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.Itinerary;
import repository.FlightRepository;
import repository.ItineraryRepository;
import service.interfaces.IItineraryService;

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

		return itineraries.stream().map(this.itineraryConverter::convertToDTO).toList();
	}

	@Override
	public boolean createItinerary(ItineraryHelperModel itineraryHelperModel) {
		// TODO check role current user

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

		if (itineraryHelperModel.getNotes().equals("") || itineraryHelperModel == null) {
			itinerary.setNotes("No notes yet");
		}

		itinerary.setNotes(itinerary.getNotes());
		this.itineraryRepository.save(itinerary);
		return true;
	}

	@Override
	public boolean editItinerary(EditItineraryModel editItineraryModel) {
		// TODO check role current user

		Itinerary itinerary = this.itineraryRepository.findById(editItineraryModel.getItineraryId())
				.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
						MessageFormat.format("Can not edit itinerary. Itinerary with id [{0}] not found",
								editItineraryModel.getItineraryId())));

		Flight currentFlight = this.flightRepository.findById(editItineraryModel.getCurrentFlight())
					.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
							MessageFormat.format("Can not edit itinerary. Flight with id [{0}] not found",
								editItineraryModel.getCurrentFlight())));


		if (editItineraryModel.getNewFlightId() == null) {
			if (editItineraryModel.getSeatsReserved() > currentFlight.getPlane().getCapacity()) {
				throw new FlightManagerException(HttpStatus.CONFLICT, MessageFormat.format(
						"Can not update itinerary. The number of seats reserved [{0}] can not be higher than the current plane's capacity [{1}]",
						editItineraryModel.getSeatsReserved(), currentFlight.getPlane().getCapacity()));
			}

			validateEditItineraryReservedSeats(editItineraryModel, currentFlight.getPlane().getCapacity());

			itinerary.setSeatsReserved(editItineraryModel.getSeatsReserved());
			this.itineraryRepository.save(itinerary);
			return true;
		}


		if (editItineraryModel.getNewFlightId() != null) {

			Flight newFlight = this.flightRepository.findById(editItineraryModel.getNewFlightId())
					.orElseThrow(() -> new FlightManagerException(HttpStatus.NOT_FOUND,
							MessageFormat.format("Can not edit itinerary. New flight with id [{0}] not found",
									editItineraryModel.getNewFlightId())));

			validateEditItineraryReservedSeats(editItineraryModel, newFlight.getPlane().getCapacity());

			itinerary.setSeatsReserved(editItineraryModel.getSeatsReserved());
			itinerary.setFlight(newFlight);
			setItineraryNewFLight(itinerary, newFlight);
			this.itineraryRepository.save(itinerary);
			return true;
		}

		return false;
	}

	@Override
	public boolean removeItinerary(Long idItinerary) {
		// TODO check role current user

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
					"Can not edit itinerary. The number of seats reserved [{0}] can not be higher than the plane's capacity [{1}]",
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
