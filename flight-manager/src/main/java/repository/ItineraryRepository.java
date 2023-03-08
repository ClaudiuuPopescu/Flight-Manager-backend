package repository;

import org.springframework.data.repository.CrudRepository;

import msg.project.flightmanager.model.Itinerary;

public interface ItineraryRepository extends CrudRepository<Itinerary, Long> {

}
