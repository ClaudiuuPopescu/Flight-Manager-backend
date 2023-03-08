package service.interfaces;

import java.util.List;

import dto.ItineraryDto;
import modelHelper.EditItineraryModel;
import modelHelper.ItineraryHelperModel;

public interface IItineraryService {

	List<ItineraryDto> getAll();

	boolean createItinerary(ItineraryHelperModel itineraryHelperModel);

	boolean editItinerary(EditItineraryModel editItineraryModel);

	boolean removeItinerary(Long idItinerary);

}
