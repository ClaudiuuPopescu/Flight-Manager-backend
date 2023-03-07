package service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import converter.ItineraryConverter;
import dto.ItineraryDto;
import exceptions.FlightManagerException;
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

		Itinerary itinerary = this.itineraryConverter.convertCreateModelToItinerary(itineraryHelperModel, flight);

		this.itineraryRepository.save(itinerary);
		return true;
	}

	@Override
	public boolean editItinerary(ItineraryHelperModel itineraryHelperModel) {
		// TODO check role current user
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

}
