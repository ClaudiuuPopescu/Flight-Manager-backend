package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import enums.RoleEnum;
import msg.project.flightmanager.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	@Query("SELECT r FROM role r where r.roleEnum = :roleEnum")
	Optional<Role> findByEnum(@Param("roleEnum") RoleEnum roleEnum);
	
	@Query("SELECT r FROM role")
	List<Role> getAll();
}
