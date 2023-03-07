package service.interfaces;

import java.util.List;

import dto.ItineraryDto;
import modelHelper.ItineraryHelperModel;

public interface IItineraryService {

	List<ItineraryDto> getAll();

	boolean createItinerary(ItineraryHelperModel itineraryHelperModel);

	boolean editItinerary(ItineraryHelperModel itineraryHelperModel);

	boolean removeItinerary(Long idItinerary);

}
