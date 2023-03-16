package msg.project.flightmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import msg.project.flightmanager.model.RefreshToken;
import msg.project.flightmanager.model.User;

public interface RefreshTokenRepositoy extends CrudRepository<RefreshToken, Long>{
	
	@Query("SELECT rf FROM refreshtoken rf WHERE rf.token = :token")
	Optional<RefreshToken> getRefreshTokenByToken(@Param("token") String token);
	
	@Query("DELETE rf FROM refreshtoken rf WHERE rf.user = :user")
	void deleteRefreshTokenByUser(@Param("user") User user);

}
