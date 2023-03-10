package msg.project.flightmanager.service.interfaces;

import java.util.List;

import msg.project.flightmanager.dto.ItineraryDto;
import msg.project.flightmanager.modelHelper.EditItineraryModel;
import msg.project.flightmanager.modelHelper.ItineraryHelperModel;

public interface IItineraryService {

	List<ItineraryDto> getAll();

	boolean createItinerary(ItineraryHelperModel itineraryHelperModel);

	boolean editItinerary(EditItineraryModel editItineraryModel);

	boolean removeItinerary(Long idItinerary);

}
