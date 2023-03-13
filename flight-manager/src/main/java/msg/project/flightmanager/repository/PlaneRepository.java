package msg.project.flightmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import msg.project.flightmanager.model.Plane;

@Repository
public interface PlaneRepository extends CrudRepository<Plane, Long> {

	@Query("SELECT p FROM Plane p where p.tailNumber = :tailNumber")
	Optional<Plane> findByTailNumber(@Param("tailNumber") int tailNumber);
}
