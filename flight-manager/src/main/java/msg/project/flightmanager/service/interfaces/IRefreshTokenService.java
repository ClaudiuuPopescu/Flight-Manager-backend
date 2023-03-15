package msg.project.flightmanager.service.interfaces;

import java.util.Optional;

import msg.project.flightmanager.model.RefreshToken;
import msg.project.flightmanager.model.User;

public interface IRefreshTokenService {

	Optional<RefreshToken> findByToken(String token);
	RefreshToken createRefreshToken(User user);
	void verifyExpiration(RefreshToken token);
	int deleteByUserName(String username);
}
