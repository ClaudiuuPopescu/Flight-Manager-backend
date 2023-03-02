package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import msg.project.flightmanager.model.Plane;

public interface PlaneRepository extends CrudRepository<Plane, Long> {

	@Query("SELECT p FROM plane p where p.tailNumber = :tailNumber")
	Optional<Plane> findByTailNumber(@Param("tailNumber") int tailNumber);
}
