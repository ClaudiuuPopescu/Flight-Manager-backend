package msg.project.flightmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import msg.project.flightmanager.model.FlightTemplate;

@Repository
@Transactional
public interface FlightTemplateRepository extends CrudRepository<FlightTemplate, Long> {

	@Override
	List<FlightTemplate> findAll();
	
	@Query("SELECT ft FROM FlightTemplate ft where ft.idFlightTemplate = :idFlightTEmplate")
	Optional<FlightTemplate> findFlightTemplateByID(@Param("idFlightTEmplate") Long idFlightTEmplate);
	
	@Query("SELECT idFlightTemplate FROM FlightTemplate")
	List<Long> getTemplatesIds();

}
