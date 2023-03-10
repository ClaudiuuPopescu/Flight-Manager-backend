package msg.project.flightmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import msg.project.flightmanager.model.Permission;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {

	@Query("SELECT p FROM Permission p where p.title = :title")
	Optional<Permission> findByTitle(@Param("title") String title);

}
