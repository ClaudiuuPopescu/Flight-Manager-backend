package msg.project.flightmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import msg.project.flightmanager.model.Address;
import msg.project.flightmanager.model.Company;

@Repository
@Transactional
public interface CompanyRepository extends CrudRepository<Company, Long> {

	@Override
	List<Company> findAll();

	@Query("SELECT c FROM Company c WHERE c.name = :name")
	Optional<Company> findCompanyByName(@Param("name") String name);
	
	@Query("SELECT c FROM Company c WHERE c.phone_number=:phoneNumber")
	Optional<Company> findCompanyByPhoneNumber(@Param("phoneNumber") String phoneNumber);
	
	@Query("SELECT c FROM Company c WHERE c.email = :email")
	Optional<Company> findCompanyByEmail(@Param("email") String email);
	
	@Query("SELECT c FROM Company c WHERE c.address_id = :address")
	Optional<Company> findCompanyByAddress(@Param("address") Address address);

}
