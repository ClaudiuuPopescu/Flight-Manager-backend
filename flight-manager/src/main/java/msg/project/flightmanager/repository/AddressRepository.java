package msg.project.flightmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import msg.project.flightmanager.model.Address;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

	@Override
	@Query("SELECT a FROM Address a WHERE a.idAddress = :idAddress")
	Optional<Address> findById(@Param("idAddress") Long idAddress);

	@Query("SELECT a FROM Address a WHERE a.country = :country AND a.city = :city AND a.street = :street AND a.streetNumber = :streetNumber AND a.apartment = :apartment")
	Optional<Address> findByAllAttributes(@Param("country") String country, @Param("city") String city,
			@Param("street") String street, @Param("streetNumber") int streetNumber, @Param("apartment") int apartment);
}
