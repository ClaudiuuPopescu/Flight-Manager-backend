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

	@Query("SELECT ft FROM FlightTemplate ft")
	List<FlightTemplate> findAll();
	
	@Query("SELECT ft FROM FlightTemplate ft where ft.idFlightTemplate = :idFlightTemplate")
	Optional<FlightTemplate> findFlightTemplateByID(@Param("idFlightTEmplate") Long idFlightTemplate);
	
	@Query("SELECT idFlightTemplate FROM FlightTemplate")
	List<Long> getTemplatesIds();

}
