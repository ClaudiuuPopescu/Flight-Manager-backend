package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import msg.project.flightmanager.model.Flight;

public interface FlightRepository extends CrudRepository<Flight, Long> {

	List<Flight> findAll();

	@Query("SELECT f FROM Flight WHERE f.flightName = name")
	Optional<Flight> findFlightByName(String name);


}
