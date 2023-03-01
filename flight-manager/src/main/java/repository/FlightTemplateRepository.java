package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import msg.project.flightmanager.model.FlightTemplate;

@Repository
@Transactional
public interface FlightTemplateRepository extends CrudRepository<FlightTemplate, Long> {

	List<FlightTemplate> findAll();

	Optional<FlightTemplate> findFlightTemplateByID(Long idFlightTEmplate);

}
