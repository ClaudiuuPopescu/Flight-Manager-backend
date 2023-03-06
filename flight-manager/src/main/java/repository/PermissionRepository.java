package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import msg.project.flightmanager.model.Permission;

public interface PermissionRepository extends CrudRepository<Permission, Long> {

	@Query("SELECT p FROM permission p where p.permissionEnum = :permissionEnum")
	Optional<Permission> findByEnum(@Param("permisionEnum") String permissionEnum);

}
