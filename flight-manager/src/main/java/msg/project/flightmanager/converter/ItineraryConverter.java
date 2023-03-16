package msg.project.flightmanager.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import msg.project.flightmanager.dto.ItineraryDto;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.Itinerary;
import msg.project.flightmanager.modelHelper.ItineraryHelperModel;

@Component
public class ItineraryConverter implements IConverter<Itinerary, ItineraryDto> {
	@Autowired
	private FlightConverter flightConverter;

	@Override
	public ItineraryDto convertToDTO(Itinerary itinerary) {
		return ItineraryDto.builder()
				.id_itinerary(itinerary.getId_itinerary()).seatsReserved(itinerary.getSeatsReserved())
				.flight(this.flightConverter.convertToDTO(itinerary.getFlight()))
				.seatsTotal(itinerary.getFlight().getPlane().getCapacity()).fromCountry(itinerary.getFromCountry())
				.fromCity(itinerary.getFromCity()).toCountry(itinerary.getToCountry()).toCity(itinerary.getToCity())
				.date(itinerary.getDate())
				.build();
	}

	@Override
	public Itinerary convertToEntity(ItineraryDto itineraryDto) {
		// TODO Auto-generated method stub
		return null;
	}

	public Itinerary convertCreateModelToItinerary(ItineraryHelperModel itineraryHelperModel, Flight flight) {
		return Itinerary.builder()
				.seatsReserved(itineraryHelperModel.getSeatsReserved())
				.flight(flight)
				.boardingTime(flight.getBoardingTime()).duration(flight.getDuration())
				.seatsTotal(flight.getPlane().getCapacity()).fromCountry(flight.getFrom().getAddress().getCountry())
				.fromCity(flight.getFrom().getAddress().getCity()).toCountry(flight.getTo().getAddress().getCountry())
				.toCity(flight.getTo().getAddress().getCity()).date(flight.getDate()).build();
	}

}
