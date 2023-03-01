package repository;

import org.springframework.data.repository.CrudRepository;

import msg.project.flightmanager.model.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {

}
