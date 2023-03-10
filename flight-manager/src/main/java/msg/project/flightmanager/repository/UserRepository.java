package msg.project.flightmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import msg.project.flightmanager.model.User;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

	@Query("SELECT u FROM User u where u.username = :username")
	Optional<User> findByUsername(@Param("username") String username);

	@Query("SELECT u FROM User u where u.phoneNumber = :phoneNumber")
	Optional<User> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

	@Query("SELECT u FROM User u where u.email = :email")
	Optional<User> findByEmail(@Param("email") String email);
}
