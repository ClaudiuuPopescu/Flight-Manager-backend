package msg.project.flightmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import msg.project.flightmanager.model.Flight;

@Repository
@Transactional
public interface FlightRepository extends CrudRepository<Flight, Long> {

	@Override
	List<Flight> findAll();

	@Query("SELECT f FROM Flight f WHERE f.flightName = :name")
	Optional<Flight> findFlightByName(@Param("name") String name);
	
	@Query("SELECT f FROM Flight f WHERE f.idFlight = :flightId")
	Optional<Flight> findFlightById(@Param("flightId") Long flightId);
}
