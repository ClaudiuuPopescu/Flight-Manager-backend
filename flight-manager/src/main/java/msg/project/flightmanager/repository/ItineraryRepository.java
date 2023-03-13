package msg.project.flightmanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import msg.project.flightmanager.model.Itinerary;

@Repository
public interface ItineraryRepository extends CrudRepository<Itinerary, Long> {

}
