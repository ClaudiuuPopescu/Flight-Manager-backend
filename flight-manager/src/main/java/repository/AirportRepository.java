package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import msg.project.flightmanager.model.Airport;

public interface AirportRepository extends CrudRepository<Airport, Long> {

	@Query("SELECT a FROM airport a where a.airportName = :airportName")
	Optional<Airport> findByName(@Param("airportName") String ariportName);

	@Query("SELECT a FROM airport a where a.codeIdentifier = :codeIdentifier")
	Optional<Airport> findByCodeIdentifier(@Param("codeIdentifier") String codeIdentifier);
	
	@Query("SELECT a FROM airport")
	List<Airport> getAll();
}
