package msg.project.flightmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import msg.project.flightmanager.model.RefreshToken;
import msg.project.flightmanager.model.User;


@Repository
public interface RefreshTokenRepositoy extends CrudRepository<RefreshToken, Long>{
	
	@Query("SELECT rf FROM refreshtoken rf WHERE rf.token = :token")
	Optional<RefreshToken> getRefreshTokenByToken(@Param("token") String token);
	
	@Query("SELECT rf FROM refreshtoken rf WHERE rf.user = :user")
	Optional<RefreshToken> getRefreshTokenByUser(@Param("user") User user);
//	
//	@Query("DELETE FROM refreshtoken rf WHERE rf.user = :user")
//	void deleteRefreshTokenByUser(@Param("user") User user);

}
