package msg.project.flightmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import msg.project.flightmanager.model.Role;

@Repository
@Transactional
public interface RoleRepository extends CrudRepository<Role, Long> {

	@Query("SELECT r FROM Role r where r.title = :roleTitle")
	Optional<Role> findByTitle(@Param("roleTitle") String roleTitle);
}
