package msg.project.flightmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import msg.project.flightmanager.model.Airport;

@Repository
public interface AirportRepository extends CrudRepository<Airport, Long> {

	@Query("SELECT a FROM Airport a where a.airportName = :airportName")
	Optional<Airport> findByName(@Param("airportName") String ariportName);

	@Query("SELECT a FROM Airport a where a.codeIdentifier = :codeIdentifier")
	Optional<Airport> findByCodeIdentifier(@Param("codeIdentifier") String codeIdentifier);
}
