package msg.project.flightmanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import msg.project.flightmanager.model.Itinerary;

@Repository
@Transactional
public interface ItineraryRepository extends CrudRepository<Itinerary, Long> {

}
