package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import msg.project.flightmanager.model.Company;

@Repository
@Transactional
public interface CompanyRepository extends CrudRepository<Company, Long> {

	List<Company> findAll();

	@Query("SELECT c FROM Company WHERE c.name = name")
	Optional<Company> findCompanyByName(String name);

}
